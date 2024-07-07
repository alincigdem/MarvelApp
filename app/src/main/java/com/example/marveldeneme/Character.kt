package com.example.marveldeneme

import android.os.Parcel
import android.os.Parcelable


data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
) : Parcelable {
    var fullImageUrl: String
        get() = "${thumbnail.path.replace("http://", "https://")}/portrait_xlarge.${thumbnail.extension}"
        set(value) {
            // Setter gerekirse buraya kod ekleyebilirsiniz
        }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Thumbnail::class.java.classLoader) ?: Thumbnail("", "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(thumbnail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }
}


data class Thumbnail(
    val path: String,
    val extension: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(extension)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thumbnail> {
        override fun createFromParcel(parcel: Parcel): Thumbnail {
            return Thumbnail(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnail?> {
            return arrayOfNulls(size)
        }
    }
}
data class CharacterResponse(val data: CharacterData)

data class CharacterData(val results: List<Character>)