package com.example.bookk.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookk.Model.Books
import com.example.bookk.R

class BookAdapter(val context: Context?,var list:ArrayList<Books>):
    RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.book_item,parent,false);
        return BookAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (context != null) {
            Glide.with(context).load(list[position].Image).into(holder.imageItem)
        };
        holder.name.text = list[position].Name
        holder.genre.text = list[position].Genre
        holder.publishYear.text = "Published in ${list[position].Release}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var imageItem = itemView.findViewById<ImageView>(R.id.imageItem);
        var genre = itemView.findViewById<TextView>(R.id.genre);
        var name = itemView.findViewById<TextView>(R.id.name);
        var publishYear = itemView.findViewById<TextView>(R.id.publishYear);


    }
}