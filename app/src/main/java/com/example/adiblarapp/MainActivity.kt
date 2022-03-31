package com.example.adiblarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.adiblarapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var onBackPressedListener: OnBackPressedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener?) {
        this.onBackPressedListener = onBackPressedListener
    }

    override fun onBackPressed() {
        onBackPressedListener?.onBackPressed()
        super.onBackPressed()
    }

    interface OnBackPressedListener {
        fun onBackPressed()
    }
}