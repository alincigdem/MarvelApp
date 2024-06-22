package com.example.marveldeneme


data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
) {
    var fullImageUrl: String
        get() = "${
            thumbnail.path.replace(
                "http://",
                "https://"
            )
        }/portrait_xlarge.${thumbnail.extension}"
        set(value) {
            // Setter gerekirse buraya kod ekleyebilirsiniz
        }
}


    data class Thumbnail(
        val path: String,
        val extension: String
    )

    data class CharacterResponse(val data: CharacterData)

    data class CharacterData(val results: List<Character>)
