package com.example.bookk.Contract

import android.view.View
import com.example.bookk.Model.AccountInfo

interface AccountContract {
    interface View{
        fun setViews(accountInfo: AccountInfo?);
        fun showSnackBar(message: String)
        fun setVerifyText(text: String)
        fun startLoginActivity()
    }
    interface Presenter{
        fun getUserAccountInfo();
        fun onClick(v: android.view.View?)
    }
}