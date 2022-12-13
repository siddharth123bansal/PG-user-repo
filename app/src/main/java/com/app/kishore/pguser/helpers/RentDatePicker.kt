package com.app.kishore.pguser.helpers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.app.kishore.pguser.activities.DNDDialog
import java.util.*
import kotlin.reflect.KFunction2

class RentDatePicker(val setDND: KFunction2<String, String, Unit>) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
      //  Toast.makeText(context, "M : "+month+"Y : "+year , Toast.LENGTH_SHORT).show()
        val returnDate = ""+year+"-"+(month+1)+"-"+day
        Log.d("DND",returnDate)
        val dndDialog = context?.let { DNDDialog(it,returnDate,setDND) }
        dndDialog?.setCancelable(true)
        dndDialog?.show()

    }
}