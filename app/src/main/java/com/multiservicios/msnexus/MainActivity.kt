package com.multiservicios.msnexus

import android.os.Bundle
import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.multiservicios.msnexus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Throwable) {

            val error = TextView(this).apply {
                text = "MS NEXUS - ERROR\n\n${e.javaClass.name}\n\n${e.message}\n\n${e.stackTraceToString()}"
                textSize = 14f
                setTextColor(Color.RED)
                setPadding(32, 32, 32, 32)
            }

            setContentView(error)
        }
    }
}
