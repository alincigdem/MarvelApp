package com.example.marveldeneme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marveldeneme.databinding.FragmentCharacterListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterListFragment : Fragment() {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var binding: FragmentCharacterListBinding
    private lateinit var recyclerView: RecyclerView
    private val characterAdapter = CharacterAdapter(mutableListOf()) { character ->
        navigateToCharacterDetail(character)
       /* val action = CharacterListFragmentDirections
            .actionCharacterListFragmentToCharacterDetailFragment()
        findNavController().navigate(action)
        Log.d("CharacterListFragment", "Navigating to CharacterDetailFragment with character: $character")*/

    }
    private var currentPage = 0
    private var isLoading = false
    private val PAGE_SIZE = 30
    private val visibleThreshold = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterListBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CharacterListFragment", "Fragment created successfully.")

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
            layoutManager = GridLayoutManager(requireContext(), 5)
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
                Log.e("CharacterListFragment", "Error loading characters: ${e.message}")
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

    private fun navigateToCharacterDetail(character: Character) {
        // Bundle oluşturup karakter nesnesini içine yerleştirin
        val bundle = Bundle().apply {
            putParcelable("character", character)
        }

        // CharacterDetailFragment oluşturun ve arguments olarak bundle'ı atayın
        val fragment = CharacterDetailFragment()
        fragment.arguments = bundle

        // Fragment geçişini başlatın
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}
