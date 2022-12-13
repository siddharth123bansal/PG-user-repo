package com.app.kishore.pguser.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.adapter.notificationAdapter
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.LoadingDialog
import com.app.kishore.pguser.helpers.Utils
import com.app.kishore.pguser.helpers.VolleyRequest
import com.app.kishore.pguser.models.Notification
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject



class NotificationActivity : AppCompatActivity() {
    val TAG = "NOTIFICATION"
    private lateinit var notificationAdapter: notificationAdapter
    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationList : ArrayList<Notification>
    private lateinit var user : JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        notificationList = ArrayList()
        notificationRecyclerView = findViewById(R.id.recyclerViewNotifications)
        notificationAdapter = notificationAdapter(this,notificationList)
        notificationRecyclerView.adapter = notificationAdapter
        user = JSONObject(intent.getStringExtra("USER"))

        fetchNotifications(user.getString("token"))



        findViewById<ImageView>(R.id.back_btn).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }
        })
    }

    fun fetchNotifications(userToken : String){
        val loadingDialog = LoadingDialog(this)
        loadingDialog.setCancelable(false)
        loadingDialog.show()
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                notificationList.clear()
                if(response.has("message") && response.getString("message").equals("No notifications")){
                    notificationList.add(Notification("No notifications"))

                    loadingDialog.cancel()
                }else {
                    val notifications = response.getJSONArray("notifications")
                    if(notifications.length() > 0){
                        for(i in 0..notifications.length()-1){
                            val notification = notifications.get(i) as JSONObject
                            val notificationObj = Notification(notification.getString("message"))
                            notificationList.add(notificationObj)
                        }
                    }
                }

                notificationAdapter.notifyDataSetChanged()
                loadingDialog.cancel()



            }


            override fun errorCallback(error_message: VolleyError) {
                loadingDialog.cancel()
                Log.d(TAG,"ERROR : ->  "+error_message?.toString())
                Toast.makeText(this@NotificationActivity, "Notifications not found!", Toast.LENGTH_LONG).show()
                error_message.printStackTrace()
            }

        })
        request.getRequest(Constants.GET_NOTIFICATIONS,userToken)
    }
}