package com.example.tracker.msc

import com.example.tracker.listener.StepListener

class StepDetector {

    private val ACCELERATION_RING_SIZE = 50
    private val SPEED_RING_SIZE = 10


    private val STEP_THRESHOLD = 50f

    private val STEP_DELAY = 250000000 //Ns

    private var accelRingCounter = 0
    private val accelRingX = FloatArray(ACCELERATION_RING_SIZE)
    private val accelRingY = FloatArray(ACCELERATION_RING_SIZE)
    private val accelRingZ = FloatArray(ACCELERATION_RING_SIZE)
    private var velRingCounter = 0
    private val velRing = FloatArray(SPEED_RING_SIZE)
    var  lastStepTimeNs: Long = 0
    private var oldSpeedEstimate = 0f

    private var listener: StepListener? = null

    fun registerListener(listener: StepListener) {
        this.listener = listener
    }

    fun updateAccelerometer(timeNs: Long, x: Float, y: Float, z: Float) {
        val currentAccel = FloatArray(3)
        currentAccel[0] = x
        currentAccel[1] = y
        currentAccel[2] = z

        //update globalnego wektora z
        accelRingCounter++
        accelRingX[accelRingCounter % ACCELERATION_RING_SIZE] = currentAccel[0]
        accelRingY[accelRingCounter % ACCELERATION_RING_SIZE] = currentAccel[1]
        accelRingZ[accelRingCounter % ACCELERATION_RING_SIZE] = currentAccel[2]

        val worldZ = FloatArray(3)
        worldZ[0] = SensorFilter().sum(accelRingX) / Math.min(accelRingCounter, ACCELERATION_RING_SIZE)
        worldZ[1] = SensorFilter().sum(accelRingY) / Math.min(accelRingCounter, ACCELERATION_RING_SIZE)
        worldZ[2] = SensorFilter().sum(accelRingZ) / Math.min(accelRingCounter, ACCELERATION_RING_SIZE)

        val normalization_factor = SensorFilter().norm(worldZ)

        worldZ[0] = worldZ[0] / normalization_factor
        worldZ[1] = worldZ[1] / normalization_factor
        worldZ[2] = worldZ[2] / normalization_factor

        val currentZ = SensorFilter().dot(worldZ, currentAccel) - normalization_factor
        velRingCounter++
        velRing[velRingCounter % SPEED_RING_SIZE] = currentZ

        val speedEstimate = SensorFilter().sum(velRing)

        if (speedEstimate > STEP_THRESHOLD && oldSpeedEstimate <= STEP_THRESHOLD
            && timeNs - lastStepTimeNs > STEP_DELAY
        ) {
            listener!!.step(timeNs)
            lastStepTimeNs = timeNs
        }
        oldSpeedEstimate = speedEstimate
    }
}
