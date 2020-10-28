package com.hms.demo.hquicnews

import java.text.SimpleDateFormat
import java.util.*

data class Article(val author:String,
                   val title:String,
                   val description:String,
                   val url:String,
                   val _time:String){
    val time:String
        get() {
            val date= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(_time)
            return date?.toString() ?: ""
        }
}

