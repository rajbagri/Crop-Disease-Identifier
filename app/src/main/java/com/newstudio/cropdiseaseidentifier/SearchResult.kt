package com.newstudio.cropdiseaseidentifier

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.newstudio.cropdiseaseidentifier.databinding.ActivitySearchResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResult : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        val plantDis = intent.getStringExtra("plantDisease")
        val byteArray = intent.getByteArrayExtra("plantImage")
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        binding.apply {
            textViewDiseaseName.text = plantDis
            imageViewCrop.setImageBitmap(bitmap)
        }

        // Request data asynchronously and update UI when results are ready
        geminiAnswer("give very short info about $plantDis this crop disease and don't use * * in sentences ") { answer ->
            binding.textViewDiseaseInfo.text = answer
        }

        geminiAnswer("point out the symptoms of the disease $plantDis short points using '' and use 1, 2, for the points and headings with proper spacing and in short" ) { answer ->
            binding.textViewDiseaseSymptomsInfo.text = answer
        }

        geminiAnswer("give the treatment of the crop disease $plantDis short points in short and use 1, 2, for the points and headings with proper spacing and in short") { answer ->
            binding.textViewDiseaseTreatmentInfo.text = answer
        }

    }

    // Function to request information and return it via a callback
    private fun geminiAnswer(prompt: String, callback: (String) -> Unit) {
        val apiKey = "AIzaSyCkX5pZ2XRy3wGSeWU5E8a6hixCuGs66xE" // Securely load the API key
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generativeModel.generateContent(prompt)
                withContext(Dispatchers.Main) {
                    callback(response.text.toString()) // Invoke callback when response is received
                }
                Log.d("Response", response.toString())
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Exception", "Error during request: ${e.message}")
                    Toast.makeText(this@SearchResult, "Error fetching data. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
