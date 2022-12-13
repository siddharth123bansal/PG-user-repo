package com.app.kishore.pguser.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.kishore.pguser.R
import com.app.kishore.pguser.adapter.messageAdapter
import com.app.kishore.pguser.models.Member
import com.app.kishore.pguser.models.Message
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_group_chat.*
import org.json.JSONObject


class GroupChatActivity : AppCompatActivity() {
    private val TAG = "CHAT"
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageTextBox : EditText
    private lateinit var sendMessageBtn : ImageView
    private lateinit var receiverName : TextView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: messageAdapter
    private lateinit var mDbRef : DatabaseReference
    private lateinit var userID : String
    private lateinit var user : JSONObject
    private lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageTextBox = findViewById(R.id.messageBox)
        sendMessageBtn = findViewById(R.id.sendMessageButton)
        mDbRef = FirebaseDatabase.getInstance().getReference()
        groupNameAppBar.text = "Group Chat"
        gson = Gson()
        messageList = ArrayList()
        user = JSONObject( intent.getStringExtra("USER"))
        userID = user.get("_id").toString()
        val ROOM_ID = intent.getStringExtra("ROOM_ID")

        messageAdapter = messageAdapter(this,messageList,user,null,true)
        chatRecyclerView.adapter = messageAdapter

//        senderRoom = (receiver._id + userID)
//        receiverRoom = (userID + receiver._id)


        //adding data to recycler view

        mDbRef.child("group-chats").child(ROOM_ID!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })




        findViewById<ImageView>(R.id.back_btn).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }
        })


        sendMessageBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val message = messageTextBox.text.toString().trim()
                if(message.length != 0){
                    // adding message to database
                    val messageObj = Message(message,userID,user.getString("name"),user.getString("profileimage"))
                    mDbRef.child("group-chats").child(ROOM_ID!!).child("messages").push()
                        .setValue(messageObj)
                    messageTextBox.setText("")


                }
            }
        })
    }
}