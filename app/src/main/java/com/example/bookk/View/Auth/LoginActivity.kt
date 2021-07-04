package com.example.bookk.View.Auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.bookk.Contract.LoginContract
import com.example.bookk.MainActivity
import com.example.bookk.Presenter.LoginPresenter
import com.example.bookk.R
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity(),LoginContract.View, View.OnClickListener{
    private var loginPresenter:LoginPresenter? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        instantiateObject()
    }

    private fun instantiateObject(){
        loginPresenter = LoginPresenter(this,this);
        findViewById<Button>(R.id.submit).setOnClickListener(this);
        findViewById<TextView>(R.id.signUp).setOnClickListener(this);
        findViewById<TextView>(R.id.skip).setOnClickListener(this);
    }

    override fun emailError(message: String) {
        var emailInputLayout = findViewById<TextInputLayout>(R.id.emailInputLayout);
        emailInputLayout.error = message;
    }

    override fun passwordError(message: String) {
        var passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout);
        passwordInputLayout.error = message;
    }

    override fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.submit->{
                refreshErrors();
                var email:String = findViewById<AppCompatEditText>(R.id.email).text.toString();
                var password:String = findViewById<AppCompatEditText>(R.id.password).text.toString();
                loginPresenter?.onClick(v,email,password);
                return;
            }
            R.id.signUp->{
                startSignUpActivity()
            }
            R.id.skip->{
                startMainActivity()
            }
        }
    }

    private fun refreshErrors(){
        findViewById<TextInputLayout>(R.id.passwordInputLayout).error = null;
        findViewById<TextInputLayout>(R.id.emailInputLayout).error = null;
    }

    override fun startMainActivity() {
        startActivity(Intent(this,MainActivity::class.java));
        finish()
    }

    private fun startSignUpActivity() {
        startActivity(Intent(this,SingUpActivity::class.java));
    }

}