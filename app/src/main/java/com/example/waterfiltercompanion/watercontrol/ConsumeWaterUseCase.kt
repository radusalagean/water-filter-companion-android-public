package com.example.waterfiltercompanion.watercontrol

import com.example.waterfiltercompanion.datapersistence.LocalRepository

class ConsumeWaterUseCase(
    private val localRepository: LocalRepository
) {

    /**
     * Takes in the current capacity, consumes the predefined amount, persists the
     *  remaining capacity after consumption
     *
     *  Returns true if water was consumed, false otherwise
     */
    suspend operator fun invoke(currentCapacity: Int?): Boolean {
        if (currentCapacity == null) return false
        currentCapacity.takeIf { it >= UNITS_TO_CONSUME }?.let {
            val newCapacity = currentCapacity - UNITS_TO_CONSUME
            localRepository.setRemainingCapacity(newCapacity)
            return true
        }
        return false
    }

    companion object {
        const val UNITS_TO_CONSUME = 1
    }
}