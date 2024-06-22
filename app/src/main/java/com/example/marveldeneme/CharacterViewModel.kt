// CharacterViewModel.kt
package com.example.marveldeneme
import androidx.lifecycle.ViewModel

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    suspend fun getCharacters(offset: Int, limit: Int): List<Character> {
        return repository.getCharacters(offset, limit)
    }
}