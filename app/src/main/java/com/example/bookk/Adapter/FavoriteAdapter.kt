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
import com.example.bookk.Helper
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.R

class FavoriteAdapter(val context: Context?, var list:ArrayList<FavoriteModel>,var favoriteCallback: FavoriteCallback):
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.favourate_item,parent,false);
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
        var delete = itemView.findViewById<ImageView>(R.id.delete);
        var genre = itemView.findViewById<TextView>(R.id.genre);
        var name = itemView.findViewById<TextView>(R.id.name);
        var publishYear = itemView.findViewById<TextView>(R.id.publishYear);
        var reBody = itemView.findViewById<RelativeLayout>(R.id.reBody);

        fun setViews(fav:FavoriteModel){
            if (context != null) {
                Glide.with(context).load(fav.Image).into(imageItem)
            };
            name.text = fav.Name
            genre.text = fav.Genre
            publishYear.text = "Published in ${fav.Release}"

            reBody.setOnClickListener {
                var edit: SharedPreferences.Editor? =
                    context?.getSharedPreferences(context?.getString(R.string.cache),Context.MODE_PRIVATE)?.edit();
                edit?.putString(context?.getString(R.string.childA),fav.ChildA)
                edit?.putString(context?.getString(R.string.childB),fav.ChildB)
                edit?.putString(context?.getString(R.string.childC),fav.ChildC)
                edit?.putInt(context?.getString(R.string.start_type),Helper.FAVORATESTART)
                edit?.apply();
                favoriteCallback.openBookDetailAct(imageItem,name,genre)
            }
            delete.setOnClickListener{favoriteCallback.deleteFavorite(fav.FavoriteId)}
        }
    }
}