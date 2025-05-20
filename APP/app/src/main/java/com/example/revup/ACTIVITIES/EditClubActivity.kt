package com.example.revup.ACTIVITIES

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.current_user
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
                binding.editClubActivityPicture.scaleType = ImageView.ScaleType.CENTER_CROP
            }else{
                binding.editClubActivityPicture.setImageResource(R.drawable.baseline_add_photo_alternate_24)
                binding.editClubActivityPicture.scaleType = ImageView.ScaleType.FIT_CENTER
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

        binding.editClubActivityPicture.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.editClubActivitySaveButton.setOnClickListener {
            var club_to_save = checkParams()
            try{
                if(club == null){
                    var result = apiRevUp.postClub(club_to_save!!, selectedImageUri, this)
                    if(result != null){
                        Toast.makeText(this, "Club saved", Toast.LENGTH_LONG).show()
                        curr_club = result
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }else{
                    if(apiRevUp.putClub(club_to_save!!, selectedImageUri, this)){
                        Toast.makeText(this, "Club saved", Toast.LENGTH_LONG).show()
                        curr_club = club_to_save
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error saving club. ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        club = curr_club

        if(club != null){
            if(club!!.picture != null || club!!.picture != ""){
                Glide.with(this).load(image_path+club!!.picture).into(binding.editClubActivityPicture)
                binding.editClubActivityPicture.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            binding.editClubActivityName.setText(club!!.name)
            binding.editClubActivityDescription.setText(club!!.description)
        }
    }

    fun checkParams(): Club? {
        var image: Uri? = null
        var name: String? = null
        var description: String? = null
        var founder: Int? = null
        var creationDate: String? = null

        if(selectedImageUri == null && curr_club == null){
            Toast.makeText(this, "Please select a picture", Toast.LENGTH_LONG).show()
            return null
        }else if(binding.editClubActivityPicture.drawable == null){
            image = selectedImageUri
        }

        if(binding.editClubActivityName.text.toString() == ""){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            return null
        }else{
            name = binding.editClubActivityName.text.toString()
        }

        if(curr_club == null){
            founder = current_user!!.id
            creationDate = java.time.LocalDate.now().toString()
        }else{
            founder = club!!.founder
            creationDate = club!!.creationDate
        }


        if(image == null && curr_car != null){
            return Club(curr_club!!.id, name!!, founder!!, description, curr_club!!.picture, creationDate!!)
        }

        return Club(null, name!!, founder!!, description, null, creationDate!!)
    }
}