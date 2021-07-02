package com.example.bookk.Presenter

import android.content.Context
import android.view.MenuItem
import com.example.bookk.Contract.MainActContract
import com.example.bookk.R
import com.example.bookk.View.Fragment.Account
import com.example.bookk.View.Fragment.Favourate
import com.example.bookk.View.Fragment.Home
import com.example.bookk.View.Fragment.Notes
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActPresenter(private var context:Context,private var mainView:MainActContract.View) : MainActContract.Presenter{

    override fun activityStarted() {

    }

    var bottomNavigationView = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.nav_home-> mainView.setFragment(Home());
                R.id.nav_fav->mainView.setFragment(Favourate());
                R.id.nav_note->mainView.setFragment(Notes());
                R.id.nav_account->mainView.setFragment(Account());
            }
            return true;
        }
    }

    override fun setBottomNavigationListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        return bottomNavigationView;
    }


}