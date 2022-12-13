package com.app.kishore.pguser.models

class Message {
    var message : String? = null
    var senderID : String? = null
    var senderName : String? = null
    var senderProfileImage : String? = null

    constructor(){}

    constructor(message: String?, senderID : String?){
        this.message = message
        this.senderID = senderID
    }

    constructor(message: String?, senderID : String?, senderName : String?, senderProfileImage : String?){
        this.message = message
        this.senderID = senderID
        this.senderName = senderName
        this.senderProfileImage = senderProfileImage
    }
}