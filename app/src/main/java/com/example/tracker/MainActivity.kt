package com.example.tracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tracker.msc.StepDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Lets_go_button.setOnClickListener {
            val intent = Intent(this, Steps::class.java)

            startActivity(intent)
        }


    }
}
