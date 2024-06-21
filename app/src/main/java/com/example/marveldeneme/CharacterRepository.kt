package com.example.marveldeneme

import MarvelApi
import RetrofitInstance
import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest

class CharacterRepository {

    private val api: MarvelApi = RetrofitInstance.api
    private val publicKey = "5494e2773c8126770d8b6a47ed63fb6d"
    private val privateKey = "a1d9a7e9f77e0d42ffba2e3314f7259cd8800fd0"

    suspend fun getCharacter(characterId: Int): Character {
        val timeStamp = (System.currentTimeMillis() / 1000).toString() // Saniye cinsinden zaman damgası
        val hash = generateHash(timeStamp, privateKey, publicKey)

        Log.d("CharacterRepository", "Character ID: $characterId")
        Log.d("CharacterRepository", "Timestamp: $timeStamp")
        Log.d("CharacterRepository", "Public Key: $publicKey")
        Log.d("CharacterRepository", "Hash: $hash")

        val response = api.getCharacter(characterId, timeStamp, publicKey, hash)
        if (response.isSuccessful) {
            Log.d("CharacterRepository", "API request successful")
            val characterResponse = response.body()
            if (characterResponse != null && characterResponse.data.results.isNotEmpty()) {
                val character = characterResponse.data.results[0]
                val fullImageUrl = createFullImageUrl(character.thumbnail.path, character.thumbnail.extension)
                Log.d("CharacterRepository", "Full image URL: $fullImageUrl")
                Log.d("CharacterRepository", "Character data received: $character")
                Log.d("CharacterRepository", "Character thumbnail path: ${character.thumbnail.path}")
                Log.d("CharacterRepository", "Character thumbnail extension: ${character.thumbnail.extension}")
                return character
            } else {
                Log.e("CharacterRepository", "Empty character data or results list is empty.")
            }
        } else {
            Log.e("CharacterRepository", "API request failed with code ${response.code()}")
        }
        return Character(-1, "", "", Thumbnail("", ""))
    }

    fun createFullImageUrl(path: String, extension: String): String {
        // Belirli bir görüntü varyantı adı seçin (örneğin, "portrait_xlarge") ve varyasyon adını yol öğesine ekleyin
        val variantName = "portrait_xlarge"
        val fullImageUrl = "$path/$variantName.$extension"
        Log.d("CharacterRepository", "Creating full image URL: Path = $path, Extension = $extension, Full URL = $fullImageUrl")
        return fullImageUrl
    }

    fun generateHash(ts: String, privateKey: String, publicKey: String): String {
        val input = "$ts$privateKey$publicKey"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
