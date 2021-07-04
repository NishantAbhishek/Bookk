package com.example.bookk.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookk.Callbacks.FavoriteCallback
import com.example.bookk.Callbacks.NoteCallback
import com.example.bookk.Helper
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Model.NoteModel
import com.example.bookk.R

class NoteAdapter(val context: Context?, var list:ArrayList<NoteModel>,var noteCallback: NoteCallback):
    RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setViews(list[position])
    }

    override fun getItemCount(): Int {
        Log.e("Size",list?.size.toString())
        return list.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var imageItem = itemView.findViewById<ImageView>(R.id.imageItem);
        var name = itemView.findViewById<TextView>(R.id.name);
        var edit = itemView.findViewById<ImageView>(R.id.edit)
        var delete = itemView.findViewById<ImageView>(R.id.delete);
        var note_ = itemView.findViewById<TextView>(R.id.note)
        fun setViews(note:NoteModel){
            if (context != null) {
                Glide.with(context).load(note.Image).into(imageItem)
            };
            name.text = note.Name
            note_.text = note.Note
            edit.setOnClickListener {noteCallback.editNote(note)}
            delete.setOnClickListener {noteCallback.deleteNote(note)}
        }
    }
}