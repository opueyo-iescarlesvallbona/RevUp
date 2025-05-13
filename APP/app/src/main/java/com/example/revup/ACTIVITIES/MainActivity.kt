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
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityMainBinding
import com.google.android.material.animation.AnimationUtils


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

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
            intent.putExtra("member", current_user)
            startActivity(intent)
        }

        initFragment(HomeFragment())
        binding.mainActivityBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    initFragment(HomeFragment())
                    activeCreatePostButton(true)
                    true
                }
                R.id.events -> {
                    initFragment(EventsFragment())
                    true
                }
                R.id.search -> {
                    initFragment(SearchFragment())
                    true
                }
                R.id.chats -> {
                    initFragment(ChatsFragment())
                    true
                }
                else -> {true}
            }


            if(it.itemId != R.id.home){
                activeCreatePostButton(false)
            }
            true
        }
        binding.mainActivityBtnAdd.setOnClickListener{
            onAddButtonClicked()
        }
        binding.mainActivityBtnAddText.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            val intent = Intent(this, AddTextPostActivity::class.java)
            startActivity(intent)
        }
        binding.mainActivityBtnAddImage.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            val intent = Intent(this, AddImagePostActivity::class.java)
            startActivity(intent)
        }
        binding.mainActivityBtnAddRoute.setOnClickListener {
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
            val intent = Intent(this, AddRoutePostActivity::class.java)
            startActivity(intent)
        }
    }

    fun activeCreatePostButton(activate: Boolean){
        if(secFloatingBtnVisible){
            setEnableFloatingButtons(false, true)
        }
        if(activate){
            binding.mainActivityBtnAdd.visibility = View.VISIBLE
        }else{
            binding.mainActivityBtnAdd.visibility = View.INVISIBLE
        }
    }

    fun initFragment(fragment: Fragment){
        val transaccio = supportFragmentManager.beginTransaction()
        transaccio.replace(R.id.mainActivity_fragmentContainerView, fragment)
        transaccio.commit()
    }

    fun onAddButtonClicked(){
        secFloatingBtnVisible = !secFloatingBtnVisible
        setEnableFloatingButtons(secFloatingBtnVisible, false)
        setAnimation(secFloatingBtnVisible)
    }

    private fun setEnableFloatingButtons(visible: Boolean, reset: Boolean){
        if(!visible){
            binding.mainActivityBtnAddText.visibility = View.INVISIBLE
            binding.mainActivityBtnAddImage.visibility = View.INVISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.INVISIBLE
        }else{
            binding.mainActivityBtnAddText.visibility = View.VISIBLE
            binding.mainActivityBtnAddImage.visibility = View.VISIBLE
            binding.mainActivityBtnAddRoute.visibility = View.VISIBLE
        }
        if(reset && binding.mainActivityBtnAddText.animation != null && binding.mainActivityBtnAddImage.animation != null){
            binding.mainActivityBtnAddText.clearAnimation()
            binding.mainActivityBtnAddImage.clearAnimation()
            binding.mainActivityBtnAddRoute.clearAnimation()
        }
        setClickable(visible)
    }

    private fun setAnimation(secFloatingBtnVisible: Boolean){
        if(!secFloatingBtnVisible){
            binding.mainActivityBtnAddText.startAnimation(anim_toBottom)
            binding.mainActivityBtnAddImage.startAnimation(anim_toBottom)
            binding.mainActivityBtnAddRoute.startAnimation(anim_toBottom)
            binding.mainActivityBtnAdd.animate().rotationBy(-45f).setDuration(500).start()
        }else{
            binding.mainActivityBtnAddText.startAnimation(anim_fromBottom)
            binding.mainActivityBtnAddImage.startAnimation(anim_fromBottom)
            binding.mainActivityBtnAddRoute.startAnimation(anim_fromBottom)
            binding.mainActivityBtnAdd.animate().rotationBy(45f).setDuration(500).start()
        }
    }

    private fun setClickable(clickable: Boolean){
        if(!clickable){
            binding.mainActivityBtnAddText.isClickable = false
            binding.mainActivityBtnAddImage.isClickable = false
            binding.mainActivityBtnAddRoute.isClickable = false
        }else{
            binding.mainActivityBtnAddText.isClickable = true
            binding.mainActivityBtnAddImage.isClickable = true
            binding.mainActivityBtnAddRoute.isClickable = true
        }
    }

    override fun onPause() {
        super.onPause()
        secFloatingBtnVisible = false
        setEnableFloatingButtons(false, true)
    }

}