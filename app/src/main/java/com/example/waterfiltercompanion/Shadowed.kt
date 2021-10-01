package com.example.waterfiltercompanion

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.example.waterfiltercompanion.ui.screen.main.MainActivity
import com.google.android.renderscript.Toolkit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.system.measureTimeMillis

@Composable
fun Shadowed(
    modifier: Modifier = Modifier,
    radiusPx: Int = 25,
    elevation: Dp,
    alpha: Float = 0.4f,
    flow: (Flow<Unit>)? = null,
    content: @Composable () -> Unit
) {
    var shadowBitmap: ImageBitmap? by remember { mutableStateOf(null) }
    val elevationPx: Float = with(LocalDensity.current) {
         remember(elevation) { elevation.toPx() }
    }
    var shadowedView: ShadowedView? by remember { mutableStateOf(null) }
    LaunchedEffect(radiusPx) {
        shadowedView?.radiusPx = radiusPx
    }
    AndroidView(
        factory = { context ->
            ShadowedView(
                context = context,
                dynamic = flow != null,
                content = content
            ) {
                shadowBitmap = it.asImageBitmap()
            }.also { shadowedView = it }
        },
        modifier = modifier.drawRealShadow(
            shadowBitmap = shadowBitmap,
            radiusPx = radiusPx,
            elevationPx = elevationPx,
            alpha = alpha
        )
    )
    LaunchedEffect(flow) {
        flow?.collect {
            shadowedView?.updateShadow()
        }
    }
}

private fun Modifier.drawRealShadow(
    shadowBitmap: ImageBitmap?,
    radiusPx: Int,
    elevationPx: Float,
    alpha: Float
): Modifier = this.then(
    DrawRealShadowModifier(
        shadowBitmap = shadowBitmap,
        radiusPx = radiusPx,
        elevationPx = elevationPx,
        alpha = alpha
    )
)

private class DrawRealShadowModifier(
    private val shadowBitmap: ImageBitmap?,
    private val radiusPx: Int,
    private val elevationPx: Float,
    private val alpha: Float
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        if (!size.isEmpty() && shadowBitmap != null) {
            drawImage(
                image = shadowBitmap,
                topLeft = Offset(
                    x = -radiusPx.toFloat(),
                    y = -radiusPx.toFloat() + elevationPx
                ),
                alpha = alpha
            )
        }
        drawContent()
    }
}

private class ShadowedView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val dynamic: Boolean,
    private val content: @Composable () -> Unit,
    private val onBitmapUpdate: (Bitmap) -> Unit
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var cleanBitmap: Bitmap? = null
    private var blurBitmap: Bitmap? = null
    private var blurBitmapToDisplay: Bitmap? = null
    private val cleanBitmapCanvas: Canvas = Canvas()
    private val blurBitmapCanvas: Canvas = Canvas()
    private var job: Job? = null
    var radiusPx: Int = 1

    init {
//        setViewCompositionStrategy(
//            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//        )
    }

    @Composable
    override fun Content() {
        content()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("rsg", "onSizeChanged $w $h $oldw $oldh")
        if (!dynamic) updateShadow()
    }

    fun updateShadow() {
        Log.d("rsg", "update shadow, job = $job")
        job?.cancel()
        generateEmptyBitmapsIfNeeded()
        val cleanBitmap = cleanBitmap
        val blurBitmap = blurBitmap
        val blurBitmapToDisplay = blurBitmapToDisplay
        if (cleanBitmap == null || blurBitmap == null || blurBitmapToDisplay == null) return
        post {
            job = (context as MainActivity).lifecycleScope.launch(Dispatchers.Default) {
                val drawToBitmapMillis = measureTimeMillis {
                    withContext(Dispatchers.Main) {
                        drawToBitmap(cleanBitmap)
                    }
                }
                val blurMillis = measureTimeMillis { Toolkit.blur(cleanBitmap, blurBitmap, radiusPx) }
                withContext(Dispatchers.Main) {
                    onBitmapUpdate(blurBitmap)
                }
                val cloneMillis = measureTimeMillis { blurBitmap copyTo blurBitmapToDisplay }
                Log.d("rsg", "drawToBitmapMillis = $drawToBitmapMillis | blurMillis = $blurMillis | cloneMillis = $cloneMillis")
                withContext(Dispatchers.Main) {
                    onBitmapUpdate(blurBitmapToDisplay)
                }
            }
        }
    }

    private fun generateEmptyBitmapsIfNeeded() {
        cleanBitmap = generateEmptyBitmapIfNeeded(cleanBitmap)
        blurBitmap = generateEmptyBitmapIfNeeded(blurBitmap)
        blurBitmapToDisplay = generateEmptyBitmapIfNeeded(blurBitmapToDisplay)
    }

    private fun generateEmptyBitmapIfNeeded(currentBitmap: Bitmap? = null): Bitmap? {
        return getExpectedBitmapSize()?.let { expectedSize ->
            currentBitmap?.takeIf { it.matchesExpectedSpecs(expectedSize) } ?: Bitmap.createBitmap(
                expectedSize.width.toInt(),
                expectedSize.height.toInt(),
                Bitmap.Config.ARGB_8888
            )
        }
    }

    private fun getExpectedBitmapSize(): Size? {
        if (width == 0 || height == 0) return null
        val extraSpace = 2 * radiusPx.toFloat()
        val bitmapWidth = width + extraSpace
        val bitmapHeight = height + extraSpace
        return Size(
            width = bitmapWidth,
            height = bitmapHeight
        )
    }

    private fun Bitmap.matchesExpectedSpecs(
        expectedBitmapSize: Size? = getExpectedBitmapSize()
    ): Boolean {
        return expectedBitmapSize?.let {
            width == it.width.toInt() && height == it.height.toInt()
        } ?: false
    }

    private infix fun Bitmap.copyTo(target: Bitmap) {
        blurBitmapCanvas.let {
            it.setBitmap(blurBitmapToDisplay)
            target.eraseColor(0)
            it.drawBitmap(this, 0f, 0f, null)
        }
    }

    private fun drawToBitmap(target: Bitmap) {
        cleanBitmapCanvas.let {
            it.setBitmap(target)
            target.eraseColor(0)
            it.save()
            it.translate(radiusPx.toFloat(), radiusPx.toFloat())
            draw(it)
            it.restore()
        }
    }
}