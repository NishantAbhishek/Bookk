package com.example.bookk.Contract

import android.view.View

interface LoginContract {
    interface View{
        fun emailError(message:String);
        fun passwordError(message: String);
        fun showToast(message:String);
        fun startMainActivity();
    }

    interface Presenter{
        fun onClick(v: android.view.View?,email:String,password:String);
    }
}