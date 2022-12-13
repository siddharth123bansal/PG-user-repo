package com.app.kishore.pguser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.kishore.pguser.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_pginfo.*
import kotlinx.android.synthetic.main.activity_pginfo.pgbanner_imageView
import kotlinx.android.synthetic.main.activity_pginfo.tvPGEmail
import kotlinx.android.synthetic.main.activity_pginfo.tvPGName
import kotlinx.android.synthetic.main.activity_pginfo.tvPGOwnerName
import kotlinx.android.synthetic.main.activity_pginfo.tvPGPhone
import kotlinx.android.synthetic.main.activity_pginfo.tvPGaddress
import org.json.JSONObject

class PGInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pginfo)

        val pgownerObj = JSONObject(intent.getStringExtra("PGOWNER"))


        if (pgownerObj.has("pgname"))
        tvPGName.setText(pgownerObj.getString("pgname"))
        if (pgownerObj.has("email"))
        tvPGEmail.setText(pgownerObj.getString("email"))
        if (pgownerObj.has("phone"))
        tvPGPhone.setText(pgownerObj.getString("phone"))
        if (pgownerObj.has("pgaddress"))
        tvPGaddress.setText(pgownerObj.getString("pgaddress"))
        if (pgownerObj.has("name"))
        tvPGOwnerName.setText(pgownerObj.getString("name"))

        if (pgownerObj.has("pgtype"))
            tvPGType.setText(pgownerObj.getString("pgtype"))

        if (pgownerObj.has("food"))
            tvPGFood.setText(pgownerObj.getString("food")+" times")

        if (pgownerObj.has("coverimage"))
        Glide.with(this).load(pgownerObj.getString("coverimage"))
            .into(pgbanner_imageView)
        else  Glide.with(this).load(R.drawable.banner)
            .into(pgbanner_imageView)

        if(pgownerObj.has("roomsharingoptions")){
            var roomShare = ""
            val ob = pgownerObj.getJSONObject("roomsharingoptions")
            if(ob.getBoolean("one")) roomShare += "One, "
            if(ob.getBoolean("two")) roomShare += "Two, "
            if(ob.getBoolean("three")) roomShare += "Three, "
            if(ob.getBoolean("four")) roomShare += "Four"

            tvShareOpt.setText(roomShare)

        }


        if(pgownerObj.has("rules")){
            var roomShare = ""
            val ob = pgownerObj.getJSONObject("rules")
            if(!ob.getBoolean("novisitors")) tvVisitors.setText("Visitors : Allowed")
            if(!ob.getBoolean("noparties"))  tvParty.setText("Parties : Allowed")
            if(!ob.getBoolean("nosmoking"))  tvSmoking.setText("Smoking : Allowed")

        }

        if(pgownerObj.has("facilities")){
            var facilities = ""
            val ob = pgownerObj.getJSONObject("facilities")
            if(ob.getBoolean("lift")) facilities += "Lift, "
            if(ob.getBoolean("gym")) facilities += "Gym, "
            if(ob.getBoolean("washingmachine")) facilities += "Washing Machine, "
            if(ob.getBoolean("customcooking")) facilities += "Custom Cooking, "
            if(ob.getBoolean("wifi")) facilities += "Wifi, "
            if(ob.getBoolean("parkingspace")) facilities += "Parking space, "
            if(ob.getBoolean("hotwater")) facilities += "Hotwater, "
            if(ob.getBoolean("alternativeroomcleaning")) facilities += "Alternative room cleaning, "
            if(ob.getBoolean("fridge")) facilities += "Fridge, "
            if(ob.getBoolean("dailyroomcleaning")) facilities += "Daily room cleaning, "
            if(ob.getBoolean("cctv")) facilities += "CCTV, "
            if(ob.getBoolean("gamingroom")) facilities += "Gaming room, "
            if(ob.getBoolean("smarttv")) facilities += "Smart TV, "
            if(ob.getBoolean("studychairs")) facilities += "Study chairs, "

            facilitiesTv.setText(facilities)

        }





        back_btn.setOnClickListener{
            finish()
        }
    }
}