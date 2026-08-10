package com.multiservicios.msnexus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.multiservicios.msnexus.databinding.ActivityMainBinding
import com.multiservicios.msnexus.ui.dashboard.DashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_container,
                    DashboardFragment()
                )
                .commit()
        }
    }
}
