import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marveldeneme.Character
import com.example.marveldeneme.CharacterRepository
import com.example.marveldeneme.Thumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterViewModel : ViewModel() {

    private val repository = CharacterRepository()

    suspend fun getCharacter(characterId: Int): Character {
        return withContext(Dispatchers.IO) {
            try {
                val characterResult = repository.getCharacter(characterId)
                Log.d("CharacterViewModel", "Character received: $characterResult")
                characterResult
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error getting character: ${e.message}")
                Character(-1, "", "", Thumbnail("", ""))
            }
        }
    }
}
