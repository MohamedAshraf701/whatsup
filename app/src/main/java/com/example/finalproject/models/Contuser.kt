package com.example.finalproject.models

class Contuser {
    var uid: String? = null
    var username: String? = null
    var profilepicture: String? = null
    var phonenumber: String? = null
    var lastseen: String? = null
    var status: String? = null

    constructor() {}
    constructor(
        uid: String?,
        username: String?,
        profilepicture: String?,
        phonenumber: String?,
        lastseen: String?,
        status: String?
    ) {
        this.uid = uid
        this.username = username
        this.profilepicture = profilepicture
        this.phonenumber = phonenumber
        this.lastseen = lastseen
        this.status = status
    }
}