package com.app.kishore.pguser.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.adapter.RoomMemberAdapter
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.LoadingDialog
import com.app.kishore.pguser.helpers.VolleyRequest
import com.app.kishore.pguser.models.Member
import com.app.kishore.pguser.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private val TAG = "HOME"
    private lateinit var pgowner : JSONObject
    private lateinit var roomMemberAdapter : RoomMemberAdapter
    private lateinit var roomMemberRV : RecyclerView
    private lateinit var memberList : ArrayList<Member>
    private lateinit var roomMatesJsonArray : JSONArray
    private lateinit var user : JSONObject
    private  var groupChatRoomId : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        memberList = ArrayList()
        pgowner = JSONObject()
        roomMatesJsonArray = JSONArray()

        user = JSONObject(intent.getStringExtra("USER"))
        admin_chat_icon.visibility = View.VISIBLE
        admin_phone_icon.visibility = View.VISIBLE
        img_group_chat.isEnabled = true
        roomMemberRV = findViewById(R.id.roomMemberRecyclerView)
        roomMemberAdapter = RoomMemberAdapter(this,memberList, JSONObject(intent.getStringExtra("USER")))
        roomMemberRV.adapter = roomMemberAdapter
        fetchData()

        if(user.has("profileimage"))
        Glide.with(this).load(user.get("profileimage"))
            .error(R.drawable.user)
            .diskCacheStrategy(
            DiskCacheStrategy.ALL).into(user_profile_image)
        else Glide.with(this).load(R.drawable.user)
            .error(R.drawable.user)
            .diskCacheStrategy(
                DiskCacheStrategy.ALL).into(user_profile_image)


        notificationBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val i = Intent(this@HomeActivity, NotificationActivity::class.java)
                i.putExtra("USER",user.toString())
                startActivity(i)
            }
        })

        img_group_chat.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val i = Intent(this@HomeActivity, GroupChatActivity::class.java)
                i.putExtra("USER",user.toString())
                i.putExtra("ROOM_ID",groupChatRoomId)
                i.putExtra("ROOM_MEMBERS",roomMatesJsonArray.toString())
                startActivity(i)
            }
        })


        user_profile_image.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val i = Intent(this@HomeActivity, ProfileActivity::class.java)
                i.putExtra("USER",user.toString())
                startActivity(i)
            }
        })

        admin_chat_icon.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val gson = Gson()
                val i = Intent(this@HomeActivity, ChatActivity::class.java)
                i.putExtra("USER",user.toString())
                i.putExtra("MEMBER",pgowner.toString())
                startActivity(i)
            }
        })



        pgname_textView.setOnClickListener{
            val i = Intent(this@HomeActivity, PGInfoActivity::class.java)
            i.putExtra("USER",user.toString())
            i.putExtra("PGOWNER",pgowner.toString())
            startActivity(i)
        }



        admin_phone_icon.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                // Toast.makeText(context, "Call!", Toast.LENGTH_SHORT).show()
                val phoneIntent =  Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+pgowner.getString("phone")))
                if (ContextCompat.checkSelfPermission(this@HomeActivity,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this@HomeActivity as Activity, arrayOf(android.Manifest.permission.CALL_PHONE),
                        91)

                } else {
                    startActivity(phoneIntent);

                }

            }
        })



    }


    fun fetchData(){
        val loadingDialog = LoadingDialog(this)
        loadingDialog.setCancelable(false)
        loadingDialog.show()
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                memberList.clear()
                groupChatRoomId = response.getString("_id")
                pgowner = response.get("pgownerid") as JSONObject
                pgname_textView.setText(pgowner.getString("pgname"))
                tvAdminName.text = pgowner.getString("name")
                tvRoomDesc.text = "Floor No "+response.getString("floornumber")+", Room No "+response.getString("roomnumber")+"\n"+response.getString("beds")+" Beds Per Room, TV, Washroom"



                 roomMatesJsonArray = response.getJSONArray("users")
                if(roomMatesJsonArray.length() > 0){
                    for(i in 0..roomMatesJsonArray.length()-1){
                        val teammateObj = roomMatesJsonArray.get(i) as JSONObject
                        val teamMate = teammateObj.get("user") as JSONObject
                        val name = teamMate.get("name").toString()
                        val _id = teamMate.getString("_id")
                        val email = teamMate.getString("email")
                        val phone = teamMate.getString("phone")
                        val profileimage = teamMate.getString("profileimage")

                        if(_id != JSONObject(intent.getStringExtra("USER")).getString("_id")){
                        val mem = Member(name,_id,email,phone,profileimage)
                        memberList.add(mem)
                        }
                    }

                }

                roomMemberAdapter.notifyDataSetChanged()
                loadingDialog.cancel()
            }
            override fun errorCallback(error_message: VolleyError) {
                loadingDialog.cancel()
                tvAdminName.text = "No Admin"
                tvRoomDesc.text = "No room allotted to the user."
                img_group_chat.isEnabled = false
                admin_chat_icon.visibility = View.GONE
                admin_phone_icon.visibility = View.GONE
                Log.d(TAG,"ERROR : ->  "+error_message?.toString())
               // Toast.makeText(this@HomeActivity, "Try again after sometime!", Toast.LENGTH_LONG).show()
                error_message.printStackTrace()
            }

        })
        
        val token = user.getString("token")
        request.getRequest(Constants.GET_ROOM_DETAILS,token)

    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }
}