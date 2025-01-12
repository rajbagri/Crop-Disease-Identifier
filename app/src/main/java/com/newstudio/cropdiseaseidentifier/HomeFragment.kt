package com.newstudio.cropdiseaseidentifier

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.newstudio.cropdiseaseidentifier.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            imageViewScan.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if(intent.resolveActivity(requireContext().packageManager) != null){
                    startActivityForResult(intent, 1)
                }
                else{
                    Toast.makeText(requireContext(), "There is something wrong with camera", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            val image = data?.extras?.get("data")
            val bitmap = image as Bitmap
            geminiAnswer(bitmap)
        }
    }


    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    private fun geminiAnswer(rotatedBitmap: Bitmap) {
        // Call the API

        val apiKey = "AIzaSyCkX5pZ2XRy3wGSeWU5E8a6hixCuGs66xE"

        val generativeModel = GenerativeModel(
            "gemini-1.5-pro",
            apiKey
        )

        CoroutineScope(Dispatchers.IO).launch {

            try{
                val response = generativeModel.generateContent(content {
                    text("Identify the disease based on the image of  the photo only disease name nothing else")
                    image(rotatedBitmap)
                })
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(requireContext(), SearchResult::class.java)
                    intent.putExtra("plantDisease", response.text.toString())
                    val stream = ByteArrayOutputStream()
                    rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra("plantImage", byteArray)
                    startActivity(intent)
                }

                Log.d("Response", response.toString())
            }
            catch (e: Exception){
                Log.e("Exception", "Error during request: ${e.message}")
            }


        }

    }

}