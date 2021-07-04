package com.example.bookk.Presenter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookk.Contract.LoginContract
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val loginView:LoginContract.View,private val context: Context): LoginContract.Presenter{

    private fun loginUser(email: String, password: String){
        var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance();

        var dialog:Dialog = Dialog(context);
        dialog.setContentView(R.layout.login_dialog)
        dialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password).
        addOnCompleteListener(object :OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    dialog.dismiss()
                    saveUserDetail("",email)
                    loginView.startMainActivity()
                }else{
                    dialog.dismiss()
                    loginView.emailError("Incorrect email or password")
                    loginView.passwordError("Incorrect email or password")
                }
            }
        });
    }

    private fun checkEmail(email:String):Boolean{
        if(TextUtils.isEmpty(email)){
            loginView.emailError("Can't be Empty");
            return false
        }else{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                return true;
            }else{
                loginView.emailError("Not Email Address");
                return false;
            }
        }
    }

    private fun checkPassword(password: String):Boolean{
        return if(TextUtils.isEmpty(password)){
            loginView.passwordError("Can't be Empty");
            false
        }else{
            return true
        }
    }

    override fun onClick(v: View?,email:String,password:String) {
        when(v?.id){
            R.id.submit->{
                var emailCheck = checkEmail(email);
                var passwordCheck = checkPassword(password);

                if(emailCheck && passwordCheck){
                    loginUser(email,password)
                }
            }
        }
    }


    private fun saveUserDetail(name: String, email: String){
        var editor: SharedPreferences.Editor = context.getSharedPreferences(context.getString(R.string.cache),
            AppCompatActivity.MODE_PRIVATE
        ).edit();
        editor.putString(context.getString(R.string.user_email),email);
        editor.putString(context.getString(R.string.user_name),name);
        editor.putBoolean(context.getString(R.string.user_loggedIn),true);
        editor.apply()
    }

}