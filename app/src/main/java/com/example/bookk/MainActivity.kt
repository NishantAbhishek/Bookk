package com.example.bookk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.bookk.Contract.MainActContract
import com.example.bookk.Presenter.MainActPresenter
import com.example.bookk.View.Auth.SingUpActivity
import com.example.bookk.View.Fragment.Home
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),MainActContract.View {
    private var currentFragment: Fragment = Home();
    private var presenter:MainActPresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)
        setFragment(currentFragment)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }
    
    private fun initialize() {
        presenter = MainActPresenter(this,this);
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnNavigationItemSelectedListener(presenter?.setBottomNavigationListener())
    }

    override fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(findViewById<LinearLayout>(R.id.liLayout),message, 500).show()
    }

    override fun onResume() {
        super.onResume()
        Helper.reloadUser()
    }

    override fun onPause() {
        super.onPause()
        Helper.reloadUser()
    }

    override fun startSignUpPage() {
        startActivity(Intent(this,SingUpActivity::class.java))
        finish()
    }

}