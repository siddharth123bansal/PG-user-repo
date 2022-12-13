package com.app.kishore.pguser.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.kishore.pguser.R
import com.app.kishore.pguser.activities.ChatActivity
import com.app.kishore.pguser.models.Member
import com.google.gson.Gson
import org.json.JSONObject

class RoomMemberAdapter(val context : Context,val contactList : ArrayList<Member>, val user : JSONObject) : RecyclerView.Adapter<RoomMemberAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomMemberAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomMemberAdapter.ViewHolder, position: Int) {
        val listItem  = contactList.get(position)
        holder.contactName.text = listItem.name
        holder.contactSono.text = (position+1).toString()

        holder.callButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                // Toast.makeText(context, "Call!", Toast.LENGTH_SHORT).show()
                val phoneIntent =  Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+listItem.phone))
                if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE),
                        91)

                } else {
                   context.startActivity(phoneIntent);

                }

            }
        })


                holder.chatButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val gson = Gson()
                val i = Intent(context, ChatActivity::class.java)
                i.putExtra("USER",user.toString())
                i.putExtra("MEMBER",gson.toJson(listItem))
                context.startActivity(i)
            }
        })

    }

    override fun getItemCount(): Int {
      return contactList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val contactName = itemView.findViewById<TextView>(R.id.contactName)
        val contactSono = itemView.findViewById<TextView>(R.id.contact_sono)
        val callButton = itemView.findViewById<ImageView>(R.id.member_phone_icon)
        val chatButton = itemView.findViewById<ImageView>(R.id.member_chat_icon)

    }
}