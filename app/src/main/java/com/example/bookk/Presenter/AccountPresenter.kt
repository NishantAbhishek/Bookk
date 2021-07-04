package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    var userInfo:AccountInfo?=null;
    override fun getUserAccountInfo() {
        if (Helper.getUid() != null) {
            var reference = FirebaseDatabase.getInstance().
            reference.child("Account").child(Helper.getUid()!!);

            reference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var accountInfo = snapshot.getValue(AccountInfo::class.java);
                    userInfo = accountInfo;
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
            R.id.fab_edit->{
                editUserDetail()
            }
            R.id.logout->{LogOutUser()}
        }
    }

    private fun editUserDetail(){
        if(Helper.getUid()!=null && userInfo?.Name!=null&&userInfo?.Avatar!=null){
            var dialog = Dialog(context!!);
            dialog.setContentView(R.layout.account_edit_dialog);
            dialog.show()
            dialog.findViewById<EditText>(R.id.name).setText(userInfo?.Name)
            var avatar = userInfo?.Avatar;

            when(userInfo?.Avatar){
                1->{dialog.findViewById<ImageView>(R.id.avatar1).background = context?.getDrawable(R.drawable.avatr_click)}
                2->{dialog.findViewById<ImageView>(R.id.avatar2).background = context?.getDrawable(R.drawable.avatr_click)}
                4->{dialog.findViewById<ImageView>(R.id.avatar4).background = context?.getDrawable(R.drawable.avatr_click)}
                5->{dialog.findViewById<ImageView>(R.id.avatar5).background = context?.getDrawable(R.drawable.avatr_click)}
            }

            var avatarClick = object: View.OnClickListener{
                override fun onClick(v: View?) {
                    dialog.findViewById<ImageView>(R.id.avatar1).background = null
                    dialog.findViewById<ImageView>(R.id.avatar2).background = null
                    dialog.findViewById<ImageView>(R.id.avatar4).background = null
                    dialog.findViewById<ImageView>(R.id.avatar5).background = null
                    when(v?.id){
                        R.id.avatar1->{
                            dialog.findViewById<ImageView>(R.id.avatar1).background = context?.getDrawable(R.drawable.avatr_click);avatar=1
                        }
                        R.id.avatar2->{
                            dialog.findViewById<ImageView>(R.id.avatar2).background= context?.getDrawable(R.drawable.avatr_click);avatar=2
                        }
                        R.id.avatar4->{
                            dialog.findViewById<ImageView>(R.id.avatar4).background= context?.getDrawable(R.drawable.avatr_click);avatar=4
                        }
                        R.id.avatar5->{
                            dialog.findViewById<ImageView>(R.id.avatar5).background= context?.getDrawable(R.drawable.avatr_click);avatar=5
                        }
                    }
                }
            }
            dialog.findViewById<ImageView>(R.id.avatar1).setOnClickListener(avatarClick)
            dialog.findViewById<ImageView>(R.id.avatar2).setOnClickListener(avatarClick)
            dialog.findViewById<ImageView>(R.id.avatar4).setOnClickListener(avatarClick)
            dialog.findViewById<ImageView>(R.id.avatar5).setOnClickListener(avatarClick)
            dialog.findViewById<Button>(R.id.submit).setOnClickListener{
                var userData = userInfo?.copy();
                userData?.Name = dialog.findViewById<EditText>(R.id.name).text.toString()
                userData?.Avatar = avatar
                dialog.dismiss()
                updateDetailDatabase(userData)
            };
        }
    }

    private fun updateDetailDatabase(user:AccountInfo?){
        var reference = FirebaseDatabase.getInstance().reference.child("Account").child(Helper.getUid()!!);
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.show()
        reference.setValue(user).addOnCompleteListener {task->
            dialog.dismiss()
            if(task.isSuccessful){
                accountView.showSnackBar("Success")
            }else{
                accountView.showSnackBar("Failure")
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

    private fun LogOutUser(){
        context?.getSharedPreferences(context?.getString(R.string.cache),Context.MODE_PRIVATE)?.edit()?.clear()?.apply();
        FirebaseAuth.getInstance().signOut();
        accountView.startLoginActivity()
    }



}