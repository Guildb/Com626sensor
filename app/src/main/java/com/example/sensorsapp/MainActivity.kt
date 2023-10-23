package com.example.sensorsapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), SensorEventListener {

    //Objects representing the two sensors we need (accelerometer and magnetic field)
    lateinit var accel: Sensor
    lateinit var magField: Sensor


    // Arrays to hold the current accelerometer and magnetic field sensor values
    var accelValues = FloatArray(3)
    var magFieldValues = FloatArray(3)
    var orientationMatrix  = FloatArray(16)
    var orientations = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use the sensor manager to obtain the two sensors

        //Set up the sensorEventListener to listen to changes in both sensors
        val sMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        magField = sMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

        sMgr.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
        sMgr.registerListener(this, magField, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Leave blank
    }

    override fun onSensorChanged(ev: SensorEvent) {

        // Test which sensor has been detected.
        if(ev.sensor == accel) {

            // Copy the current values into the acceleration array
            val newRawValues = ev.values.copyOf()

            for (i in accelValues.indices){
                accelValues[i] = (newRawValues[i]*0.05 + accelValues[i]*(1-0.05)).toFloat()
            }

            findViewById<TextView>(R.id.tv1).text = accelValues[0].toString()
            findViewById<TextView>(R.id.tv2).text = accelValues[1].toString()
            findViewById<TextView>(R.id.tv3).text = accelValues[2].toString()

        } else if (ev.sensor == magField) {
            val newRawValues = ev.values.copyOf()

            for (i in magFieldValues.indices){
                magFieldValues[i] = (newRawValues[i]*0.05 + magFieldValues[i]*(1-0.05)).toFloat()
            }

        }

        SensorManager.getRotationMatrix (orientationMatrix , null, accelValues, magFieldValues)
        SensorManager.getOrientation(orientationMatrix, orientations)

        findViewById<TextView>(R.id.tvm1).text = (orientations[0]*(180/Math.PI)).toString()
        findViewById<TextView>(R.id.tvm2).text = (orientations[1]*(180/Math.PI)).toString()
        findViewById<TextView>(R.id.tvm3).text = (orientations[2]*(180/Math.PI)).toString()
    }
}