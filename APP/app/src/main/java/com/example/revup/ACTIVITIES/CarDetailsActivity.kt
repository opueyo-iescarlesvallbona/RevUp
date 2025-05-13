package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityCarDetailsBinding

class CarDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCarDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.carDetailsActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.carDetailsActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.carDetailsActivityEditButton.setOnClickListener {
            if(binding.carDetailsActivityEditButton.text == "Edit Car"){
                binding.carDetailsActivityEditButton.text = "Save Car"
                binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.green))
                disableWidgets(true)
            }else{
                binding.carDetailsActivityEditButton.text = "Edit Car"
                binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
                disableWidgets(false)
            }
        }

        val editable = intent.getBooleanExtra("editable", false)
        disableWidgets(editable)

        val car = curr_car

        if(car != null){
            Glide.with(this).load(image_path+car.picture).into(binding.carDetailsActivityPicture)
            binding.carDetailsActivityBrand.setText(car.model!!.brand!!.name)
            binding.carDetailsActivityModel.setText(car.model!!.modelName)
            binding.carDetailsActivityYear.setText(car.modelYear.toString())
            binding.carDetailsActivityPower.setText(car.horsePower.toString() + " hp")
            binding.carDetailsActivityDescript.setText(car.description)

            if(car.memberId == current_user!!.id){
                binding.carDetailsActivityEditButton.visibility = View.VISIBLE
            }else{
                binding.carDetailsActivityEditButton.visibility = View.INVISIBLE
            }
        }

    }

    fun disableWidgets(set: Boolean = false){
        binding.carDetailsActivityPicture.isEnabled = set
        binding.carDetailsActivityBrand.isEnabled = set
        binding.carDetailsActivityModel.isEnabled = set
        binding.carDetailsActivityYear.isEnabled = set
        binding.carDetailsActivityPower.isEnabled = set
        binding.carDetailsActivityDescript.isEnabled = set
    }
}