package com.example.marveldeneme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.marveldeneme.databinding.FragmentCharacterDetailBinding

@Suppress("DEPRECATION")
class CharacterDetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CharacterDetailFragment", "Fragment created and view created.")

       // val character = arguments?.getParcelable<Character>("character")
        val arguments = arguments
        if (arguments != null) {
            val character = arguments.getParcelable<Character>("character")
            Log.d("CharacterDetailFragment", "arguments: $arguments")
            if (character != null) {
                Log.d("CharacterDetailFragment", "Received character: $character")
                binding.txtName.text = character.name
                binding.txtDescription.text = character.description

                // Açıklama kontrolü
                if (character.description.isNotEmpty()) {
                    binding.txtDescription.text = character.description
                } else {
                    binding.txtDescription.text = "Açıklama bulunmamaktadır."
                }

                Glide.with(requireContext())
                    .load(character.fullImageUrl)
                    .into(binding.imgCharacter)
                // Burada character nesnesini kullanabilirsiniz
            } else {
                Log.e("CharacterDetailFragment", "Character is null")
            }
        } else {
            Log.e("CharacterDetailFragment", "Arguments are null")
        }

       /* character?.let {
            Log.d("CharacterDetailFragment", "Received character: $character")
            binding.txtName.text = it.name
            binding.txtDescription.text = it.description
            Glide.with(requireContext())
                .load(it.fullImageUrl)
                .into(binding.imgCharacter)
        }*/
    }
}
