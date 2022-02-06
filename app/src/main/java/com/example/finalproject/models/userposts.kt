package com.example.finalproject.models

class userposts {
    var name: String? = null
    var profileimg: String? = null
    var post: String? = null
    var time: String? = null
    var uid: String? = null
    var key: String? = null

    constructor() {}
    constructor(
        name: String?,
        profileimg: String?,
        post: String?,
        time: String?,
        uid: String?,
        key: String?
    ) {
        this.name = name
        this.profileimg = profileimg
        this.post = post
        this.uid = uid
        this.time = time
        this.key = key
    }
}