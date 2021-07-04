package com.example.bookk.Callbacks

import android.widget.ImageView
import android.widget.TextView
import com.example.bookk.Model.NoteModel

interface ItemClick {
    fun transitionAnimation(imageItem:ImageView,name:TextView,genre:TextView);
}

interface FavoriteCallback{
    fun deleteFavorite(favoriteId:String?)
    fun openBookDetailAct(imageItem: ImageView, name: TextView, genre: TextView);
}
interface NoteCallback{
    fun editNote(note:NoteModel);
    fun deleteNote(note:NoteModel);
}

