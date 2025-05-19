package com.example.revup.ADAPTERS

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import com.example.revup._DATACLASS.Message

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
        val container: LinearLayout = view.findViewById(R.id.messageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        holder.messageText.text = "${msg.member?.membername}: ${msg.contentMessage}"

        // Change alignment if it's the user's own message
//        val params = holder.container.layoutParams as RecyclerView.LayoutParams
//        holder.container.gravity = if (msg.isOwnMessage) Gravity.END else Gravity.START
    }

    override fun getItemCount(): Int = messages.size
}