package com.example.marveldeneme

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest

class CharacterRepository  {

    private val api: MarvelApi = RetrofitInstance.api
    private val publicKey = "5494e2773c8126770d8b6a47ed63fb6d"
    private val privateKey = "a1d9a7e9f77e0d42ffba2e3314f7259cd8800fd0"

    suspend fun getCharacters(offset: Int, limit: Int): List<Character> {
        val timeStamp = (System.currentTimeMillis() / 1000).toString()
        val hash = generateHash(timeStamp)

        val response = api.getCharacters(timeStamp, publicKey, hash, offset, limit)
        if (response.isSuccessful) {
            val characterResponse = response.body()
            if (characterResponse != null) {
                val characters = characterResponse.data.results
                // Her karakterin resim URL'sini oluştur ve görseli olmayanları filtrele
                val filteredCharacters = characters.filter { character ->
                    val fullImageUrl = createFullImageUrl(character.thumbnail.path, character.thumbnail.extension)
                    character.fullImageUrl = fullImageUrl

                    // description ve görsel kontrolü yap ve logla
                    val hasDescription = character.description.isNotEmpty()
                    val hasImage = character.thumbnail.path.isNotEmpty() && character.thumbnail.extension.isNotEmpty()
                    Log.d("CharacterRepository", "Character: ${character.name}, Description exists: $hasDescription, Image exists: $hasImage")

                    hasImage && hasDescription
                }

                // Log filtered characters listesi
                Log.d("CharacterRepository", "Filtered Characters: $filteredCharacters")

                return filteredCharacters
            } else {
                Log.e("CharacterRepository", "Character response is null")
            }
        } else {
            // Başarısız istek durumunda logla
            Log.e("CharacterRepository", "Failed to fetch characters. Response code: ${response.code()}")
        }
        return emptyList()
    }

    private fun createFullImageUrl(path: String, extension: String): String {
        // Belirli bir görüntü varyantı adı seçin (örneğin, "portrait_xlarge") ve varyasyon adını yol öğesine ekleyin
        val variantName = "portrait_xlarge"
        val fullImageUrl = "$path/$variantName.$extension"
        Log.d(
            "CharacterRepository",
            "Creating full image URL: Path = $path, Extension = $extension, Full URL = $fullImageUrl"
        )
        return fullImageUrl
    }

    private fun generateHash(ts: String): String {
        Log.d("CharacterRepository", "Generating hash for timestamp: $ts")
        val input = "$ts$privateKey$publicKey"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
