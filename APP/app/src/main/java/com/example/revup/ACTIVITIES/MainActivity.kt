package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.revup.FRAGMENTS.ChatsFragment
import com.example.revup.FRAGMENTS.EventsFragment
import com.example.revup.FRAGMENTS.HomeFragment
import com.example.revup.FRAGMENTS.SearchFragment
import com.example.revup.R
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.curr_event
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityMainBinding
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val anim_fromBottom: Animation by lazy { loadAnimation(this, R.anim.from_bottom_anim) }
    private val anim_toBottom: Animation by lazy { loadAnimation(this, R.anim.to_bottom_anim) }

    private var secFloatingBtnVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainActivityBtnProfile.setOnClickListener {
            val intent = Intent(this, MemberDetailsActivity::class.java)
            curr_member = current_user
            startActivity(intent)
        }

        binding.mainActivityBtnNotifications.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Log out")
                .setMessage("You are going to log out. Are you sure?")
                .setPositiveButton("Log out") { dialog, _ ->
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    sharedPreferences.edit(){
                        clear()
                        apply()
                    }
                    val intent = Intent(this, LogInActivity::class.java)
                    current_user = null
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        initFragment(HomeFragment())
        binding.mainActivityBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setAnimation(false)
                    secFloatingBtnVisible = false
                    initFragment(HomeFragment())
                    activeCreatePostButton(true)
                    true
                }

                R.id.events -> {
                    setAnimation(false)
                    secFloatingBtnVisible = false
                    initFragment(EventsFragment())
                    activeCreatePostButton(true)
                    true
                }

                R.id.search -> {
                    setAnimation(false)
                    secFloatingBtnVisible = false
                    initFragment(SearchFragment())
                    activeCreatePostButton(true)
                    true
                }

                R.id.chats -> {
                    setAnimation(false)
                    initFragment(ChatsFragment())
                    true
                }

                else -> {
                    true
                }
            }


            if (it.itemId != R.id.home && it.itemId != R.id.events && it.itemId != R.id.search) {
                activeCreatePostButton(false)
            }
            true
        }
        binding.mainActivityBtnAdd.setOnClickListener {
            onAddButtonClicked()
        }
        binding.mainActivityBtnAddText.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            val intent = Intent(this, AddTextPostActivity::class.java)
            startActivity(intent)
        }
        binding.mainActivityBtnAddImage.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.home) {
                val intent = Intent(this, AddImagePostActivity::class.java)
                startActivity(intent)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.events) {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra("editable", true)
                curr_event = null
                startActivity(intent)
            }

        }

        binding.mainActivityBtnAddClub.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.search) {
                val intent = Intent(this, EditClubActivity::class.java)
                curr_club = null
                startActivity(intent)
            }
        }
        binding.mainActivityBtnAddRoute.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.home) {
                val intent = Intent(this, AddRoutePostActivity::class.java)
                startActivity(intent)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.events) {
                val intent = Intent(this, RecordRouteActivity::class.java)
                startActivity(intent)
            }

        }
    }

    fun activeCreatePostButton(activate: Boolean) {
        if (secFloatingBtnVisible) {
            if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.home) {
                setEnableFloatingButtons(true, false)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.events) {
                setEnableFloatingButtonsEvent(true, false)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.search) {
                setEnableFloatingButtonsClub(true, false)
            }
        }
        if (activate) {
            binding.mainActivityBtnAdd.visibility = View.VISIBLE
        } else {
            binding.mainActivityBtnAdd.visibility = View.INVISIBLE
        }
    }

    fun initFragment(fragment: Fragment) {
        val transaccio = supportFragmentManager.beginTransaction()
        transaccio.replace(R.id.mainActivity_fragmentContainerView, fragment)
        transaccio.commit()
    }

    fun onAddButtonClicked() {
        secFloatingBtnVisible = !secFloatingBtnVisible
        if (secFloatingBtnVisible) {
            if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.home) {
                setEnableFloatingButtons(secFloatingBtnVisible, false)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.events) {
                setEnableFloatingButtonsEvent(secFloatingBtnVisible, false)
            } else if (binding.mainActivityBottomNavigationView.selectedItemId == R.id.search) {
                setEnableFloatingButtonsClub(secFloatingBtnVisible, false)
            }
        }
        setAnimation(secFloatingBtnVisible)
    }

    private fun setEnableFloatingButtonsClub(visible: Boolean, reset: Boolean) {
        binding.mainActivityBtnAddText.visibility = View.GONE
        binding.mainActivityBtnAddRoute.visibility = View.GONE
        binding.mainActivityBtnAddImage.visibility = View.GONE

        if (!visible) {
            binding.mainActivityBtnAddClub.visibility = View.INVISIBLE
        } else {
            binding.mainActivityBtnAddClub.visibility = View.VISIBLE
        }
        if (reset && binding.mainActivityBtnAddClub.animation != null) {
            binding.mainActivityBtnAddClub.clearAnimation()
            binding.mainActivityBtnAddText.clearAnimation()
            binding.mainActivityBtnAddImage.clearAnimation()
            binding.mainActivityBtnAddRoute.clearAnimation()
            if(binding.mainActivityBtnAdd.rotation==45f) {
                binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            }
        }
        setClickable(visible)
    }

    private fun setEnableFloatingButtonsEvent(visible: Boolean, reset: Boolean) {
        binding.mainActivityBtnAddText.visibility = View.GONE
        binding.mainActivityBtnAddClub.visibility = View.GONE
        if (!visible) {
            binding.mainActivityBtnAddImage.visibility = View.INVISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.INVISIBLE
        } else {
            binding.mainActivityBtnAddImage.visibility = View.VISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.VISIBLE
        }
        if (reset && binding.mainActivityBtnAddImage.animation != null) {
            binding.mainActivityBtnAddImage.clearAnimation()
            binding.mainActivityBtnAddRoute.clearAnimation()
            binding.mainActivityBtnAddClub.clearAnimation()
            if(binding.mainActivityBtnAdd.rotation==45f) {
                binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            }
        }
        setClickable(visible)
    }

    private fun setEnableFloatingButtons(visible: Boolean, reset: Boolean) {
        binding.mainActivityBtnAddClub.visibility = View.GONE
        if (!visible) {
            binding.mainActivityBtnAddText.visibility = View.INVISIBLE
            binding.mainActivityBtnAddImage.visibility = View.INVISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.INVISIBLE
        } else {
            binding.mainActivityBtnAddText.visibility = View.VISIBLE
            binding.mainActivityBtnAddImage.visibility = View.VISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.VISIBLE
        }
        if (reset && binding.mainActivityBtnAddText.animation != null && binding.mainActivityBtnAddImage.animation != null) {
            binding.mainActivityBtnAddText.clearAnimation()
            binding.mainActivityBtnAddImage.clearAnimation()
            binding.mainActivityBtnAddRoute.clearAnimation()
            binding.mainActivityBtnAddClub.clearAnimation()
            if(binding.mainActivityBtnAdd.rotation==45f) {
                binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            }
        }
        setClickable(visible)
    }

    private fun setAnimation(secFloatingBtnVisible: Boolean) {
        if (!secFloatingBtnVisible) {
            if (binding.mainActivityBtnAdd.rotation == 45f) {
                if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.home){
                    binding.mainActivityBtnAddClub.visibility = View.GONE
                    binding.mainActivityBtnAddText.startAnimation(anim_toBottom)
                    binding.mainActivityBtnAddImage.startAnimation(anim_toBottom)
                    binding.mainActivityBtnAddRoute.startAnimation(anim_toBottom)
                }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.events){
                    binding.mainActivityBtnAddClub.visibility = View.GONE
                    binding.mainActivityBtnAddText.visibility = View.GONE
                    binding.mainActivityBtnAddImage.startAnimation(anim_toBottom)
                    binding.mainActivityBtnAddRoute.startAnimation(anim_toBottom)

                }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.search){

                    binding.mainActivityBtnAddRoute.clearAnimation()
                    binding.mainActivityBtnAddText.clearAnimation()
                    binding.mainActivityBtnAddImage.clearAnimation()
                    binding.mainActivityBtnAddClub.startAnimation(anim_toBottom)
                }

                if(binding.mainActivityBtnAdd.rotation==45f) {
                    binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
                }
            }
        } else {
            if (binding.mainActivityBtnAdd.rotation == 0f) {
                if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.home){
                    binding.mainActivityBtnAddText.startAnimation(anim_fromBottom)
                    binding.mainActivityBtnAddImage.startAnimation(anim_fromBottom)
                    binding.mainActivityBtnAddRoute.startAnimation(anim_fromBottom)
                }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.events){
                    binding.mainActivityBtnAddImage.startAnimation(anim_fromBottom)
                    binding.mainActivityBtnAddRoute.startAnimation(anim_fromBottom)
                }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.search){
                    binding.mainActivityBtnAddClub.startAnimation(anim_fromBottom)
                }

                if(binding.mainActivityBtnAdd.rotation==0f){
                    binding.mainActivityBtnAdd.animate().rotationBy(45f).setDuration(500).start()
                }

            }
        }
    }

    private fun setClickable(clickable: Boolean) {
        if (!clickable) {
            binding.mainActivityBtnAddText.isClickable = false
            binding.mainActivityBtnAddImage.isClickable = false
            binding.mainActivityBtnAddRoute.isClickable = false
            binding.mainActivityBtnAddClub.isClickable = false
        } else {
            binding.mainActivityBtnAddText.isClickable = true
            binding.mainActivityBtnAddImage.isClickable = true
            binding.mainActivityBtnAddRoute.isClickable = true
            binding.mainActivityBtnAddClub.isClickable = true
        }
    }

    override fun onPause() {
        super.onPause()
        secFloatingBtnVisible = false
        if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.home){
            setEnableFloatingButtons(false, true)
        }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.events){
            setEnableFloatingButtonsEvent(false, true)
        }else if(binding.mainActivityBottomNavigationView.selectedItemId==R.id.search){
            setEnableFloatingButtonsClub(false, true)
        }
    }

}