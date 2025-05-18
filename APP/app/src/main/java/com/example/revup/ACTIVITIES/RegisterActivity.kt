package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup.databinding.ActivityRegisterBinding
import java.util.Date
import androidx.core.content.edit
import com.example.practicamapes_oscarpueyocasas.API.MunicipalitiesCrudAPI
import com.example.revup._DATACLASS.Gender
import com.example.revup._DATACLASS.MemberLocation
import com.example.revup._DATACLASS.Municipality
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import kotlin.collections.listOf

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    val apiRevUp = RevupCrudAPI()
    var apiMunicipality = MunicipalitiesCrudAPI()
    var locationId: Int? = null
    var genderId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameTextFieldGender: AutoCompleteTextView = binding.registerActivityGenderTextField
        val nameTextFieldLocation: AutoCompleteTextView = binding.registerActivityLocationTextField

        var genders = listOf<Gender>()
        try{
            genders = apiRevUp.getAllGenders(applicationContext)!!.toList()
            ArrayAdapter(this, android.R.layout.simple_list_item_1, genders.map { it.name }).also { adapter ->
                nameTextFieldGender.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting genders: $e", Toast.LENGTH_LONG).show()
        }

        var locations = listOf<Municipality>()
        try{
            locations = apiMunicipality.getMunicipisByName("")!!
            ArrayAdapter(this, android.R.layout.simple_list_item_1, locations.map { it.nompoblacio }).also { adapter ->
                nameTextFieldLocation.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting locations: $e", Toast.LENGTH_LONG).show()
        }

        binding.registerActivityLayoutDateofBirthTextField.setEndIconOnClickListener {
            val constraints = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val startDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select birth date")
                .setCalendarConstraints(constraints)
                .build()

            startDatePicker.show(supportFragmentManager, "START_DATE_PICKER")

            startDatePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.format(selection)
                binding.registerActivityDateofbirthTextField.setText(date)
            }
        }

        binding.registerActivityBtnLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }


        binding.registerActivityBtnregister.setOnClickListener {
            val password = binding.registerActivityPasswordTextField.text.toString()
            val memberName = binding.registerActivityUsernameTextField.text.toString()
            val email = binding.registerActivityEmailTextField.text.toString()
            val name = binding.registerActivityNameTextField.text.toString()
            val experience = binding.registerActivityExperienceTextField.text.toString()
            val dateOfBirth = binding.registerActivityDateofbirthTextField.text.toString()
            val gender = binding.registerActivityGenderTextField.text.toString()
            val location = binding.registerActivityLocationTextField.text.toString()

            if(!checks(memberName, password, email, name, experience, dateOfBirth, gender, location)){
                Toast.makeText(this, "Error on fields", Toast.LENGTH_LONG).show()
            }else{
                var memberExists: Boolean? = false
                try{
                    memberExists = apiRevUp.getMemberExist(memberName, applicationContext)
                }catch(e: Exception){
                    Toast.makeText(this, "Error getting member: $e", Toast.LENGTH_LONG).show()
                }


                if(memberExists==null||memberExists){
                    binding.registerActivityUsernameTextField.error = "Membername already exists"
                    Toast.makeText(this, "Membername already exists", Toast.LENGTH_SHORT).show()
                }else{
                    //Parse DateOfBirth from String to Date
                    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val date: Date = format.parse(dateOfBirth)!!

                    //Create member
                    val member = Member(name = memberName, membername = memberName, genderId = genderId!!, locationId = locationId!!, email = email,
                        dateOfBirth = format.format(date), loginDate = format.format(Date()), password = password)

                    //Add member
                    var afegit = false
                    try{
                        apiRevUp.postMember(member, null, applicationContext)
                        afegit = true
                    }catch (e: Exception){
                        Toast.makeText(this, "Error adding member: $e", Toast.LENGTH_LONG).show()
                    }

                    //Login if member created
                    if(afegit){
                        var token:String? = null
                        try{
                            token = apiRevUp.login(member.membername.toString(), member.password.toString(), applicationContext)
                        }catch (e: Exception){
                            Toast.makeText(this, "Error getting token: $e", Toast.LENGTH_LONG).show()
                        }

                        if(token==null||token==""){
                            Toast.makeText(this, "Error getting token", Toast.LENGTH_LONG).show()
                        }else{
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            sharedPreferences.edit() {
                                putString("token", token)
                                apply()
                            }
                        }

                        Toast.makeText(this, "Member created", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }





    }
    fun checks(memberName: String, password: String, email: String, name: String, experience: String, dateOfBirth: String, gender: String, location: String): Boolean{
        val passwordRegex = Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{13,}")
        var error = false
        if(memberName.isEmpty()){
            binding.registerActivityUsernameTextField.error = "Required"
            error = true
        }
        if(password.isEmpty()){
            binding.registerActivityPasswordTextField.error = "Required"
            error = true
        }else if(!password.matches(passwordRegex)) {
            binding.registerActivityPasswordTextField.error =
                "Correct Format:\n· Min 13 length\n· Min 1 capital\n· Min 1 lower\n· Min 1 digit\n· Min 1 symbol"
            error = true
        }
        if(email.isEmpty()){
            binding.registerActivityEmailTextField.error = "Required"
            error = true
        }
        if(name.isEmpty()){
            binding.registerActivityNameTextField.error = "Required"
            error = true
        }
        if(experience.isEmpty()){
            binding.registerActivityExperienceTextField.error = "Required"
            error = true
        }
        if(dateOfBirth.isEmpty()){
            binding.registerActivityDateofbirthTextField.error = "Required"
            error = true
        }else {
            try {
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date: Date = format.parse(dateOfBirth)!!
            } catch (e: Exception) {
                binding.registerActivityDateofbirthTextField.error = "Date format:\nDD-MM-YYYY"
                error = true
            }
        }
        if(gender.isEmpty()){
            binding.registerActivityEmailTextField.error = "Required"
            error = true
        }else{
            try{
                var genders = apiRevUp.getAllGenders(this)
                if(genders == null || genders.isEmpty()){
                    binding.registerActivityGenderTextField.error = "Required"
                    error = true
                }else{
                    for(genderObj in genders){
                        if(genderObj.name == gender){
                            genderId = genderObj!!.id
                        }
                    }
                    if(genderId == null){
                        binding.registerActivityGenderTextField.error = "Required"
                        error = true
                    }
                }
            }catch (e: Exception){
                binding.registerActivityGenderTextField.error = "Required"
                error = true
            }
        }
        if(location.isEmpty()){
            binding.registerActivityLocationTextField.error = "Required"
            error = true
        }else{
            try{
                val municipalities = apiMunicipality.getMunicipisByName(location)
                if(municipalities.isNullOrEmpty()){
                    binding.registerActivityLocationTextField.error = "Required"
                    error = true
                }else if(municipalities.size != 1){
                    binding.registerActivityLocationTextField.error = "Required"
                    error = true
                }else{
                    val locationApi = apiRevUp.getLocationsByMunicipality(location, this)
                    if(locationApi == null){
                        val muni = municipalities[0]
                        val memberLocation = MemberLocation(
                            id = null,
                            ccaa = muni.idprovincia.toString(),
                            municipality = muni.nompoblacio,
                            latitude = muni.latitut,
                            longitude = muni.longitut
                        )
                        val locationPost = apiRevUp.postLocation(memberLocation, this)
                        locationId = locationPost!!.id
                    }else if(locationApi.size != 1){
                        binding.registerActivityLocationTextField.error = "Required"
                        error = true
                    }else{
                        locationId = locationApi[0].id
                    }
                }
            }catch (e: Exception){
                binding.registerActivityLocationTextField.error = "Required"
                error = true
            }
        }
        if (error)
            return false
        else
            return true
    }
}