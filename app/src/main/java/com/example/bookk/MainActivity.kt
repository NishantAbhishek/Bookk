package com.example.bookk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.bookk.Contract.MainActContract
import com.example.bookk.Presenter.MainActPresenter
import com.example.bookk.View.Fragment.Home
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),MainActContract.View {
    private var currentFragment: Fragment = Home();
    private var presenter:MainActPresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onStart() {
        super.onStart()
        setFragment(currentFragment)
        initialize()
    }
    
    private fun initialize() {
        presenter = MainActPresenter(this,this);
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnNavigationItemSelectedListener(presenter?.setBottomNavigationListener())
    }

    override fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
    }
}