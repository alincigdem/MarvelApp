// CharacterViewModel.kt
package com.example.marveldeneme
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters
    private var offset = 0
    private val limit = 30

    fun loadMoreCharacters() {
        viewModelScope.launch {
            val newCharacters = repository.getCharacters(offset, limit)
            val currentCharacters = _characters.value.orEmpty().toMutableList()
            currentCharacters.addAll(newCharacters)
            _characters.value = currentCharacters
            offset += limit
        }
    }
    suspend fun getCharacters(offset: Int, limit: Int): List<Character> {
        return repository.getCharacters(offset, limit)
    }
}