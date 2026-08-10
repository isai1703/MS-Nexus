package com.multiservicios.msnexus

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "MS NEXUS\n\nMainActivity funcionando correctamente"
            textSize = 24f
            setTextColor(Color.BLACK)
            setPadding(48, 48, 48, 48)
        }

        setContentView(textView)
    }
}
