package com.example.marveldeneme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marveldeneme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the CharacterListFragment when the activity is created
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CharacterListFragment())
                .commit()
        }
    }



}
