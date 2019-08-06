package com.example.chatapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Kush Pandya on 8/5/2019.
 */

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String) : Parcelable {
    constructor() : this("", "", "")
}