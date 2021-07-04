package com.example.bookk.Presenter

import android.content.Context
import android.util.Log
import android.view.View
import com.example.bookk.Callbacks.EmailVerifiedCallback
import com.example.bookk.Contract.AccountContract
import com.example.bookk.Helper
import com.example.bookk.Model.AccountInfo
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AccountPresenter(var accountView: AccountContract.View, var context: Context?) :
    AccountContract.Presenter {
    override fun getUserAccountInfo() {
        if (Helper.getUid() != null) {
            var reference = FirebaseDatabase.getInstance().
            reference.child("Account").child(Helper.getUid()!!);

            reference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var accountInfo = snapshot.getValue(AccountInfo::class.java);
                    Log.e("TAG:- ",accountInfo?.Name+"");
                    accountView.setViews(accountInfo)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvVerify->{
                if(Helper.isEmailVerified()){
                    accountView.showSnackBar("Email Already Verified")
                    accountView.setVerifyText("Congrats, Email is Verified")
                }else{
                    Helper.isEmailVerified(object :EmailVerifiedCallback{
                        override fun isEmailVerified(emailVerified: Boolean) {
                            if(emailVerified){
                                accountView.showSnackBar("Email Already Verified")
                                accountView.setVerifyText("Congrats, Email is Verified")
                            }else{
                                verifyEmail()
                            }
                        }
                    })
                }
            }
        }
    }

    private fun verifyEmail(){
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()?.
        addOnCompleteListener(object :OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    accountView.showSnackBar("Email Verification Sent, Check your email")
                }
            }
        })
    }



}