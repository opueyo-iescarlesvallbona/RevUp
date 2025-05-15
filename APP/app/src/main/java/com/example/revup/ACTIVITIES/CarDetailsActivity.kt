package com.example.revup.ACTIVITIES

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Brand
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Model
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityCarDetailsBinding


class CarDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCarDetailsBinding
    val apiRevUp = RevupCrudAPI()
    private var selectedImageUri: Uri? = null
    private var selectedBrand: Brand? = null
    private var selectedModel: Model? = null

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

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.carDetailsActivityPicture.setImageURI(selectedImageUri)
            }else{
                binding.carDetailsActivityPicture.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            }
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

        binding.carDetailsActivityPicture.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.carDetailsActivityEditButton.setOnClickListener {
            if(binding.carDetailsActivityEditButton.text == "Edit Car"){
                binding.carDetailsActivityEditButton.text = "Save Car"
                binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.green))
                disableWidgets(true)
            }else{
                var car = checkParams()
                if(car != null){
                    try{
                        if(curr_car == null){
                            var result = apiRevUp.postCar(car, selectedImageUri, applicationContext)
                            if(result){
                                Toast.makeText(this, "Car saved", Toast.LENGTH_LONG).show()
                                binding.carDetailsActivityEditButton.text = "Edit Car"
                                binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
                                disableWidgets(false)
                            }else{
                                Toast.makeText(this, "Error saving car", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            car.id = curr_car!!.id
                            var result = apiRevUp.putCar(car, selectedImageUri, applicationContext)
                            if(result){
                                Toast.makeText(this, "Car saved", Toast.LENGTH_LONG).show()
                                binding.carDetailsActivityEditButton.text = "Edit Car"
                                binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
                                disableWidgets(false)
                            }else{
                                Toast.makeText(this, "Error saving car", Toast.LENGTH_LONG).show()
                            }
                        }
                    }catch (e: Exception){
                        Toast.makeText(this, "Error saving car. ${e.message}", Toast.LENGTH_LONG).show()
                        Log.i("ERROR CAR SAVE", e.message.toString())
                    }
                }
            }
        }

        val nameTextFieldBrand: AutoCompleteTextView = binding.carDetailsActivityBrandTextField
        val nameTextFieldModel: AutoCompleteTextView = binding.carDetailsActivityModelTextField

        var brands = listOf<Brand>()
        try{
            brands = apiRevUp.getBrands(applicationContext)!!.toList()
            ArrayAdapter(this, android.R.layout.simple_list_item_1, brands.map { it.name }).also { adapter ->
                nameTextFieldBrand.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting brands: $e", Toast.LENGTH_LONG).show()
        }

        nameTextFieldBrand.setOnItemClickListener{ parent, view, position, id ->
            val selectedBrandName = parent.getItemAtPosition(position) as String
            selectedBrand = brands.firstOrNull { it.name == selectedBrandName }

            selectedBrand?.let { brand ->
                try {
                    val models = apiRevUp.getModelsByBrandId(brand.id, applicationContext) ?: emptyList()
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, models.map { it.modelName }).also { adapter ->
                        nameTextFieldModel.setAdapter(adapter)
                    }
                    nameTextFieldModel.setOnItemClickListener{parent, view, position, id ->
                        val selectedModelName = parent.getItemAtPosition(position) as String
                        selectedModel = models.firstOrNull { it.modelName == selectedModelName }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error getting models: $e", Toast.LENGTH_LONG).show()
                }
            }
            nameTextFieldModel.setText("")
        }

        val editable = intent.getBooleanExtra("editable", false)
        disableWidgets(editable)

        if(editable){
            binding.carDetailsActivityEditButton.text = "Save Car"
            binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.green))
        }else{
            binding.carDetailsActivityEditButton.text = "Edit Car"
            binding.carDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
        }

        val car = curr_car

        if(car != null){
            Glide.with(this).load(image_path+car.picture).into(binding.carDetailsActivityPicture)
            binding.carDetailsActivityBrandTextField.setText(car.model!!.brand!!.name)
            binding.carDetailsActivityModelTextField.setText(car.model!!.modelName)
            binding.carDetailsActivityYear.setText(car.modelYear.toString())
            binding.carDetailsActivityPower.setText(car.horsePower.toString())
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
        binding.carDetailsActivityLayoutBrandTextField.isEnabled = set
        binding.carDetailsActivityLayoutModelTextField.isEnabled = set
        binding.carDetailsActivityYear.isEnabled = set
        binding.carDetailsActivityPower.isEnabled = set
        binding.carDetailsActivityDescript.isEnabled = set
    }

    fun checkParams(): Car? {
        var image: Uri? = null
        var image_path: String? = null
        var model: Model? = null
        var hp: Double? = null
        var year: Int? = null
        var description: String? = null
        if(selectedImageUri == null && binding.carDetailsActivityPicture.drawable == null){
            Toast.makeText(this, "Please select a picture", Toast.LENGTH_LONG).show()
            return null
        }else if(binding.carDetailsActivityPicture.drawable == null){
            image = selectedImageUri
        }
        if(binding.carDetailsActivityBrandTextField.text.toString() == ""){
            Toast.makeText(this, "Please select a brand", Toast.LENGTH_LONG).show()
            return null
        }
        if(binding.carDetailsActivityModelTextField.text.toString() == ""){
            Toast.makeText(this, "Please select a model", Toast.LENGTH_LONG).show()
            return null
        }
        try{
            var brand_model_check: Boolean = apiRevUp.getBrandModelCheck(
                binding.carDetailsActivityBrandTextField.text.toString(),
                binding.carDetailsActivityModelTextField.text.toString(), this) == true
            if(!brand_model_check){
                return null
            }
            var models = apiRevUp.getModels(this)
            if(models != null){
                for(mod in models){
                    if(mod.modelName == binding.carDetailsActivityModelTextField.text.toString()){
                        model = mod
                    }
                }
            }
        }catch(e: Exception){
            Toast.makeText(this, "Error getting brand model check", Toast.LENGTH_LONG).show()
            return null
        }
        try {
            hp = binding.carDetailsActivityPower.text.toString().toDouble()
        }catch (e: Exception){
            Toast.makeText(this, "Value not valid", Toast.LENGTH_LONG).show()
            return null
        }
        try{
            year = binding.carDetailsActivityYear.text.toString().toInt()
        }catch (e: Exception){
            Toast.makeText(this, "Value not valid", Toast.LENGTH_LONG).show()
            return null
        }

        if(image == null && curr_car != null){
            return Car(null, current_user!!.id, model!!.id, year, hp, binding.carDetailsActivityDescript.text.toString(), curr_car!!.picture, null, null)
        }

        return Car(null, current_user!!.id, model!!.id, year, hp, binding.carDetailsActivityDescript.text.toString())
    }
}