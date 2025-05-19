package com.example.revup._API

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.revup._DATACLASS.Message
import com.example.revup._DATACLASS.current_user
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import java.util.Date

class ChatService(private val context: Context, private val memberName: String, private val onMessageReceived: (Message) -> Unit) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("http://172.16.24.136:5178/Chat")
        .build()

    fun connect() {
        hubConnection.on("ReceiveMessage", { sender, message ->
            Log.i("MESSAGE", "Message from $sender: $message")
        }, String::class.java, String::class.java)

        hubConnection.on("ReceiveGroupMessage", { sender, message ->
            println("Group message from $sender: $message")
        }, String::class.java, String::class.java)

        hubConnection.start().blockingAwait()
        hubConnection.send("Register", memberName)
    }

    fun sendMessageToUser(target: String, message: String) {
        hubConnection.send("SendMessageToUser", target, message)
        val m = Message(
            senderId = current_user!!.id, isOwnMessage = true,
            receiverId = null,
            datetime = Date(),
            contentMessage = message,
            stateId = null,
            messageState = null,
            member = null,
            senderMemberName = memberName,
            recieverMemberName = null,
            member1 = null
        )
        onMessageReceived(m)
    }

    fun joinGroup(group: String) {
        hubConnection.send("JoinGroup", group)
    }

    fun sendMessageToGroup(group: String, message: String) {
        hubConnection.send("SendMessageToGroup", group, message)
    }

    fun disconnect() {
        hubConnection.stop()
    }
}