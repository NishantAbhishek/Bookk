package com.example.bookk.View.Auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.bookk.Contract.SignUpContract
import com.example.bookk.MainActivity
import com.example.bookk.Presenter.SignupPresenter
import com.example.bookk.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class SingUpActivity : AppCompatActivity(),SignUpContract.View, View.OnClickListener{
    private var presenter:SignUpContract.Presenter?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        instantiate()
    }

    private fun instantiate(){
        presenter = SignupPresenter(this,this);
        findViewById<Button>(R.id.submit).setOnClickListener(this);
        findViewById<TextView>(R.id.skip).setOnClickListener(this);
        findViewById<TextView>(R.id.login).setOnClickListener(this);
    }

    override fun emailError(message: String) {
        var emailInputLayout = findViewById<TextInputLayout>(R.id.emailInputLayout);
        emailInputLayout.error = message;
    }

    override fun passwordError(message: String) {
        var passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout);
        passwordInputLayout.error = message;
    }

    override fun nameError(message: String) {
        var passwordInputLayout = findViewById<TextInputLayout>(R.id.nameInputLayout);
        passwordInputLayout.error = message;
    }

    override fun showToast(message: String) {
//        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit->{
                refreshErrors();
                var email:String = findViewById<AppCompatEditText>(R.id.email).text.toString();
                var password:String = findViewById<AppCompatEditText>(R.id.password).text.toString();
                var name:String = findViewById<AppCompatEditText>(R.id.name).text.toString();
                presenter?.onClick(v,email,password,name);
                return
            }
            R.id.login->startLoginActivity()
            R.id.skip->startMainActivity()
        }
    }

    private fun refreshErrors(){
        findViewById<TextInputLayout>(R.id.passwordInputLayout).error = null;
        findViewById<TextInputLayout>(R.id.emailInputLayout).error = null;
        findViewById<TextInputLayout>(R.id.nameInputLayout).error = null;
    }

    private fun startLoginActivity(){
        startActivity(Intent(this,LoginActivity::class.java))
    }

    override fun startMainActivity(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(findViewById<LinearLayout>(R.id.parentLayout),message,Snackbar.LENGTH_LONG).show()
    }

    override fun saveUserDetail(name: String, email: String) {
        var editor:SharedPreferences.Editor = getSharedPreferences(getString(R.string.cache),MODE_PRIVATE).edit();
        editor.putString(getString(R.string.user_email),email);
        editor.putString(getString(R.string.user_name),name);
        editor.putBoolean(getString(R.string.user_loggedIn),true);
        editor.apply()
    }

}