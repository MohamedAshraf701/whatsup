package com.example.finalproject.models

class User {
    var uid: String? = null
    var username: String? = null
    var profilepicture: String? = null
    var status: String? = null
    var phonenumber: String? = null
    var lastseen: String? = null

    constructor(
        uid: String?,
        username: String?,
        profilepicture: String?,
        status: String?,
        phonenumber: String?,
        lastseen: String?
    ) {
        this.uid = uid
        this.username = username
        this.profilepicture = profilepicture
        this.status = status
        this.phonenumber = phonenumber
        this.lastseen = lastseen
    }

    constructor() {}
}