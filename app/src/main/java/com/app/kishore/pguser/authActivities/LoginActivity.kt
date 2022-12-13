package com.app.kishore.pguser.authActivities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.activities.HomeActivity
import com.app.kishore.pguser.helpers.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private val TAG = "LOGIN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(Utils.isUserPresent(this)){
            val i = Intent(this@LoginActivity,HomeActivity::class.java)
            i.putExtra("USER",Utils.getUser(this))
            startActivity(i)
            finish()
        }

        val text = "<font color=#000F08>Create account?</font> <font color=#1B5299>Signup</font>"
        signupButton.setText(Html.fromHtml(text))

        signupButton.isEnabled = true
        signupButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                signupButton.isEnabled = false
                startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
                finish()
            }
        })

        btnLogin.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
             val userEmail = evLoginEmail.text.toString().trim()
             val userPassword = evLoginPassword.text.toString().trim()

                if(MyCustomUtlis.checkEmail(this@LoginActivity,userEmail) && userPassword?.length != 0){
                btnLogin.isEnabled = false
                // Login logic goes here . . .
                    processLogin(userEmail,userPassword)


                }
                else Toast.makeText(this@LoginActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun processLogin(userEmail : String, userPassword : String) {
        loading.visibility = View.VISIBLE
        val request = VolleyRequest(this@LoginActivity, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                sendDeviceToken(response.getJSONObject("user"))
                fetchUserProfile((response.get("user") as JSONObject).get("token").toString(),
                    (response.get("user") as JSONObject).get("_id").toString())
            }

            override fun errorCallback(error_message: VolleyError) {
              loading.visibility = View.GONE
                btnLogin.isEnabled = true
                var jsonError: String? = ""
                val networkResponse: NetworkResponse = error_message.networkResponse
                if (networkResponse != null && networkResponse.data != null) {
                    jsonError = String(networkResponse.data)
                    try {
                        val errorJson = JSONObject(jsonError)
                        Toast.makeText(this@LoginActivity, errorJson.getString("error"), Toast.LENGTH_SHORT).show()

                    }catch (e : Exception){
                        Toast.makeText(this@LoginActivity,"Server Error!", Toast.LENGTH_SHORT).show()
                    }                }

            }

        })


        val bodyData = JSONObject()
        bodyData.put("email",userEmail)
        bodyData.put("password",userPassword)

        request.postWithBody(Constants.USER_LOGIN,bodyData,"")


    }


    fun fetchUserProfile(userToken : String, userID : String){
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                val userObj = response as JSONObject
                userObj.put("token",userToken)
                Log.d(TAG,"PUTTING UID :->  "+userID)
                userObj.put("_id",userID)
                Log.d(TAG,userObj.toString())

                if(cbRemember.isChecked){
                    Utils.saveUser(this@LoginActivity,userObj.toString())
                }

                val i = Intent(this@LoginActivity,HomeActivity::class.java)
                i.putExtra("USER",userObj.toString())
                startActivity(i)
                finish()

                loading.visibility = View.GONE


            }


            override fun errorCallback(error_message: VolleyError) {
                loading.visibility = View.GONE
                btnLogin.isEnabled = true
                Log.d(TAG,"ERROR : ->  "+error_message?.toString())
                Toast.makeText(this@LoginActivity, "Profile not found!", Toast.LENGTH_LONG).show()
                error_message.printStackTrace()
            }

        })

        request.getRequest(Constants.GET_USER_PROFILE,userToken)
    }

    private fun sendDeviceToken(res: JSONObject) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val deviceToken = task.result
            Log.d(TAG,"DEV TOKEN :->    "+deviceToken)

            val request = VolleyRequest(this@LoginActivity, object :
                CallBack {
                override fun responseCallback(response: JSONObject) {
                    Log.d(TAG,"DEV TOKEN :->    SENT!")

                }

                override fun errorCallback(error_message: VolleyError) {
                    Log.d(TAG,"ERROR : ->  "+error_message?.toString())
                    Toast.makeText(this@LoginActivity, "Try again after sometime!", Toast.LENGTH_LONG).show()
                    error_message.printStackTrace()
                }

            })
            val body = JSONObject()
            body.put("devicetoken",deviceToken)

            request.postWithBody(Constants.DEVICE_TOKEN,body,res.get("token").toString())


        })    }


}