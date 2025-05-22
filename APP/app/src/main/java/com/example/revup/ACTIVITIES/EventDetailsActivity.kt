package com.example.revup.ACTIVITIES

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Brand
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.ClubEvent
import com.example.revup._DATACLASS.EventState
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.curr_event
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityEventDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityEventDetailsBinding
    val apiRevup = RevupCrudAPI()
    var mMap: GoogleMap? = null
    val event = curr_event
    var location: LatLng? = if(curr_event!=null) LatLng(curr_event!!.lat, curr_event!!.long) else null
    private var selectedImageUri: Uri? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editable = intent.getBooleanExtra("editable", false)
        disableWidgets(editable)



        val nameTextFieldClub: AutoCompleteTextView = binding.eventDetailsActivityClub
        val nameTextFieldState: AutoCompleteTextView = binding.eventDetailsActivityState

        var clubs: MutableList<Club>? = mutableListOf<Club>()
        try{
            clubs = apiRevup.getClubsByMember(current_user!!.id!!, this)
            val clubsWithPermission: MutableList<Club> = mutableListOf()
            for (c: Club in clubs!!){
                if(apiRevup.checkIfPermission(current_user!!.id!!, c.id!!, this)==true){
                    clubsWithPermission.add(c)
                }
            }

            ArrayAdapter(this, android.R.layout.simple_list_item_1, clubsWithPermission.map { it.name }).also { adapter ->
                nameTextFieldClub.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting clubs: $e", Toast.LENGTH_LONG).show()
        }


        var states: MutableList<EventState>? = mutableListOf<EventState>()
        try{
            states = apiRevup.getEventStates(this)

            ArrayAdapter(this, android.R.layout.simple_list_item_1, states!!.map { it.name }).also { adapter ->
                nameTextFieldState.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting states: $e", Toast.LENGTH_LONG).show()
        }

        //DATES
        binding.eventDetailsActivityEnddate.isFocusable = false
        binding.eventDetailsActivityStartdate.isFocusable = false

        var selectedStartDate: Long? = null

        binding.eventDetailsActivityLayoutstartdate.setEndIconOnClickListener {
            val constraints = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val startDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select start date")
                .setCalendarConstraints(constraints)
                .build()

            startDatePicker.show(supportFragmentManager, "START_DATE_PICKER")

            startDatePicker.addOnPositiveButtonClickListener { selection ->
                selectedStartDate = selection
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.format(selection)
                binding.eventDetailsActivityStartdate.setText(date)
            }
        }

        binding.eventDetailsActivityLayoutenddate.setEndIconOnClickListener {
            if (selectedStartDate == null) {
                Toast.makeText(this, "Please select a start date first", Toast.LENGTH_SHORT).show()
                return@setEndIconOnClickListener
            }

            val constraints = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        return date > selectedStartDate!!
                    }

                    override fun describeContents(): Int = 0
                    override fun writeToParcel(dest: Parcel, flags: Int) {
                        TODO("Not yet implemented")
                    }
                }).build()

            val endDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select end date")
                .setCalendarConstraints(constraints)
                .build()

            endDatePicker.show(supportFragmentManager, "END_DATE_PICKER")

            endDatePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.format(selection)
                binding.eventDetailsActivityEnddate.setText(date)
            }
        }


        //LOCATION
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val lat = data?.getDoubleExtra("LAT", 0.0)
                val lng = data?.getDoubleExtra("LNG", 0.0)
                mMap?.clear()
                val pos = LatLng(lat!!, lng!!)
                mMap?.addMarker(MarkerOptions().position(pos).title("Event Location"))
                location = pos
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 10f))

                binding.eventDetailsActivityAddressTextField.setText(getAddressFromLatLng(this, pos))
            }

        }
        binding.btnSetlocationMap.setOnClickListener {

            val intent = Intent(this, SetLocationMapActivity::class.java)
            resultLauncher.launch(intent)
        }

        //IMAGE
        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.eventDetailsActivityPicture.setImageURI(selectedImageUri)
                binding.eventDetailsActivityPicture.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
        disableWidgets(false)

        binding.eventDetailsActivityPicture.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.btnSetlocationMap.visibility = View.GONE

        binding.eventDetailsActivityBackButton.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.eventDetailsActivityEditButton.setOnClickListener {
            if (binding.eventDetailsActivityEditButton.text == "Edit Event") {
                binding.eventDetailsActivityEditButton.text = "Save Event"
                binding.eventDetailsActivityEditButton.setTextColor(getColor(R.color.green))
                disableWidgets(true)
                binding.btnSetlocationMap.visibility = View.VISIBLE
            } else {
                var event = checkParams()
                if (event != null) {
                    try {
                        if (curr_event == null) {
                            var result: ClubEvent? = null
                     try{
                                result = apiRevup.postEvent(event, selectedImageUri, applicationContext)
                            }catch(e: Exception){
                                Toast.makeText(this, "Error uploading event", Toast.LENGTH_SHORT).show()
                            }

                            if (result!=null) {
                                Toast.makeText(this, "Event saved", Toast.LENGTH_LONG).show()
                                binding.eventDetailsActivityEditButton.text = "Edit Event"
                                binding.eventDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
                                disableWidgets(false)
                                onBackPressed()
                            } else {
                                Toast.makeText(this, "Error saving Event", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            event.id = curr_event!!.id
                            var result =
                                apiRevup.putEvent(event, selectedImageUri, applicationContext)
                            if (result) {
                                Toast.makeText(this, "Event saved", Toast.LENGTH_LONG).show()
                                binding.eventDetailsActivityEditButton.text = "Edit Event"
                                binding.eventDetailsActivityEditButton.setTextColor(getColor(R.color.orange))
                                disableWidgets(false)
                            } else {
                                Toast.makeText(this, "Error saving Event", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error saving Event. ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        Log.i("ERROR Event SAVE", e.message.toString())
                    }
                }
            }
        }



        if(curr_event==null){
            binding.eventDetailsActivityPicture.setImageResource(R.drawable.baseline_add_photo_alternate_24)
        }else{
            if (event?.bitmap != null) {
                try {
                    binding.eventDetailsActivityPicture.setImageBitmap(event?.bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error setting event image", Toast.LENGTH_SHORT).show()
                }
            } else {
                try {
                    Glide.with(this).load(image_path + event?.picture)
                        .into(binding.eventDetailsActivityPicture)
                    binding.eventDetailsActivityPicture.scaleType = ImageView.ScaleType.CENTER_CROP
                } catch (e: Exception) {
                    Toast.makeText(this, "Error setting event image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.eventDetailsActivityNameTextField.setText(event?.name?.toString() ?: "")
        binding.eventDetailsActivityAddressTextField.setText(event?.address?.toString() ?: "")

        var club: Club? = null
        try {
            if(event!=null){
                club = apiRevup.getClubById(event.clubId, this)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error getting club data", Toast.LENGTH_SHORT).show()
        }

        if (club != null) {
            binding.eventDetailsActivityClub.setText(club.name)
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.eventDetailsActivityStartdate.setText(if(event?.startDate == null) "" else inputFormat.parse(event?.startDate.toString()).let { outputFormat.format(it) })
        binding.eventDetailsActivityEnddate.setText(if(event?.endDate == null) "" else inputFormat.parse(event?.endDate.toString()).let { outputFormat.format(it) })

        binding.eventDetailsActivityDescript.setText(event?.description?.toString() ?: "")

        var state: EventState? = null
        try {
            if(event!=null){
                state = apiRevup.getEventStateById(event!!.state, this)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error getting event state data", Toast.LENGTH_SHORT).show()
        }

        if (state != null) {
            binding.eventDetailsActivityState.setText(state.name.toString())
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment_eventsDetails_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(editable){
            binding.eventDetailsActivityEditButton.performClick()
        }
    }

    fun disableWidgets(set: Boolean = false) {
        binding.eventDetailsActivityPicture.isEnabled = set
        binding.eventDetailsActivityLayoutNameTextField.isEnabled = set
        binding.eventDetailsActivityLayoutAddressTextField.isEnabled = false
        binding.eventDetailsActivityLayoutclub.isEnabled = set
        binding.eventDetailsActivityLayoutstartdate.isEnabled = set
        binding.eventDetailsActivityLayoutenddate.isEnabled = set
        binding.eventDetailsActivityLayoutdescript.isEnabled = set
        binding.eventDetailsActivityLayoutstate.isEnabled = set
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings?.isZoomControlsEnabled = false

        if(event!=null){
            val pos = LatLng(event!!.lat, event.long)

            val markerOption = MarkerOptions()
            markerOption.position(pos)

            markerOption.title(event.name.toString())

            map?.addMarker(markerOption)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
        }
    }

    fun checkParams(): ClubEvent? {
        var image: Uri? = null
        var name: String? = null
        var address: String? = null
        var club: Club? = null
        var startDate: String? = null
        var endDate: String? = null
        var descript: String? = null
        var state: EventState? = null
        var localdatetime_now = Date.from(LocalDateTime.now()!!.atZone(ZoneId.systemDefault()).toInstant())

        if (selectedImageUri==null) {
            if(curr_event==null){
                Toast.makeText(this, "Image not selected", Toast.LENGTH_LONG).show()
                return null
            }else{
                Toast.makeText(this, "A new image has not been selected, so the old image will be uploaded.", Toast.LENGTH_SHORT).show()
                binding.eventDetailsActivityPicture.setImageBitmap(curr_event?.bitmap)
            }
        } else{
            image = selectedImageUri
        }
        if (binding.eventDetailsActivityNameTextField.text.toString() == "") {
            Toast.makeText(this, "Please select a name", Toast.LENGTH_LONG).show()
            return null
        }
        if (binding.eventDetailsActivityAddressTextField.text.toString() == "") {
            Toast.makeText(this, "Please select a address", Toast.LENGTH_LONG).show()
            return null
        }
        if (binding.eventDetailsActivityClub.text.toString() == "") {
            Toast.makeText(this, "Please select a club", Toast.LENGTH_LONG).show()
            return null
        }
        try {
            var availableClubs = apiRevup.getClubsByMember(current_user!!.id!!, this)
            var available = false
            for (clubf in availableClubs!!) {
                if (clubf.name == binding.eventDetailsActivityClub.text.toString()) {
                    available = true
                    club = clubf
                    break
                }
            }
            if (!available) {
                return null
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error getting club check", Toast.LENGTH_LONG).show()
            return null
        }

        if (binding.eventDetailsActivityStartdate.text.toString() == "") {
            Toast.makeText(this, "Please select a start date", Toast.LENGTH_LONG).show()
            return null
        }else{
            try {
                if (FormatDate(binding.eventDetailsActivityStartdate.text.toString()).before(
                        localdatetime_now
                    )
                ) {
                    Toast.makeText(this, "Start date must be in the future", Toast.LENGTH_LONG).show()
                    return null
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error parsing date", Toast.LENGTH_LONG).show()
                return null
            }
        }

        if (binding.eventDetailsActivityEnddate.text.toString() == "") {
            Toast.makeText(this, "Please select a end date", Toast.LENGTH_LONG).show()
            return null
        }else{
            try {
                if (FormatDate(binding.eventDetailsActivityEnddate.text.toString()).before(
                        localdatetime_now
                    )
                ) {
                    Toast.makeText(this, "End date must be in the future", Toast.LENGTH_LONG).show()
                    return null
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error parsing date", Toast.LENGTH_LONG).show()
                return null
            }
        }

        if (binding.eventDetailsActivityState.text.toString() == "") {
            Toast.makeText(this, "Please select a state", Toast.LENGTH_LONG).show()
            return null
        }
        try {
            var states = apiRevup.getEventStates(this)
            var available = false
            for (statef in states!!) {
                if (statef.name == binding.eventDetailsActivityState.text.toString()) {
                    available = true
                    state = statef
                    break
                }
            }
            if (!available) {
                return null
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error getting state check", Toast.LENGTH_LONG).show()
            return null
        }
        if(location==null){
            return null
        }

        name = binding.eventDetailsActivityNameTextField.text.toString()
        address = binding.eventDetailsActivityAddressTextField.text.toString()

        startDate = binding.eventDetailsActivityStartdate.text.toString()
        endDate = binding.eventDetailsActivityEnddate.text.toString()
        descript = binding.eventDetailsActivityDescript.text.toString()



        if (image == null && curr_event != null) {
            return ClubEvent(null,name, address,club!!.id!!, curr_event!!.picture,startDate,null,endDate,descript, state!!.id, location!!.latitude, location!!.longitude)
        }

        return ClubEvent(null,name, address,club!!.id!!, null, startDate,null,endDate,descript, state!!.id, location!!.latitude, location!!.longitude)
    }

    fun getAddressFromLatLng(context: Context, latLng: LatLng): String? {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return addresses?.firstOrNull()?.getAddressLine(0)
    }
}