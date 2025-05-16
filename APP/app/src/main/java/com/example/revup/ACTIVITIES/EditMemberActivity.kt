package com.example.revup.ACTIVITIES

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.practicamapes_oscarpueyocasas.API.MunicipalitiesCrudAPI
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Brand
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Gender
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.MemberLocation
import com.example.revup._DATACLASS.Model
import com.example.revup._DATACLASS.Municipality
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityEditMemberBinding

class EditMemberActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditMemberBinding
    var apiRevUp = RevupCrudAPI()
    var apiMunicipality = MunicipalitiesCrudAPI()
    var selectedImageUri: Uri? = null
    var member: Member? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.editMemberActivityPicture.setImageURI(selectedImageUri)
            }else{
                binding.editMemberActivityPicture.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            }
        }

        binding.editMemberActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editMemberActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editMemberActivitySaveButton.setOnClickListener {
            var member_to_save = checkParams()
            if(member_to_save != null){
                if(apiRevUp.putMember(member_to_save, selectedImageUri, this)){
                    Toast.makeText(this, "Member updated", Toast.LENGTH_LONG).show()
                    current_user = member_to_save
                    curr_member = member_to_save
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            }
        }

        binding.editMemberActivityPicture.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        val nameTextFieldLocation: AutoCompleteTextView = binding.editMemberActivityLocationTextField
        val nameTextFieldGender: AutoCompleteTextView = binding.editMemberActivityGenderTextField

        var locations = listOf<Municipality>()
        try{
            locations = apiMunicipality.getMunicipisByName("")!!
            ArrayAdapter(this, android.R.layout.simple_list_item_1, locations.map { it.nompoblacio }).also { adapter ->
                nameTextFieldLocation.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting locations: $e", Toast.LENGTH_LONG).show()
        }

        var genders = listOf<Gender>()
        try{
            genders = apiRevUp.getAllGenders(this)!!
            ArrayAdapter(this, android.R.layout.simple_list_item_1, genders.map { it.name }).also { adapter ->
                nameTextFieldGender.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting genders: $e", Toast.LENGTH_LONG).show()
        }

        member = current_user

        if(member != null){
            Glide.with(this).load(image_path+member!!.profilePicture).into(binding.editMemberActivityPicture)
            binding.editMemberActivityMemberName.setText(member!!.membername)
            binding.editMemberActivityName.setText(member!!.name)
            binding.editMemberActivityEmail.setText(member!!.email)
            if(member!!.description != null && member!!.description != ""){
                binding.editMemberActivityDescript.setText(member!!.description)
            }else{
                binding.editMemberActivityDescript.visibility = View.GONE
            }
            try{
                var location = apiRevUp.getLocationById(member!!.locationId, this)
                if(location != null){
                    binding.editMemberActivityLocationTextField.setText(location.municipality)
                }
                var gender = apiRevUp.getGenderById(member!!.genderId, this)
                if(gender != null){
                    binding.editMemberActivityGenderTextField.setText(gender.name)
                }
            }catch(e: Exception){
                Toast.makeText(this, "Error getting location or gender. ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun checkParams(): Member? {
        var image: Uri? = null
        var name: String? = null
        var email: String? = null
        var description: String? = null
        var locationId: Int? = null
        var genderId: Int? = null

        if(selectedImageUri == null && curr_member == null){
            Toast.makeText(this, "Please select a picture", Toast.LENGTH_LONG).show()
            return null
        }else if(binding.editMemberActivityPicture.drawable == null){
            image = selectedImageUri
        }

        if(binding.editMemberActivityName.text.toString() != member!!.name){
            name = binding.editMemberActivityName.text.toString()
        }else{
            name = member!!.name
        }

        if(binding.editMemberActivityEmail.text.toString() != member!!.email){
            email = binding.editMemberActivityEmail.text.toString()
        }else{
            email = member!!.email
        }

        if(binding.editMemberActivityDescript.text.toString() != member!!.description){
            description = binding.editMemberActivityDescript.text.toString()
        }else{
            description = member!!.description
        }

        try{
            var municipalities = apiMunicipality.getMunicipisByName(binding.editMemberActivityLocationTextField.text.toString())
            if(municipalities == null || municipalities.isEmpty()){
                Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                return null
            }else if(municipalities.size != 1){
                Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                return null
            }
            var location = apiRevUp.getLocationsByMunicipality(binding.editMemberActivityLocationTextField.text.toString(), this)
            if(location == null){
                var muni = municipalities[0]
                var memberLocation = MemberLocation(
                    id = null,
                    ccaa = muni.idprovincia.toString(),
                    municipality = muni.nompoblacio,
                    latitude = muni.latitut,
                    longitude = muni.longitut
                )
                var location = apiRevUp.postLocation(memberLocation, this)
                locationId = location!!.id
            }else if(location.size != 1){
                Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                return null
            }else{
                locationId = location[0].id
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error configuring location. ${e.message}", Toast.LENGTH_LONG).show()
        }

        try{
            var genders = apiRevUp.getAllGenders(this)
            if(genders == null || genders.isEmpty()){
                Toast.makeText(this, "Error getting genders", Toast.LENGTH_LONG).show()
                return null
            }
            for(gender in genders){
                if(gender.name == binding.editMemberActivityGenderTextField.text.toString()){
                    genderId = gender!!.id
                }
            }
            if(genderId == null){
                Toast.makeText(this, "Select a valid gender", Toast.LENGTH_LONG).show()
                return null
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting genders. ${e.message}", Toast.LENGTH_LONG).show()
        }

        if(image == null && curr_member != null){
            return Member(member!!.id, name, member!!.membername, null, email, genderId!!,
                locationId!!, member!!.dateOfBirth, member!!.loginDate, description, member!!.profilePicture, member!!.password)
        }

        return Member(member!!.id, name, member!!.membername, null, email, genderId!!,
            locationId!!, member!!.dateOfBirth, member!!.loginDate, description, null, member!!.password)
    }
}
