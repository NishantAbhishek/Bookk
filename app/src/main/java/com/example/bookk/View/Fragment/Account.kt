package com.example.bookk.View.Fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bookk.Contract.AccountContract
import com.example.bookk.Contract.FavoriteContract
import com.example.bookk.Helper
import com.example.bookk.Model.AccountInfo
import com.example.bookk.Presenter.AccountPresenter
import com.example.bookk.R
import com.example.bookk.View.Auth.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class Account : Fragment(),AccountContract.View,View.OnClickListener{
    private lateinit var presenter:AccountContract.Presenter;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize(){
        presenter = AccountPresenter(this,context);
        presenter.getUserAccountInfo()
    }

    override fun setViews(accountInfo: AccountInfo?) {

        var avatar: Drawable? = when(accountInfo?.Avatar) {
            1->context?.getDrawable(R.drawable.avatar_1)
            2->context?.getDrawable(R.drawable.avatar2)
            4->context?.getDrawable(R.drawable.avatar_4);
            else->context?.getDrawable(R.drawable.avatar_5);
        }
        view?.findViewById<ImageView>(R.id.avatar)?.setImageDrawable(avatar);
        view?.findViewById<TextView>(R.id.name)?.text = accountInfo?.Name
        view?.findViewById<TextView>(R.id.email)?.text = accountInfo?.Email
        view?.findViewById<TextView>(R.id.phone)?.text= accountInfo?.Phone
        view?.findViewById<TextView>(R.id.member)?.text= accountInfo?.Member

        view?.findViewById<FloatingActionButton>(R.id.fab_edit)?.setOnClickListener(this)
        if(!Helper.isEmailVerified()){
            view?.findViewById<TextView>(R.id.tvVerify)?.text = "Click To Verify Your Email"
        }else{
            view?.findViewById<TextView>(R.id.tvVerify)?.text = "Congrats, Email is Verified"
        }

        view?.findViewById<TextView>(R.id.logout)?.setOnClickListener(this)
        view?.findViewById<TextView>(R.id.tvVerify)?.setOnClickListener(this)
        view?.findViewById<FloatingActionButton>(R.id.fab_edit)?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        presenter.onClick(v)
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(requireView().findViewById<RelativeLayout>(R.id.layout),message, 350).show()
    }

    override fun setVerifyText(text: String) {
        view?.findViewById<TextView>(R.id.tvVerify)?.text = "Congrats, Email is Verified"
    }

    override fun startLoginActivity(){
        context?.startActivity(Intent(activity,LoginActivity::class.java))
    }
}