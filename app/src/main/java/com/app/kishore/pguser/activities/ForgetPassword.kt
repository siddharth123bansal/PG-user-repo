package com.app.kishore.pguser.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.authActivities.LoginActivity
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.VolleyRequest
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_forget_password.loading
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        if(forgetEmail.text.toString().trim()!=null){
            val email=forgetEmail.text.toString().trim()
            btnsendotp.setOnClickListener {
                sendotp()
            }

        }
        else{
            Toast.makeText(this,"Email is required to send OTP",Toast.LENGTH_LONG).show()
        }
    }

    fun sendotp(){
        loading.visibility=View.VISIBLE
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                val res=response.getString("message")
                Log.d("msgsa",res.toString())
                Toast.makeText(this@ForgetPassword,res.toString(),Toast.LENGTH_LONG).show()
                if(res.toString().equals("OTP sent successfully")) {
                    val i = Intent(this@ForgetPassword, EnterOTP::class.java)
                    i.putExtra("email", forgetEmail.text.toString())
                    loading.visibility=View.GONE
                    startActivity(i)
                }
            }
            override fun errorCallback(error_message: VolleyError) {
                    Toast.makeText(this@ForgetPassword,
                        "An error occurred!Please try again later.",
                        Toast.LENGTH_SHORT).show()
                loading.visibility=View.GONE

            }
        })
        val bodyData = JSONObject()
        bodyData.put("email", forgetEmail.text.toString().trim().toString())
        request.postWithBody(Constants.reqotp, bodyData, "")
    }
}