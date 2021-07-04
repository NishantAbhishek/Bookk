package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.example.bookk.Callbacks.EmailVerifiedCallback
import com.example.bookk.Contract.MainActContract
import com.example.bookk.Helper
import com.example.bookk.R
import com.example.bookk.View.Fragment.Account
import com.example.bookk.View.Fragment.Favorite
import com.example.bookk.View.Fragment.Home
import com.example.bookk.View.Fragment.Notes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActPresenter(private var context: Context, private var mainView: MainActContract.View) :
    MainActContract.Presenter {

    var userLoggedIn = false;
    override fun activityStarted() {

    }

    var bottomNavigationView = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_home -> {
                    mainView.setFragment(Home())
                };
                R.id.nav_fav -> {
                    if (userLoggedIn && Helper.getUid() != null) {
                        Helper.isEmailVerified(object : EmailVerifiedCallback {
                            override fun isEmailVerified(emailVerified: Boolean) {
                                if (emailVerified) {
                                    mainView.setFragment(Favorite())
                                } else {
                                    mainView.showSnackBar("Verify Email from Account Page!!!")
                                }
                            }
                        });

                    } else {
                        return createAnAccountDialog()
                    }
                };
                R.id.nav_note -> {
                    if (userLoggedIn && Helper.getUid() != null) {
                        Helper.isEmailVerified(object : EmailVerifiedCallback {
                            override fun isEmailVerified(emailVerified: Boolean) {
                                if (emailVerified) {
                                    mainView.setFragment(Notes())
                                } else {
                                    mainView.showSnackBar("Verify Email from Account Page!!!")
                                }
                            }
                        })
                    } else {
                        return createAnAccountDialog()
                    }
                };
                R.id.nav_account -> {
                    return if (userLoggedIn && Helper.getUid() != null) {
                        mainView.setFragment(Account())
                        true;
                    } else {
                        return createAnAccountDialog()
                    }
                };
            }
            return userLoggedIn && Helper.isEmailVerified();
        }
    }

    fun createAnAccountDialog(): Boolean {
        var dialog = Dialog(context);
        dialog.setContentView(R.layout.create_account_dialog);
        dialog.show()
        dialog.findViewById<Button>(R.id.ok).setOnClickListener { mainView.startSignUpPage() }
        return false
    }

    override fun setBottomNavigationListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        isUserLoggedIn()
        return bottomNavigationView;
    }

    private fun isUserLoggedIn() {
        userLoggedIn =
            context.getSharedPreferences(context.getString(R.string.cache), Context.MODE_PRIVATE)
                .getBoolean(context.getString(R.string.user_loggedIn), false);
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabBooks -> {
                if (userLoggedIn && Helper.getUid() != null) {
                    Helper.isEmailVerified(object : EmailVerifiedCallback {
                        override fun isEmailVerified(emailVerified: Boolean) {
                            if (emailVerified) {
                                mainView.startBorrowedAct()
                            } else {
                                mainView.showSnackBar("Verify Email from Account Page!!!")
                            }
                        }
                    })
                } else {
                    createAnAccountDialog()
                }
            };
        }
    }
}