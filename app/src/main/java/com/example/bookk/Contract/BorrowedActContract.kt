package com.example.bookk.Contract

import com.example.bookk.Model.BorrowedModel

interface BorrowedActContract {

    interface View{
        fun showSnackBar(message:String)
        fun removelastData()
        fun putBook(book:ArrayList<BorrowedModel>)
    }

    interface Presenter{
        fun getAllData()
        fun deleteBorrowedBook(book: BorrowedModel, size: Int)
    }
}