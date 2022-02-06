package com.example.finalproject.models

class Message {
    var msgid: String? = null
    var msg: String? = null
    var senderid: String? = null
    var timestamp: String? = null
    var feeling = -1

    constructor() {}
    constructor(msg: String?, senderid: String?, timestamp: String?) {
        this.msg = msg
        this.senderid = senderid
        this.timestamp = timestamp
    }
}