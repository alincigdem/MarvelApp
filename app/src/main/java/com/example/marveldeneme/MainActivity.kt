package com.example.marveldeneme

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marveldeneme.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val characterAdapter = CharacterAdapter(mutableListOf())
    private var currentPage = 0
    private var isLoading = false
    private val PAGE_SIZE = 30
    private val visibleThreshold = 5 // Ekranın sonundan kaç eleman önce yeni sayfayı yüklemeye başlayacağını belirler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "Activity created successfully.")

        val repository = CharacterRepository()
        val viewModelFactory = CharacterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CharacterViewModel::class.java)

        recyclerView = binding.recyclerView
        setupRecyclerView()
        loadCharacters()
        setupScrollListener()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = characterAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 5) // 5 sütunlu grid layout
        }
    }

    private fun loadCharacters() {
        isLoading = true
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val characters = viewModel.getCharacters(currentPage * PAGE_SIZE, PAGE_SIZE)
                characterAdapter.addCharacters(characters)
                currentPage++
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading characters: ${e.message}")
            }
            isLoading = false
        }
    }

    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loadCharacters()
                }
            }
        })
    }
}
