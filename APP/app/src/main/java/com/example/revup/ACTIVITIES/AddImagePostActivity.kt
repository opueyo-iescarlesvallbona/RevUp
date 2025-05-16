package com.example.revup.ACTIVITIES

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
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
import androidx.transition.Visibility
import com.example.practicamapes_oscarpueyocasas.API.MunicipalitiesCrudAPI
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.MemberLocation
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.curr_car
import com.example.revup.databinding.ActivityAddImagePostBinding
import java.time.LocalDateTime


class AddImagePostActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddImagePostBinding
    private var selectedImageUri: Uri? = null
    var apiRevUp = RevupCrudAPI()
    var apiMunicipality = MunicipalitiesCrudAPI()

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
            }else{
                binding.addImagePostActivityPreviewImage.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            }
        }

        binding.addImagePostActivityBtnCancel.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.addImagePostActivityBtnSave.setOnClickListener{
            var post = checkParams()
            if(post != null){
                try{
                    var result = apiRevUp.postPost(post, selectedImageUri, this)
                    if(result){
                        Toast.makeText(this, "Post created", Toast.LENGTH_LONG).show()
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }catch (e: Exception){
                    Toast.makeText(this, "Error posting post. ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.addImagePostActivityPreviewImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    fun checkParams(): Post? {
        var title: String? = null
        var description: String? = null
        var locationId: Int? = null

        if(selectedImageUri == null){
            Toast.makeText(this, "Please select a picture", Toast.LENGTH_LONG).show()
            return null
        }

        if(binding.addImagePostActivityTitle.text.toString() != ""){
            title = binding.addImagePostActivityTitle.text.toString()
        }else{
            Toast.makeText(this, "Please introduce a title", Toast.LENGTH_LONG).show()
            return null
        }

        if(binding.addImagePostActivityLocationTextField.text.toString() != ""){
            try{
                var municipalities = apiMunicipality.getMunicipisByName(binding.addImagePostActivityLocationTextField.text.toString())
                if(municipalities == null || municipalities.isEmpty()){
                    Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                    return null
                }else if(municipalities.size != 1){
                    Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                    return null
                }
                var location = apiRevUp.getLocationsByMunicipality(binding.addImagePostActivityLocationTextField.text.toString(), this)
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
                    //locationId = location!!.id
                }else if(location.size != 1){
                    Toast.makeText(this, "Select a valid location", Toast.LENGTH_LONG).show()
                    return null
                }else{
                    locationId = location[0].id
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error configuring location. ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        return Post(id = null, title = title, description = description, location_id = locationId, postDate = LocalDateTime.now().toString(), postType = 2)
    }
}