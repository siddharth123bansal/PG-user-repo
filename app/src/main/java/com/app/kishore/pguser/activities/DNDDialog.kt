package com.app.kishore.pguser.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.kishore.pguser.R
import kotlinx.android.synthetic.main.activity_dnddialog.*
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class DNDDialog(context: Context,val returnDate: String, val setDND: KFunction2<String, String, Unit>) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dnddialog)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        
        dndOn.setOnClickListener{
            val meal = when(mealRG.checkedRadioButtonId){
                R.id.lunchRB -> "lunch"
                R.id.dinnerRB -> "dinner"
                R.id.tiffinRB -> "tiffin"
                else -> ""
            }
            if(meal.length != 0){
                setDND(returnDate,meal)
                this.dismiss()
            }else Toast.makeText(context, "Please select a meal!", Toast.LENGTH_SHORT).show()
        }

    }
}