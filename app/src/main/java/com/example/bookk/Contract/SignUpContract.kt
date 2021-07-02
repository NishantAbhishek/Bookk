package com.example.bookk.Contract

interface SignUpContract {
    interface View{
        fun emailError(message:String);
        fun nameError(message: String);
        fun passwordError(message: String);
        fun showToast(message:String);
        fun startMainActivity();
        fun showSnackBar(message: String);
        fun saveUserDetail(name:String,email:String);
    }
    interface Presenter{
        fun onClick(v: android.view.View?,email:String,password:String,name:String);
    }
}