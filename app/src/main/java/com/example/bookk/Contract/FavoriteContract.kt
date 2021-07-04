package com.example.bookk.Contract

import com.example.bookk.Model.FavoriteModel
import com.example.bookk.View.Fragment.Favorite

interface FavoriteContract {
    interface View{
        fun setFavorite(favoriteList:ArrayList<FavoriteModel>);
        fun showSnackBar(message: String)
        fun removeAllElement();
    }

    interface Presenter{
        fun getFavorite();
        fun deleteFavorite(favoriteId:String?,size:Int);
    }
}