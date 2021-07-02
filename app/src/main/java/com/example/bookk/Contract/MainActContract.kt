package com.example.bookk.Contract

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

interface MainActContract {
    interface View{
        fun setFragment(fragment:Fragment);
    }

    interface Presenter{
        fun activityStarted();
        fun setBottomNavigationListener():BottomNavigationView.OnNavigationItemSelectedListener;
    }

}

