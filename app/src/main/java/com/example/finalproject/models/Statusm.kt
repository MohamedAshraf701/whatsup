package com.example.finalproject.models

class Statusm {
    var imgurl: String? = null
    var timestamp: String? = null
    var key: String? = null

    constructor() {}
    constructor(imgurl: String?, timestamp: String?, key: String?) {
        this.imgurl = imgurl
        this.timestamp = timestamp
        this.key = key
    }
}