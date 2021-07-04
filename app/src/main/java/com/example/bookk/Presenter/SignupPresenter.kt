package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.opengl.Visibility
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bookk.Contract.SignUpContract
import com.example.bookk.Helper
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupPresenter(
    private val signView: SignUpContract.View,
    private val context: Context) : SignUpContract.Presenter {

    private fun signUpUser(email: String, password: String, name: String) {
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance();
        var dialog: Dialog = Dialog(context);
        dialog.setContentView(R.layout.signup_dialog)
        dialog.setCancelable(false)
        dialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(object :OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        setUserDetails(email,password,firebaseAuth,dialog,name);
                    }else{
                        dialog.dismiss()
                        signView.showSnackBar("Encountered some Problem...")
                    }
                }
            });
    }

    private fun setUserDetails(email:String,password: String,firebaseAuth:FirebaseAuth,dialog: Dialog,name:String){
        var reference = FirebaseDatabase.getInstance().reference.child("Account").child(Helper.getUid()!!);
        var data: HashMap<String, Any?> = HashMap();
        data["Avatar"] = 5;
        data["Email"] = email
        data["Member"] = "Basic"
        data["Name"] = name
        data["Phone"] = "0000000"

        reference.setValue(data).addOnCompleteListener(object :OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    sendEmailVerification(email,password,firebaseAuth,dialog,name)
                }
            }
        })
    }

    private fun sendEmailVerification(email:String,password: String,firebaseAuth:FirebaseAuth,dialog: Dialog,name:String){
        firebaseAuth.currentUser?.sendEmailVerification()?.
        addOnCompleteListener(object :OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    dialog.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE;
                    dialog.findViewById<Button>(R.id.done).visibility = View.VISIBLE;
                    dialog.findViewById<TextView>(R.id.message).text = "Account Created, Please open your email to verify it..";
                    dialog.findViewById<Button>(R.id.done).setOnClickListener{
                        dialog.dismiss()
                        signView.saveUserDetail(name,email)
                        signView.startMainActivity()
                    };
                    signView.showSnackBar("Verify your Account to enjoy Premium Features")
                }
            }
        })
    }

    private fun checkEmail(email:String):Boolean{
        if(TextUtils.isEmpty(email)){
            signView.emailError("Can't be Empty");
            return false
        }else{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                return true;
            }else{
                signView.emailError("Not Email Address");
                return false;
            }
        }
    }

    private fun checkPassword(password: String):Boolean{
        return if(TextUtils.isEmpty(password)){
            signView.passwordError("Can't be Empty");
            false
        }else{
            if(password.length<8){
                signView.passwordError("minimum 8 characters")
                return false
            }else{
                return true
            }
        }
    }

    private fun checkName(password: String):Boolean{
        return if(TextUtils.isEmpty(password)){
            signView.nameError("Can't be Empty");
            false
        }else{
            return true
        }
    }


    override fun onClick(v: View?, email: String, password: String, name: String) {
        when(v?.id){
            R.id.submit->{
                var emailCheck = checkEmail(email);
                var passwordCheck = checkPassword(password);
                var nameCheck = checkName(name);
                if(emailCheck && passwordCheck && nameCheck){
                    signUpUser(email,password,name)
                }
            }
        }
    }



}