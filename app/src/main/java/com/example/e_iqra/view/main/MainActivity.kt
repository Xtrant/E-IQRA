package com.example.e_iqra.view.main

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Remove any existing logo
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Create an ImageView for the logo
        val logo = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.logo_width),
                resources.getDimensionPixelSize(R.dimen.logo_height)
            )
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.app_logo))
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        // Add the ImageView to the Toolbar
        toolbar.addView(logo)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
