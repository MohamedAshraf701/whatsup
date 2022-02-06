package com.example.finalproject.models

import com.example.finalproject.models.Statusm
import java.util.ArrayList

class Userstatusm {
    var name: String? = null
    var profileimg: String? = null
    var lastupdate: String? = null
    var statuses: ArrayList<Statusm>? = null

    constructor() {}
    constructor(
        name: String?,
        profileimg: String?,
        lastupdate: String?,
        statuses: ArrayList<Statusm>?
    ) {
        this.name = name
        this.profileimg = profileimg
        this.lastupdate = lastupdate
        this.statuses = statuses
    }
}