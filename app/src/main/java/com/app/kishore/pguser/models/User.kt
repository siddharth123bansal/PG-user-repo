package com.app.kishore.pguser.models

import org.json.JSONObject

class User {
     var _id : String? = null
     var email : String? = null
     var usertype : String? = null
     var token : String? = null
     var name : String? = null

    constructor(_id : String?, email : String?, usertype : String?, name : String?){
        this._id = _id
        this.email = email
        this.token = token
        this.name = name
        this.usertype = usertype

    }

}