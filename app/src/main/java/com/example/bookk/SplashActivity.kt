package com.example.bookk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.bookk.View.Auth.LoginActivity
import com.example.bookk.View.Auth.SingUpActivity

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash)
        splash();
    }

    fun splash(){
        Handler(Looper.getMainLooper()).postDelayed(object :Runnable{
            override fun run() {
                var sharedPreference = getSharedPreferences(getString(R.string.cache), MODE_PRIVATE);
                var loggedIn =  sharedPreference.getBoolean(getString(R.string.user_loggedIn),false);

                if(loggedIn){
                    var intent:Intent = Intent(this@SplashActivity,MainActivity::class.java)
                    startActivity(intent);
                    finish()
                }else{
                    var intent:Intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent);
                    finish()
                }

            }
        },1000);
    }
}