package com.app.kishore.pguser.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.NetworkResponse
import com.android.volley.VolleyError
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.R
import com.app.kishore.pguser.helpers.CallBack
import com.app.kishore.pguser.helpers.LoadingDialog
import com.app.kishore.pguser.helpers.VolleyRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_room_form.*
import org.json.JSONObject


class UpdatePasswordFormFragment(val token : String) : BottomSheetDialogFragment() {
    private lateinit var addButton : TextView

    val TAG = "ALLROOM"
    var RES_CODE = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_form, container, false)

        addButton = view.findViewById(R.id.btnAdd)


        view.findViewById<ImageView>(R.id.back_btn).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                this@UpdatePasswordFormFragment.dismiss()
            }
        })

        addButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                
                val oldPassword = evOldPassword.text.toString().trim()
                val newPassword = evNewPassword.text.toString().trim()
                val confirmNewPassword = evConfirmNewPassword.text.toString().trim()

                if(oldPassword.length != 0 && newPassword.length !=0 && confirmNewPassword.length != 0){

                    if(newPassword.equals(confirmNewPassword)){
                        val loadingDialog = context?.let { LoadingDialog(it) }
                        loadingDialog?.setCancelable(false)
                        loadingDialog?.show()

                        val request = VolleyRequest(context, object :
                            CallBack {
                            override fun responseCallback(response: JSONObject) {
                                loadingDialog?.dismiss()
                                Toast.makeText(context, "Password updated!", Toast.LENGTH_SHORT).show()
                                this@UpdatePasswordFormFragment.dismiss()

                            }


                            override fun errorCallback(error_message: VolleyError) {
                                loadingDialog?.dismiss()
                                Log.d(TAG,"ERROR : ->  "+error_message?.toString())
                                var jsonError: String? = ""
                                val networkResponse: NetworkResponse = error_message.networkResponse
                                if (networkResponse != null && networkResponse.data != null) {
                                    jsonError = String(networkResponse.data)
                                    val errorJson = JSONObject(jsonError)
                                    try {
                                        Toast.makeText(
                                            context,
                                            errorJson.getString("error"),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "An error occurred!Please try again later.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "An error occurred!Please try again later.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })

                        val bodyData = JSONObject()
                        bodyData.put("oldpassword",oldPassword)
                        bodyData.put("password",newPassword)

                        request.putWithBody(Constants.CHANGE_PASSWORD,bodyData,token)


                    }else Toast.makeText(context, "Password mismatch!", Toast.LENGTH_SHORT).show()


                    
                }
    


            }
        })

        return view
    }


}