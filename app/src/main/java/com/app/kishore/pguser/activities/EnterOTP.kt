package com.app.kishore.pguser.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.authActivities.LoginActivity
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.VolleyRequest
import kotlinx.android.synthetic.main.activity_enter_otp.*
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class EnterOTP : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_otp)
        val email=intent.getStringExtra("email")
        againOTP.setOnClickListener {
            resendotp()
        }

        btnotp.setOnClickListener{
            val Otp =otp.text.toString().trim()
            val pass=newPass.text.toString().trim()
            if(Otp.length!=0 && pass.length!=0){
                    sendreq()
            }
            else{
                Toast.makeText(this,"All fiels are req",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resendotp()
    {






    }

    private fun sendreq() {
        loadings.visibility = View.VISIBLE

        val request = VolleyRequest(this,object : CallBack {
            override fun responseCallback(response: JSONObject?) {
                val res= JSONObject(response.toString())
                val msg=res.getString("message")
                Log.d("asds",msg.toString())
                if(msg.toString().equals("Password changed successfully")){

                    Toast.makeText(
                        this@EnterOTP,
                        "Password changed successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    loadings.visibility = View.GONE
                    startActivity(Intent(this@EnterOTP,LoginActivity::class.java))

                }
            }

            override fun errorCallback(error_message: VolleyError?) {

                Toast.makeText(this@EnterOTP,"Error",Toast.LENGTH_LONG).show()
                loadings.visibility = View.GONE
                Log.d("asds","error")
            }
        })


        val bodyData = JSONObject()
        bodyData.put("email",intent.getStringExtra("email").toString().trim())
        bodyData.put("password",newPass.text.toString().trim())
        bodyData.put("otp",otp.text.toString().trim())
        request.putWithBody(Constants.changepOTP,bodyData,"")
    }

}














