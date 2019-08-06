package com.example.chatapplication.models

/**
 * Created by Kush Pandya on 8/6/2019.
 */

class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long) {
    constructor() : this("","","","",-1)
}