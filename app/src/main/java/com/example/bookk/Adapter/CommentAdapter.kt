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
import com.example.bookk.Model.CommentModel
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Model.NoteModel
import com.example.bookk.R

class CommentAdapter(val context: Context?, var list:ArrayList<CommentModel>):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setViews(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        private var imageItem = itemView.findViewById<ImageView>(R.id.imageItem);
        private var name = itemView.findViewById<TextView>(R.id.name)
        private var comment_ = itemView.findViewById<TextView>(R.id.comment)
        private var date = itemView.findViewById<TextView>(R.id.date)

        fun setViews(comment:CommentModel){
            when(comment.Avatar){
                1->imageItem.setImageDrawable(context?.getDrawable(R.drawable.avatar_1));
                2->imageItem.setImageDrawable(context?.getDrawable(R.drawable.avatar2));
                4->imageItem.setImageDrawable(context?.getDrawable(R.drawable.avatar_4));
                4->imageItem.setImageDrawable(context?.getDrawable(R.drawable.avatar_5));
            }
            name.text = comment.User
            comment_.text = comment.Comment
            date.text = comment.Date
        }
    }
}