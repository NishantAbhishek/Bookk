package com.example.bookk.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookk.Callbacks.BorrowedCallback
import com.example.bookk.Callbacks.FavoriteCallback
import com.example.bookk.Helper
import com.example.bookk.Model.BorrowedModel
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.R

class BorrowedAdapter(val context: Context?, var list:ArrayList<BorrowedModel>, var borrowedCallback: BorrowedCallback):
    RecyclerView.Adapter<BorrowedAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.borrowed_item,parent,false);
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
        var delete = itemView.findViewById<ImageView>(R.id.delete);
        var genre = itemView.findViewById<TextView>(R.id.genre);
        var name = itemView.findViewById<TextView>(R.id.name);
        var publishYear = itemView.findViewById<TextView>(R.id.publishYear);
        var reBody = itemView.findViewById<RelativeLayout>(R.id.reBody);

        fun setViews(borrow:BorrowedModel){
            if (context != null) {
                Glide.with(context).load(borrow.Image).into(imageItem)
            };
            name.text = borrow.Name
            genre.text = borrow.Genre
            publishYear.text = "Published in ${borrow.Release}"

            reBody.findViewById<CardView>(R.id.openBook).setOnClickListener({
                var intent = Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf")
                intent.setData(Uri.parse("https://firebasestorage.googleapis.com/v0/b/bookk-e6f58.appspot.com/o/Books%2Fthe_kite_runner.pdf?alt=media&token=ee11e23b-42e9-4c21-84a9-77f059b2210f"))
                context?.startActivity(intent);
            })

            reBody.setOnClickListener {
                var edit: SharedPreferences.Editor? =
                    context?.getSharedPreferences(context?.getString(R.string.cache),Context.MODE_PRIVATE)?.edit();
                edit?.putString(context?.getString(R.string.childA),borrow.ChildA)
                edit?.putString(context?.getString(R.string.childB),borrow.ChildB)
                edit?.putString(context?.getString(R.string.childC),borrow.ChildC)
                edit?.putInt(context?.getString(R.string.start_type),Helper.FAVORATESTART)
                edit?.apply();
            }
            delete.setOnClickListener{borrowedCallback.deleteBorrowed(borrow)}
        }
    }
}