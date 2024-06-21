package com.example.marveldeneme

import CharacterViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.marveldeneme.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var binding: ActivityMainBinding
    private val characterId = 1009610 // Karakter kimliği

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)

        // Karakter bilgilerini al ve göster
        fetchAndDisplayCharacter(characterId)
    }

    private fun fetchAndDisplayCharacter(characterId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val character = viewModel.getCharacter(characterId)
                Log.d("MainActivity", "Character data received: $character")
                character?.let { displayCharacter(it) }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching character: ${e.message}")
            }
        }
    }


    private fun displayCharacter(character: Character) {
        binding.apply {
            txtName.text = character.name
            txtDescription.text = character.description
        }

        // Karakter resmini yükle
        val fullImageUrl = character.fullImageUrl
        Log.d("MainActivity", "Full image URL: $fullImageUrl")

        Glide.with(this@MainActivity)
            .load(fullImageUrl)
            .override(0, 0) // Resmin orijinal boyutunu kullan
            .centerCrop() // Resmi ImageView boyutlarına göre orantılı bir şekilde kırp
            .into(binding.imgCharacter)

        Log.d("MainActivity", "Character displayed: $character")
    }
}
