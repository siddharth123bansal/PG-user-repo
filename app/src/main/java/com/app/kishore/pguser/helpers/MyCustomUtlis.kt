package com.app.kishore.pguser.helpers

import android.content.Context
import android.widget.Toast
import java.util.regex.Pattern

class MyCustomUtlis {
    companion object{
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun checkEmail(context : Context,email : String) : Boolean {
            val isEmail =  EMAIL_ADDRESS_PATTERN.matcher(email).matches()
            if(!isEmail) Toast.makeText(context, "Email format incorrect", Toast.LENGTH_SHORT).show()
            return isEmail

        }
    }
}