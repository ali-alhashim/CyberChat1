package com.example.cyberchat1.model

class MessagesModel
    (
    var from:String,
    var fromUID : String,
    var message:String,
    var to:String,
    var toUID: String,
    var messageId:String,
    var time:String,
    var date:String,
    var fileName:String,
    var status:String     // seen , sent , delivered , deleted , like
    )
