package com.sumeet.insta_fireclone.models

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("uid") val uid:String = "",
    @PropertyName("username") val username:String = ""
)
