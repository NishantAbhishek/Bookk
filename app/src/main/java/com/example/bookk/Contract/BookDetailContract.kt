package com.example.bookk.Contract

import android.view.View
import com.example.bookk.Model.Books

interface BookDetailContract{
    interface View{
        fun scrollTop();
        fun scrollBottom();
        fun showSnackBar(message: String)
        fun setBookDetail(book:Books,startType:Int);
        fun startCommentAct()
    }

    interface Presenter{
        fun onClick(v:android.view.View?)
        fun getBookObject()
    }


}