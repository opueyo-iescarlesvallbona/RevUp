package com.example.revup.ACTIVITIES

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityLogInBinding


class LogInActivity : AppCompatActivity() {
    lateinit var binding : ActivityLogInBinding
    val apiRevUp = RevupCrudAPI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val autoMemberName = prefs.getString("membername", null)
        val autoPassword = prefs.getString("password", null)
        if(autoMemberName!=null){
            val token = apiRevUp.login(autoMemberName.toString(), autoPassword.toString(), applicationContext)
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            sharedPreferences.edit() {
                putString("token", token)
                apply()
            }
            try{
                val member = apiRevUp.getMemberByMemberName(autoMemberName, this)
                if(member!=null){
                    current_user = member
                    Log.i("member", current_user.toString())
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Member doesn't exists", Toast.LENGTH_LONG).show()
                }
            }catch(e: Exception){
                Toast.makeText(this, "Error getting member: $e", Toast.LENGTH_LONG).show()
            }
        }


        binding.logInActivityBtnLogIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            if(!binding.logInActivityUsernameTextField.text.isNullOrEmpty()&&
                !binding.logInActivityPasswordTextField.text.isNullOrEmpty()){

                val password = binding.logInActivityPasswordTextField.text
                val memberName = binding.logInActivityUsernameTextField.text

                val userExists = apiRevUp.getMemberExist(memberName.toString(), applicationContext)
                if(!userExists!!){
                    //ERROR
                }else{
                    val token = apiRevUp.login(memberName.toString(), password.toString(), applicationContext)
                    if(token==null||token==""){
                        //ERROR
                    }else{
                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        sharedPreferences.edit() {
                            putString("token", token)
                            putString("membername", memberName.toString())
                            putString("password", password.toString())
                            apply()
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        binding.logInActivityBtnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}

