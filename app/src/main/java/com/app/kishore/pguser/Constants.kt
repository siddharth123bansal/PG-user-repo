package com.app.kishore.pguser

class Constants {
    companion object {
        val BASE_URL = "https://backend.pgconnect.in/api/"
        val USER_LOGIN = BASE_URL+"login/user"
        val USER_SIGNUP = BASE_URL+"signup/user"
        val GET_ROOM_DETAILS = BASE_URL + "user/roomdetails"
        val GET_NOTIFICATIONS = BASE_URL+ "pgowner/getnotifications"
        val DEVICE_TOKEN = BASE_URL+"login/userdevicetoken"
        val GET_USER_PROFILE = BASE_URL+"user/profile"
        val CHANGE_PASSWORD = BASE_URL + "user/changepassword"
        val CHANGE_PROFILE_IMAGE = BASE_URL+"pgowner/changeprofileimage"
        val TOGGLE_DND = BASE_URL + "user/togglednd"
        val CHANGE_PROFILE = BASE_URL + "user/changeprofile"


    }
}