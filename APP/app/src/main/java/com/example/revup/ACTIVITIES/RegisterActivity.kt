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
import com.example.revup._DATACLASS.Gender
import com.example.revup._DATACLASS.MemberLocation
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import kotlin.collections.listOf

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    val apiRevUp = RevupCrudAPI()
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

        val nameTextField: AutoCompleteTextView = binding.registerActivityGenderTextField


        var genders = listOf<Gender>()
        try{
            genders = apiRevUp.getAllGenders(applicationContext)!!.toList()
            ArrayAdapter(this, android.R.layout.simple_list_item_1, genders.map { it.name }).also { adapter ->
                nameTextField.setAdapter(adapter)
            }
        }catch (e: Exception){}

        var locations = listOf<MemberLocation>()



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

            if(!checks(memberName, password, email, name, experience, dateOfBirth)){
                //ERROR
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
                    val member = Member(name = memberName, memberName = memberName, genderId = 1, locationId = 1, email = email,
                        dateOfBirth = format.format(date), loginDate = format.format(Date()), password = password)

                    //Add member
                    var afegit = false
                    try{
                        apiRevUp.postMember(member, applicationContext)
                        afegit = true
                    }catch (e: Exception){}

                    //Login if member created
                    if(afegit){
                        var token:String? = null
                        try{
                            token = apiRevUp.login(member.memberName.toString(), member.password.toString(), applicationContext)
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
    fun checks(memberName: String, password: String, email: String, name: String, experience: String, dateOfBirth: String): Boolean{
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
        if (error)
            return false
        else
            return true
    }
}