package com.example.revup.ACTIVITIES

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    val REQUEST_LOCATION_CODE = 100
    var havePermission: Boolean = false

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
        CheckPermissions()
        val prefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val autoMemberName = prefs.getString("membername", null)
        val autoPassword = prefs.getString("password", null)
        if(autoMemberName!=null){
            try{
                val token = apiRevUp.login(autoMemberName.toString(), autoPassword.toString(), applicationContext)
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                sharedPreferences.edit() {
                    putString("token", token)
                    apply()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error getting token: $e", Toast.LENGTH_LONG).show()
            }

            try{
                val member = apiRevUp.getMemberByMemberName(autoMemberName, this)
                if(member!=null){
                    current_user = member
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
            if(!binding.logInActivityUsernameTextField.text.isNullOrEmpty()&&
                !binding.logInActivityPasswordTextField.text.isNullOrEmpty()){

                val password = binding.logInActivityPasswordTextField.text
                val memberName = binding.logInActivityUsernameTextField.text

                val userExists = apiRevUp.getMemberExist(memberName.toString(), applicationContext)
                if(!userExists!!){
                    Toast.makeText(this, "The member doesn't exists", Toast.LENGTH_LONG).show()
                }else{
                    var token = ""
                    try{
                        token = apiRevUp.login(memberName.toString(), password.toString(), applicationContext).toString()
                    }catch(e: Exception){
                        Toast.makeText(this, "Error getting token: $e", Toast.LENGTH_LONG).show()
                    }

                    if(token==""){
                        Toast.makeText(this, "Error getting token", Toast.LENGTH_LONG).show()
                    }else{
                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        sharedPreferences.edit() {
                            putString("token", token)
                            putString("membername", memberName.toString())
                            putString("password", password.toString())
                            apply()
                        }
                        val member = apiRevUp.getMemberByMemberName(memberName.toString(), this)
                        current_user = member
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

    fun CheckPermissions(){
        if (
            (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            &&
            (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            &&
            (ContextCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED)
        ){
            havePermission = true
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.FOREGROUND_SERVICE))
                havePermission = false
            else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE), REQUEST_LOCATION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                havePermission = true
            }
            else
                havePermission = false
        }
    }
}

