package com.example.bookk.Contract

import android.view.View
import com.example.bookk.Model.Books

interface HomeContract {
    interface View{
        fun putUnderLine(uderlineIndex:Int);
        fun addBooks(books:ArrayList<Books>);
    }

    interface Presenter{
        fun getUnderLinesViews(view:android.view.View?):Array<android.view.View>
        fun onClick(view: android.view.View?);
        fun getAllBooks();
    }

}

