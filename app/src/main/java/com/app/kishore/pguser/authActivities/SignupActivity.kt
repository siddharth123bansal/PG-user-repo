package com.app.kishore.pguser.authActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.activities.HomeActivity
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.MyCustomUtlis
import com.app.kishore.pguser.helpers.Utils
import com.app.kishore.pguser.helpers.VolleyRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.loading
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    private val TAG = "SIGNUP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val textPP =
            "<font color=#000F08>By signing up you accept the </font> <font color=#1B5299>Terms of Service</font><font color=#000F08> and </font><font color=#1B5299> Privacy Policy</font>"
        terms.setText(Html.fromHtml(textPP))

        val textSign =
            "<font color=#000F08>Already have an account? </font> <font color=#1B5299>Log in</font>"
        loginButton.setText(Html.fromHtml(textSign))

        loginButton.isEnabled = true
        loginButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                loginButton.isEnabled = false
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                finish()
            }
        })


        btnSignup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val userPhone = evPhone.text.toString().trim()
                val userEmail = evEmail.text.toString().trim()
                val userPassword = evChoosePassword.text.toString().trim()
                val userConfirmPass = evConfirmPassword.text.toString().trim()
                val userName = evName.text.toString().trim()

                if (userName.length != 0 && userPhone.length == 10 && MyCustomUtlis.checkEmail(
                        this@SignupActivity,
                        userEmail
                    ) && userPassword.length != 0 && userConfirmPass.length != 0
                ) {
                    if (userPassword.equals(userConfirmPass)) {
                        btnSignup.isEnabled = false
                        // Signup logic
                        processSignup(userEmail, userPassword, userName, userPhone)


                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "Password mismatch!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@SignupActivity,
                    "Invalid Credentials!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })
    }


    private fun processSignup(
        userEmail: String,
        userPassword: String,
        userName: String,
        userPhone: String
    ) {
        loading.visibility = View.VISIBLE
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                Toast.makeText(this@SignupActivity, "Account created successfully! Please login.", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
//                fetchUserProfile(
//                    (response.get("user") as JSONObject).get("token").toString(),
//                    (response.get("user") as JSONObject).get("id").toString()
//                )
            }

            override fun errorCallback(error_message: VolleyError) {
                loading.visibility = View.GONE
                btnSignup.isEnabled = true
                var jsonError: String? = ""
                val networkResponse: NetworkResponse = error_message.networkResponse
                if (networkResponse != null && networkResponse.data != null) {
                    jsonError = String(networkResponse.data)
                    val errorJson = JSONObject(jsonError)
                    try {
                        Toast.makeText(
                            this@SignupActivity,
                            errorJson.getString("error"),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@SignupActivity,
                            "An error occurred!Please try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        "An error occurred!Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })


        val bodyData = JSONObject()
        bodyData.put("email", userEmail)
        bodyData.put("password", userPassword)
        bodyData.put("name", userName)
        bodyData.put("phone", userPhone)


        request.postWithBody(Constants.USER_SIGNUP, bodyData, "")


    }

    fun fetchUserProfile(userToken: String, userID: String) {
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                val userObj = response as JSONObject
                userObj.put("token", userToken)
                userObj.put("_id", userID)
                Log.d(TAG, userObj.toString())

                Utils.saveUser(this@SignupActivity, userObj.toString())

                val i = Intent(this@SignupActivity, HomeActivity::class.java)
                i.putExtra("USER", userObj.toString())
                startActivity(i)
                finish()

                loading.visibility = View.GONE


            }


            override fun errorCallback(error_message: VolleyError) {
                loading.visibility = View.GONE
                btnLogin.isEnabled = true
                var jsonError: String? = ""
                val networkResponse: NetworkResponse = error_message.networkResponse
                if (networkResponse != null && networkResponse.data != null) {
                    jsonError = String(networkResponse.data)
                    val errorJson = JSONObject(jsonError)
                    try {
                        Toast.makeText(
                            this@SignupActivity,
                            errorJson.getString("error"),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@SignupActivity,
                            "An error occurred!Please try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        "An error occurred!Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

        request.getRequest(Constants.GET_USER_PROFILE, userToken)
    }

}