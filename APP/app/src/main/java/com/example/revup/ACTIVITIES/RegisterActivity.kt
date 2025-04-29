package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup.databinding.ActivityRegisterBinding
import java.util.Date
import androidx.core.content.edit

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

        binding.registerActivityBtnLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        val passwordRegex = Regex("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{13,}")
        binding.registerActivityBtnregister.setOnClickListener {
            val password = binding.registerActivityPasswordTextField.text.toString()
            val memberName = binding.registerActivityUsernameTextField.text.toString()
            if(!password.matches(passwordRegex)){
                //ERROR
            }else{
                val memberExists = apiRevUp.getMemberExist(memberName, applicationContext)

                if(memberExists!!){
                    //ERROR
                }else{
                    val member = Member(name = memberName, memberName = memberName, genderId = 1, locationId = 1, email = "email", dateOfBirth = "2020-02-02", loginDate = "2020-02-02", password = password)
                    val afegit = apiRevUp.postMember(member, applicationContext)

                    if(afegit){
                        val token = apiRevUp.login(member.memberName.toString(), member.password.toString(), applicationContext)

                        if(token==null||token==""){
                            //ERROR
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
}