package com.example.bookk.Contract

import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Model.NoteModel

interface NoteContract {
    interface View{
        fun addNote(note: ArrayList<NoteModel>)
        fun showSnackBar(message: String)
        fun removelastData()
    }

    interface Presenter{
        fun getAllNotes()
        fun deleteNote(note:NoteModel,size:Int);
        fun editNote(note:NoteModel);
    }
}