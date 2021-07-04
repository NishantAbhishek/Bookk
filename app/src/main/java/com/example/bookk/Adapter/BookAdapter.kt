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
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookk.Callbacks.ItemClick
import com.example.bookk.Helper
import com.example.bookk.Model.Books
import com.example.bookk.R

class BookAdapter(val context: Context?,var list:ArrayList<Books>,var itemClick:ItemClick):
    RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.book_item,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setViews(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var imageItem = itemView.findViewById<ImageView>(R.id.imageItem);
        var genre = itemView.findViewById<TextView>(R.id.genre);
        var name = itemView.findViewById<TextView>(R.id.name);
        var publishYear = itemView.findViewById<TextView>(R.id.publishYear);
        var reBody = itemView.findViewById<RelativeLayout>(R.id.reBody);

        fun setViews(book:Books){
            if (context != null) {
                Glide.with(context).load(book.Image).into(imageItem)
            };

            name.text = book.Name
            genre.text = book.Genre
            publishYear.text = "Published in ${book.Release}"

            reBody.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    var edit:SharedPreferences.Editor? =
                        context?.getSharedPreferences(context?.getString(R.string.cache),Context.MODE_PRIVATE)?.edit();
                    edit?.putString(context?.getString(R.string.cache_image),book.Image);
                    edit?.putInt(context?.getString(R.string.cache_popularity),book.Popularity!!)
                    edit?.putString(context?.getString(R.string.cache_bookId),book.Id)
                    edit?.putString(context?.getString(R.string.cache_commentId),book.CommentId);
                    edit?.putString(context?.getString(R.string.cache_name),book.Name);
                    edit?.putString(context?.getString(R.string.cache_genre),book.Genre);
                    edit?.putString(context?.getString(R.string.cache_language),book.Language);
                    edit?.putString(context?.getString(R.string.cache_Description),book.Description);
                    edit?.putInt(context?.getString(R.string.cache_Year),book.Release!!);
                    edit?.putInt(context?.getString(R.string.cache_rating), book.Rating!!);
                    edit?.putInt(context?.getString(R.string.cache_page), book.Page!!);
                    edit?.putInt(context?.getString(R.string.start_type), Helper.HOMESTART)
                    edit?.apply();
                    itemClick.transitionAnimation(imageItem,name,genre)
                }
            })

        }
    }
}