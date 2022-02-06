package com.example.finalproject.models

class postm {
    var imgurl: String? = null
    var timestamp: String? = null

    constructor() {}
    constructor(imgurl: String?, timestamp: String?) {
        this.imgurl = imgurl
        this.timestamp = timestamp
    }
}