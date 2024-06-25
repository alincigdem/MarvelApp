package com.example.marveldeneme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
/*hellow*/
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    val api: MarvelApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }
}
