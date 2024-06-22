package com.example.marveldeneme
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CharacterAdapter(private val characters: MutableList<Character>) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun addCharacters(newCharacters: List<Character>) {
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtName)
        private val imgCharacter: ImageView = itemView.findViewById(R.id.imgCharacter)

        fun bind(character: Character) {
            txtName.text = character.name
            val fullImageUrl = character.fullImageUrl
            Log.d("CharacterAdapter", "Full Image URL: $fullImageUrl")
            Glide.with(itemView.context)
                .load(fullImageUrl)
                .override(250, 250) // İmgenin belirli bir boyutunu yükler
                .centerCrop() // İmgeyi görüntünün ortasına yerleştirir ve orantılı bir şekilde kırpar
                .into(imgCharacter)
        }
    }
}
