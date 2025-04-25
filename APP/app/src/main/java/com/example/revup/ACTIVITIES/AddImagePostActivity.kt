package com.example.revup.ACTIVITIES

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.example.revup.R
import com.example.revup.databinding.ActivityAddImagePostBinding


class AddImagePostActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddImagePostBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddImagePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.addImagePostActivityPreviewImage.setImageURI(selectedImageUri)
            } else {
                selectedImageUri = null
            }
        }

        binding.addImagePostActivityBtnCancel.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.addImagePostActivityBtnSave.setOnClickListener{
            // Save post
        }

        binding.addImagePostActivityBtnImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    private fun getPathFromUri(uri: Uri?): String? {
        if (uri == null) return null

        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndex("_data")
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }

        return null // Si no se encuentra la ruta, devolver null
    }
}