package com.example.revup.ADAPTERS

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import com.example.revup._DATACLASS.Message
import com.example.revup._DATACLASS.current_user

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
        val senderText: TextView = view.findViewById(R.id.senderText)
        val container: LinearLayout = view.findViewById(R.id.messageContainer)
        val card: CardView = view.findViewById(R.id.cardview_message_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]

        holder.senderText.text = msg.senderMemberName
        holder.messageText.text = msg.contentMessage.toString()

        // Change alignment if it's the user's own message

        holder.container.gravity = if (msg.senderId== current_user!!.id) Gravity.END else Gravity.START
        val layoutParams = holder.card.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = if (msg.senderId== current_user!!.id) Gravity.END else Gravity.START

    }

    override fun getItemCount(): Int = messages.size
}