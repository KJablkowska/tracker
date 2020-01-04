package com.example.tracker


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tracker.listener.StepListener
import com.example.tracker.msc.StepDetector
import kotlinx.android.synthetic.main.activity_steps.*


class Steps : AppCompatActivity(), SensorEventListener, StepListener {


    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private val TEXT_STEPS = ""
    companion object {
        var numSteps: Int = 0
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccelerometer(event.timestamp, event.values[0], event.values[1], event.values[2])
        }
    }

    override fun step(timeNs: Long) {
        numSteps++
        Steps.text = TEXT_STEPS.plus(numSteps)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)

        btnStart.setOnClickListener(View.OnClickListener {
            numSteps = 0
            sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
        })

        btnStop.setOnClickListener(View.OnClickListener {
            sensorManager!!.unregisterListener(this)
        })

        btnHeart.setOnClickListener {
            val intent = Intent(this, HeartBPM::class.java)

            startActivity(intent)
        }
    }
}

