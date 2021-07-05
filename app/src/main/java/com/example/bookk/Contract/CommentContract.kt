package com.example.bookk.Contract

import android.view.View
import com.example.bookk.Model.CommentModel

class CommentContract {
    interface View{
        fun putData(list:ArrayList<CommentModel>)
        fun showSnackBar(message:String)
        fun getComment():String;
        fun setViews(link:String,name:String)
        fun removeEditComment()
    }
    interface Presenter{
        fun loadData()
        fun onClick(v:android.view.View)
    }
}