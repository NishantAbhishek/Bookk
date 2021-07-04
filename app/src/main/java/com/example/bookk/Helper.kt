package com.example.bookk

import com.example.bookk.Callbacks.EmailVerifiedCallback
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object{
        var monthArray = arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec")
        var HOMESTART = 1;
        var FAVORATESTART = 2;
        fun convertMillisToDate(time:Long):String{
            var calendar = Calendar.getInstance();
            calendar.timeInMillis = time;

            val dateFormat: DateFormat = SimpleDateFormat("dd-MMM-yy hh:mm a")
            return dateFormat.format(Date()).toString()
        }

        fun genreToNode(genre: String?): String {
            return when (genre) {
                "Comedy" -> "ComedyGenre"
                "Fantasy" -> "FantasyGenre"
                "Horror" -> "Horror"
                "Mystery" -> "MysteryGenre"
                "Drama" -> "DramaGenre"
                "SciFi" -> "ScienceFictionGenre"
                else -> "ScienceFictionGenre"
            }
        }


        fun isEmailVerified(emailVerifiedCallback: EmailVerifiedCallback) {
            if (getUid() != null) {
                if (!FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    FirebaseAuth.getInstance().currentUser?.
                    reload()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            emailVerifiedCallback.isEmailVerified(FirebaseAuth.getInstance().currentUser!!.isEmailVerified)
                        }
                    }
                }else{
                    emailVerifiedCallback.isEmailVerified(true)
                }
            }else{
                emailVerifiedCallback.isEmailVerified(false)
            }
        }

        fun getUid(): String? {
            return FirebaseAuth.getInstance().currentUser?.uid
        }

        fun isEmailVerified():Boolean{
            return if(getUid()!=null){
                FirebaseAuth.getInstance().currentUser!!.isEmailVerified;
            }else{
                false;
            }
        }

        fun reloadUser(){
            if(getUid()!=null){
                FirebaseAuth.getInstance().currentUser!!.reload()
            }
        }

    }
}