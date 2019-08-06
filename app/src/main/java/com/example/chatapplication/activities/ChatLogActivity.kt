package com.example.chatapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatapplication.R
import com.example.chatapplication.adapters.ChatFromItem
import com.example.chatapplication.models.ChatMessage
import com.example.chatapplication.adapters.ChatToItem
import com.example.chatapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerViewChat.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessage()

        buttonSend.setOnClickListener {
            Log.e("Chat", "tried to chat")
            performSendMessage()
        }
    }

    private fun listenForMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessageActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser, this@ChatLogActivity))
                    }
                    else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!, this@ChatLogActivity))
                    }
                }
                recyclerViewChat.scrollToPosition(adapter.itemCount-1)
            }
        })
    }

    private fun performSendMessage() {
        val text = editTextChat.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(
            reference.key!!,
            text,
            fromId,
            toId!!,
            System.currentTimeMillis() / 1000
        )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                editTextChat.text.clear()
                recyclerViewChat.scrollToPosition(adapter.itemCount-1)
            }
        toReference.setValue(chatMessage)

        val latestReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestReference.setValue(chatMessage)

        val latestToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestToReference.setValue(chatMessage)
    }
}