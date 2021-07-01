package com.example.bookk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bookk.View.Auth.LoginActivity

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splash();
    }

    fun splash(){
        Handler().postDelayed(object :Runnable{
            override fun run() {
                var sharedPreference = getSharedPreferences(getString(R.string.cache), MODE_PRIVATE);
                var loggedIn =  sharedPreference.getBoolean(getString(R.string.user_loggedIn),false);

                if(loggedIn){
                    var intent:Intent = Intent(this@SplashActivity,MainActivity::class.java)
                    startActivity(intent);
                    finish()
                }else{
                    var intent:Intent = Intent(this@SplashActivity,LoginActivity::class.java)
                    startActivity(intent);
                    finish()
                }

            }
        },1000);
    }
}