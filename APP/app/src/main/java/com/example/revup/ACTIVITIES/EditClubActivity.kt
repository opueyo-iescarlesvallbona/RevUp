package com.example.revup.ACTIVITIES

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityEditClubBinding

class EditClubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditClubBinding
    var apiRevUp = RevupCrudAPI()
    var club: Club? = null
    var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditClubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.editClubActivityPicture.setImageURI(selectedImageUri)
            }else{
                binding.editClubActivityPicture.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            }
        }

        binding.editClubActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editClubActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editClubActivitySaveButton.setOnClickListener {
            //
        }

        club = curr_club

        if(club != null){
            if(club!!.picture != null || club!!.picture != ""){
                Glide.with(this).load(image_path+club!!.picture).into(binding.editClubActivityPicture)
            }
            binding.editClubActivityName.setText(club!!.name)
            binding.editClubActivityDescription.setText(club!!.description)
        }
    }
}