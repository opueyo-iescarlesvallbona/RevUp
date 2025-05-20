package com.example.revup._API

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.Message
import com.example.revup._DATACLASS.current_user
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import java.util.Date

class ChatService(private val context: Context, private val memberName: String, private val onMessageReceived: (Message) -> Unit) {
    val apiRevUp = RevupCrudAPI()
    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("http://172.16.24.136:5178/Chat")
        .build()

    fun connect() {
        hubConnection.on("ReceiveMessage", { sender, chatMessageJson ->
            var senderObj: Member? = null
            try{
                senderObj = apiRevUp.getMemberByMemberName(sender.toString(), context)
            }catch(e: Exception){
                Log.e("Error", e.toString())
            }

            //val message = Gson().fromJson(chatMessageJson.toString(), Message::class.java)

            val m = Message(
                senderId = senderObj!!.id!!, isOwnMessage = if(senderObj.membername == memberName) true else false,
                receiverId = current_user!!.id!!,
                datetime = Date().toString(),
                contentMessage = chatMessageJson,
                stateId = null,
                messageState = null,
                member = senderObj,
                senderMemberName = senderObj.membername,
                recieverMemberName = current_user!!.membername,
                member1 = current_user
            )
            onMessageReceived(m)
        }, String::class.java, String::class.java)

        hubConnection.on("ReceiveGroupMessage", { sender, chatMessageJson ->
            var senderObj: Member? = null
            try{
                senderObj = apiRevUp.getMemberByMemberName(sender.toString(), context)
            }catch(e: Exception){
                Log.e("Error", e.toString())
            }

            //val message = Gson().fromJson(chatMessageJson.toString(), Message::class.java)

            val m = Message(
                senderId = senderObj!!.id!!, isOwnMessage = if(senderObj!!.membername == memberName) true else false,
                receiverId = current_user!!.id!!,
                datetime = Date().toString(),
                contentMessage = chatMessageJson,
                stateId = null,
                messageState = null,
                member = senderObj!!,
                senderMemberName = senderObj!!.membername,
                recieverMemberName = current_user!!.membername,
                member1 = current_user
            )
            onMessageReceived(m)
        }, String::class.java, String::class.java)

        hubConnection.start().blockingAwait()
        hubConnection.send("Register", memberName)
    }

    fun sendMessageToUser(target: String, message: String) {
        hubConnection.send("SendMessageToUser", target, message)
        val m = Message(
            senderId = current_user!!.id!!, isOwnMessage = true,
            receiverId = null,
            datetime = Date().toString(),
            contentMessage = message,
            stateId = null,
            messageState = null,
            member = current_user!!,
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