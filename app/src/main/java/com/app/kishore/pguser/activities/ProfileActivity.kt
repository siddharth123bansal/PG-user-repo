package com.app.kishore.pguser.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.app.kishore.pguser.Constants
import com.app.kishore.pguser.EncryptDecrypt
import com.app.kishore.pguser.R
import com.app.kishore.pguser.authActivities.LoginActivity
import com.app.kishore.pguser.fragments.UpdatePasswordFormFragment
import com.app.kishore.pguser.helpers.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.lang.Byte.decode
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class ProfileActivity : AppCompatActivity() {

    private lateinit var logoutBtn: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var profilePic: CircleImageView
    private lateinit var userEmail: EditText
    private lateinit var userPhone: EditText
    private lateinit var mGetContent: ActivityResultLauncher<String>
    private lateinit var mGetCon: ActivityResultLauncher<String>
    private lateinit var resultUri: Uri
    val TAG = "PROFILE"
    val ips=100
    private var imageUri: Uri? = null
    var RES_CODE = - 1
    private lateinit var user: JSONObject
    private lateinit var token: String
    private lateinit var bitmap: Bitmap
    private var imageRef="idProof"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        user = JSONObject(intent.getStringExtra("USER"))


        if (user.has("alternatenumber"))
            evAltNumber.setText(user.getString("alternatenumber"))
        if (user.has("education"))
            evEducation.setText(user.getString("education"))
        if (user.has("homeplace"))
            evHomeAddress.setText(user.getString("homeplace"))
        if (user.has("officehoursstart"))
            evOfficeStart.setText(user.getString("officehoursstart"))
        if (user.has("officehoursend"))
            evOfficeEnds.setText(user.getString("officehoursend"))
        if (user.has("aadharnumber")) {
            val d=decrypt((user.getString("aadharnumber")))
            if(d.toString().equals("null"))adhaarcard.setText("")
            else adhaarcard.setText(d.toString())
        }else{
            adhaarcard.setText("")
        }
        if (user.has("idproof"))idproof.setText(user.getString("idproof"))


        idproof.setOnClickListener {
            mGetCon = registerForActivityResult(ActivityResultContracts.GetContent())
            { uri: Uri? ->
                // Handle the returned Uri
                val i = Intent(this@ProfileActivity, CropperActivity::class.java)
                i.putExtra("IMGUR", uri.toString())
                idproof.setText(uri.toString())
                startActivityForResult(i, 101)
            }

        }
        val adhaar = adhaarcard.text.toString().trim()
        //algorithm: String?, input: String, key: SecretKey?,iv: IvParameterSpec?
        //val enc=encrypt(Algo,adhaar.toString(), key = Key,)
        //Log.d("asd expo",enc.toString())


        if (user.has("gender")) {
            when (user.getString("gender")) {
                "Male" -> genderRG.check(R.id.maleRB)
                "Female" -> genderRG.check(R.id.femaleRB)
            }
        }

        if (user.has("foodtype")) {
            when (user.getString("foodtype")) {
                "veg" -> foodRG.check(R.id.vegRB)
                "nonveg" -> foodRG.check(R.id.nonvegRB)
            }
        }




        toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)

        logoutBtn = findViewById(R.id.Logout)
        userEmail = findViewById(R.id.username_input)
        userPhone = findViewById(R.id.phone_input)
        profilePic = findViewById(R.id.receiver_profile_image)

        userEmail.isEnabled = false
        userPhone.isEnabled = false

        profileHeading.text = "Profile Id : " + user.getString("profileid").toString().uppercase()

        findViewById<ImageView>(R.id.uploadProfileImage).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(p0: View?) {
                //Toast.makeText(this@ProfileActivity, "Up Img", Toast.LENGTH_SHORT).show()
                mGetContent.launch("image/*")

            }
        }

        )

        mGetContent = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            // Handle the returned Uri
            val i = Intent(this@ProfileActivity, CropperActivity::class.java)
            i.putExtra("IMGURI", uri.toString())
            startActivityForResult(i, 101)
        }

        findViewById<ImageView>(R.id.back_btn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val i = Intent(this@ProfileActivity, HomeActivity::class.java)
                i.putExtra("USER", user.toString())
                startActivity(i)
                finish()
            }
        }

        )

        logoutBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Utils.deleteUser(this@ProfileActivity)
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
                finishAffinity()
            }
        }

        )

        dndSwitch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (dndSwitch.isChecked) {
                    Toast.makeText(this@ProfileActivity, "Select date of return", Toast.LENGTH_LONG)
                        .show()
                    val newFragment = RentDatePicker(::setDND)
                    newFragment.show(supportFragmentManager, "datePicker")
                } else {
                    setDND("", "")
                }

            }
        })

        changePasswordButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val changePasswordSheet = UpdatePasswordFormFragment(user.getString("token"))
                changePasswordSheet.show(supportFragmentManager, changePasswordSheet.TAG)
            }
        })

        updateChanges.setOnClickListener {
            updateProfile()
        }


        userEmail.setText(user.get("email").toString())
        userPhone.setText(user.get("phone").toString())
        Glide.with(this@ProfileActivity).load(user.get("profileimage"))
            .error(R.drawable.user)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic);
        token = user.get("token").toString()
        dndSwitch.isChecked = user.getBoolean("dnd")


    }




    private fun updateProfile() {
        val gender = when (genderRG.checkedRadioButtonId) {
            R.id.maleRB -> "Male"
            R.id.femaleRB -> "Female"
            else -> ""
        }

        val altNum = evAltNumber.text.toString().trim()

        val food = when (foodRG.checkedRadioButtonId) {
            R.id.vegRB -> "veg"
            R.id.nonvegRB -> "nonveg"
            else -> ""
        }

        val officeStart = evOfficeStart.text.toString().trim()
        val officeEnd = evOfficeEnds.text.toString().trim()
        val homeplace = evHomeAddress.text.toString().trim()
        val education = evEducation.text.toString().trim()
        val adhaar = adhaarcard.text.toString().trim()

        val enc=encrypt(adhaar.toString())
        Log.d("Hello",enc.toString())
        val dec=decrypt(enc.toString())
        Log.d("Hello",dec.toString())



        val id = idproof.text.toString().trim()

        if (gender.length != 0 &&
            altNum.length != 0 &&
            food.length != 0 &&
            officeStart.length != 0 &&
            officeEnd.length != 0 &&
            homeplace.length != 0 &&
            education.length != 0 &&
            adhaar.length != 0
        ) {

            val loading = LoadingDialog(this)
            loading.setCancelable(false)
            loading.show()

            val request = VolleyRequest(this, object :
                CallBack {
                override fun responseCallback(response: JSONObject) {
                    user.put("gender", gender)
                    user.put("alternatenumber", altNum)
                    user.put("foodtype", food)
                    user.put("officehoursstart", officeStart)
                    user.put("officehoursend", officeEnd)
                    user.put("homeplace", homeplace)
                    user.put("education", education)
                    user.put("aadharnumber",enc.toString())
                    user.put("idproof", id.toString())
                    Utils.saveUser(this@ProfileActivity, user.toString())
                    loading.cancel()
                    Toast.makeText(this@ProfileActivity, "Changes updated 👍 ", Toast.LENGTH_SHORT)
                        .show()
                }


                override fun errorCallback(error_message: VolleyError) {
                    loading.cancel()
                    var jsonError: String? = ""
                    val networkResponse: NetworkResponse = error_message.networkResponse
                    if (networkResponse != null && networkResponse.data != null) {
                        jsonError = String(networkResponse.data)
                        val errorJson = JSONObject(jsonError)
                        try {
                            Toast.makeText(
                                this@ProfileActivity,
                                errorJson.getString("error"),
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ProfileActivity,
                                errorJson.getString("An error occurred!Please try again later."),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            })

            val body = JSONObject()
            body.put("gender", gender)
            body.put("alternatenumber", altNum)
            body.put("foodtype", food)
            body.put("officehoursstart", officeStart)
            body.put("officehoursend", officeEnd)
            body.put("homeplace", homeplace)
            body.put("education", education)
            body.put("aadharnumber", adhaarcard.text.toString())
            body.put("idproof", idproof.text.toString())
            request.putWithBody(Constants.CHANGE_PROFILE, body, user.getString("token"))
        } else Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show()

    }

    private fun setDND(returnDate: String, meal: String) {
        val loading = LoadingDialog(this)
        loading.setCancelable(false)
        loading.show()
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                val status = response.get("dnd") as Boolean
                dndSwitch.isChecked = status
                user.put("dnd", status)
                Utils.saveUser(this@ProfileActivity, user.toString())

                if (status)
                    Toast.makeText(this@ProfileActivity, "DND turned ON!", Toast.LENGTH_SHORT)
                        .show()
                else Toast.makeText(this@ProfileActivity, "DND turned OFF!", Toast.LENGTH_SHORT)
                    .show()
                loading.cancel()
            }


            override fun errorCallback(error_message: VolleyError) {
                loading.cancel()
                var jsonError: String? = ""
                val networkResponse: NetworkResponse = error_message.networkResponse
                if (networkResponse != null && networkResponse.data != null) {
                    jsonError = String(networkResponse.data)
                    val errorJson = JSONObject(jsonError)
                    try {
                        Toast.makeText(
                            this@ProfileActivity,
                            errorJson.getString("error"),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ProfileActivity,
                            errorJson.getString("An error occurred!Please try again later."),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })

        val body = JSONObject()
        body.put("expectedreturndate", returnDate)
        body.put("returningmeal", meal)


        if (returnDate.length != 0)
            request.putWithBody(Constants.TOGGLE_DND, body, user.getString("token"))
        else request.putWithBody(Constants.TOGGLE_DND, null, user.getString("token"))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == - 1 && requestCode == 101) {
            val result = data?.getStringExtra("RESULT")

            if (result != null) {
                resultUri = Uri.parse(result)
                Glide.with(this@ProfileActivity).load(resultUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic);
                val inputStream = this.contentResolver?.openInputStream(resultUri)
                bitmap = BitmapFactory.decodeStream(inputStream)
                uploadBitmap(bitmap)
            }

        }
    }

    private fun uploadBitmap(bitmap: Bitmap) {
        val loading = LoadingDialog(this)
        loading.setCancelable(false)
        loading.show()
        val volleyMultipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(
            Method.PUT, Constants.CHANGE_PROFILE_IMAGE,
            object : Response.Listener<NetworkResponse?> {
                override fun onResponse(response: NetworkResponse?) {
                    updateProfilePic()
                    loading.cancel()

                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    loading.cancel()
                    Toast.makeText(this@ProfileActivity, "Try again later!", Toast.LENGTH_SHORT)
                        .show()
//                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
//                    Log.e("GotError", "" + error.message)
                }
            }) {
            override fun getByteData(): Map<String, DataPart> {
                val params: MutableMap<String, DataPart> = HashMap()
                val imagename = System.currentTimeMillis()
                params["profileimage"] = DataPart("$imagename.png", getFileDataFromDrawable(bitmap))
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = java.util.HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        volleyMultipartRequest.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest)
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    fun updateProfilePic() {
        val request = VolleyRequest(this, object :
            CallBack {
            override fun responseCallback(response: JSONObject) {
                user.put("profileimage", response.getString("profileimage"))
                Utils.saveUser(this@ProfileActivity, user.toString())
            }


            override fun errorCallback(error_message: VolleyError) {
                loading.visibility = View.GONE
                btnLogin.isEnabled = true
                Log.d(TAG, "ERROR : ->  " + error_message?.toString())
                Toast.makeText(this@ProfileActivity, "Profile not found!", Toast.LENGTH_LONG).show()
                error_message.printStackTrace()
            }

        })

        request.getRequest(Constants.GET_USER_PROFILE, user.getString("token"))

    }

         val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
         val salt = "QWlGNHNhMTJTQWZ2bGhpV3U=" // base64 decode => AiF4sa12SAfvlhiWu
         val iv = "bVQzNFNhRkQ1Njc4UUFaWA==" // base64 decode => mT34SaFD5678QAZX
        fun encrypt(strToEncrypt: String) :  String?
        {
            try
            {
                val ivParameterSpec = IvParameterSpec(android.util.Base64.decode(iv, android.util.Base64.DEFAULT))

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val spec =  PBEKeySpec(secretKey.toCharArray(), android.util.Base64.decode(salt, android.util.Base64.DEFAULT), 10000, 256)
                val tmp = factory.generateSecret(spec)
                val secretKey =  SecretKeySpec(tmp.encoded, "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
                return android.util.Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), android.util.Base64.DEFAULT)
            }
            catch (e: Exception)
            {
                println("Error while encrypting: $e")
            }
            return null
        }

        fun decrypt(strToDecrypt : String) : String? {
            try
            {

                val ivParameterSpec =  IvParameterSpec(android.util.Base64.decode(iv, android.util.Base64.DEFAULT))

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val spec =  PBEKeySpec(secretKey.toCharArray(), android.util.Base64.decode(salt, android.util.Base64.DEFAULT), 10000, 256)
                val tmp = factory.generateSecret(spec);
                val secretKey =  SecretKeySpec(tmp.encoded, "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
                return  String(cipher.doFinal(android.util.Base64.decode(strToDecrypt, android.util.Base64.DEFAULT)))
            }
            catch (e : Exception) {
                println("Error while decrypting: $e");
            }
            return null
        }
    }