package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ADAPTERS.MessageAdapter
import com.example.revup.R
import com.example.revup._API.ChatService
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Message
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.curr_member_chat
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityMemberChatBinding
import com.google.android.material.textfield.TextInputEditText

class MemberChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemberChatBinding
    private lateinit var chatService: ChatService
    val apiRevUp = RevupCrudAPI()
    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                if (imeVisible) imeInsets.bottom else systemBars.bottom
            )

            insets
        }
        val ToMember = curr_member_chat

        binding.ChatActivityBackButton.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        Glide.with(this).load(image_path+ ToMember!!.profilePicture).circleCrop().into(binding.ChatActivityToMemberImage)

        binding.ChatActivityToMemberImage.setOnClickListener {
            val intent = Intent(this, MemberDetailsActivity::class.java)
            curr_member = ToMember
            startActivity(intent)
        }

        binding.chatActivityMembername.setText(ToMember!!.membername)
        binding.chatActivityMembername.setOnClickListener {
            val intent = Intent(this, MemberDetailsActivity::class.java)
            curr_member = ToMember
            startActivity(intent)
        }

        var oldMessages: MutableList<Message>? = null
        oldMessages = apiRevUp.getOldMessages(current_user!!.id!!, ToMember!!.id!!, this)
        try{

        }catch(e: Exception){
            Toast.makeText(this, "Error getting old messages", Toast.LENGTH_SHORT).show()
        }

        if(oldMessages!=null){
            for (m: Message in oldMessages){
                var member = apiRevUp.getMemberById(m.senderId, this)
                m.senderMemberName = member?.membername
            }
            messages.addAll(oldMessages)
        }

        val recyclerView: RecyclerView = findViewById(R.id.ChatActivity_recyclerView)
        adapter = MessageAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        if(messages.size>0){
            recyclerView.scrollToPosition(messages.size-1)
        }

        chatService = ChatService(this, current_user!!.membername.toString()) { message ->
            runOnUiThread {
                messages.add(message)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size-1)
            }
        }
        chatService.connect()

        findViewById<ImageButton>(R.id.activity_chat_sendButton).setOnClickListener {
            val target = ToMember!!.membername
            val input = findViewById<TextInputEditText>(R.id.activity_chat_messageText)
            val messageText = input.text.toString()
            if (messageText.isNotBlank()) {
                chatService.sendMessageToUser(target.toString(), messageText)
                input.setText("")
            }
        }
        //Toast.makeText(requireContext(), "Connected", Toast.LENGTH_SHORT).show()

    }
    override fun onDestroy() {
        chatService.disconnect()
        super.onDestroy()
    }
}