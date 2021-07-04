package com.example.bookk.View.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookk.Adapter.BookAdapter
import com.example.bookk.Adapter.FavoriteAdapter
import com.example.bookk.BookDetailActivity
import com.example.bookk.Callbacks.FavoriteCallback
import com.example.bookk.Contract.FavoriteContract
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Presenter.FavoritePresenter
import com.example.bookk.R
import com.google.android.material.snackbar.Snackbar

class Favorite : Fragment(),FavoriteContract.View,FavoriteCallback{
    private lateinit var presenter:FavoriteContract.Presenter
    private var favoriteList = ArrayList<FavoriteModel>();
    private lateinit var adapter:FavoriteAdapter;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourate, container, false)
    }

    override fun onStart() {
        super.onStart()
        initialize()
        initializeRecycler()
        presenter.getFavorite()
    }

    private fun initialize(){
        presenter = FavoritePresenter(this,context);
        adapter = FavoriteAdapter(context,favoriteList,this);
    }

    private fun initializeRecycler(){
        var recyclerView:RecyclerView? = view?.findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
    }

    override fun setFavorite(list: ArrayList<FavoriteModel>) {
        favoriteList.clear()
        favoriteList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun deleteFavorite(favoriteId:String?){
        presenter.deleteFavorite(favoriteId,favoriteList.size)
    }

    override fun openBookDetailAct(imageItem: ImageView, name: TextView, genre: TextView){
        val intent = Intent(context, BookDetailActivity::class.java)
        var  imageItem_ = Pair.create<View?, String?>(imageItem as View,"imageItem");
        var  name_= Pair.create<View?, String?>(name as View,"name");
        var  genre_ = Pair.create<View?, String?>(genre as View,"genre");

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), imageItem_, name_,genre_)

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
            startActivity(intent,options.toBundle())
        }else{
            startActivity(intent)
        }
    }

    override fun showSnackBar(message: String) {
        if(message.equals("Some Problem Occurred")){
            presenter.getFavorite()
        }
        Snackbar.make(requireView().findViewById<RelativeLayout>(R.id.layout),message, 350).show()
    }

    override fun removeAllElement() {
        favoriteList.clear()
        adapter.notifyDataSetChanged()
    }

}