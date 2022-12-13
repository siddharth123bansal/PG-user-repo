package com.app.kishore.pguser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.kishore.pguser.R
import com.app.kishore.pguser.models.Notification

class notificationAdapter(val context: Context, val notificationList : ArrayList<Notification>) : RecyclerView.Adapter<notificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificationAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification : Notification = notificationList.get(position)
        holder.notificationText.text = notification.notification
    }

    override fun getItemCount(): Int {
      return  notificationList.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val notificationText = itemView.findViewById<TextView>(R.id.notification_text)

    }
}