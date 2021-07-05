package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.bookk.Callbacks.EmailVerifiedCallback
import com.example.bookk.Contract.CommentContract
import com.example.bookk.Helper
import com.example.bookk.Model.AccountInfo
import com.example.bookk.Model.CommentModel
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentPresenter(var commentView: CommentContract.View, var context: Context) :
    CommentContract.Presenter {
    private lateinit var commentId: String
    private var list = ArrayList<CommentModel>();
    private var accountInfo: AccountInfo? = null;

    override fun loadData() {
        getUserAccountInfo()
        getSharedData()
        getFirebaseData()
    }

    private fun getSharedData() {
        var sharedPreferences =
            context.getSharedPreferences(context.getString(R.string.cache), Context.MODE_PRIVATE);
        var name = sharedPreferences.getString(context.getString(R.string.cache_name), "");
        var image = sharedPreferences.getString(context.getString(R.string.cache_image), "");
        var commentId = sharedPreferences.getString(context.getString(R.string.comment_id), "");
        this.commentId = commentId!!;
        commentView.setViews(image!!, name!!)
    }

    private fun getFirebaseData() {
        var reference = FirebaseDatabase.getInstance().reference.child("Comments").child(commentId);
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    list.clear()
                    for (comment in snapshot.children) {
                        val data = comment.getValue(CommentModel::class.java)
                        Log.e("Comment-",data?.Comment!!)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    commentView.putData(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.submit -> {
                if (Helper.getUid() != null) {
                    if (accountInfo != null && !TextUtils.isEmpty(commentView.getComment())) {
                        var comment = commentView.getComment()
                        sendComment(comment)
                    } else {
                        commentView.showSnackBar("Empty comment?")
                    }
                } else {
                    commentView.showSnackBar("Please login First....")
                }
            }
        }
    }

    private fun sendComment(comment: String) {
        Helper.isEmailVerified(object : EmailVerifiedCallback {
            override fun isEmailVerified(emailVerified: Boolean) {
                if (emailVerified) {
                    sendCommentToDatabase(comment)
                } else {
                    commentView.showSnackBar("Verify Email from Account Page!!!")
                }
            }
        })
    }

    fun sendCommentToDatabase(comment: String) {
        var progressDialog = Dialog(context);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false)
        progressDialog.show()
        var comment = CommentModel(
            accountInfo?.Avatar,
            comment,
            Helper.convertMillisToDate(System.currentTimeMillis()),
            accountInfo?.Name
        )
        var reference =
            FirebaseDatabase.getInstance().reference.child("Comments").child(commentId).child(System.currentTimeMillis().toString())
        reference.setValue(comment).addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    commentView.showSnackBar("Success..")
                    commentView.removeEditComment()
                } else {
                    commentView.showSnackBar("Failure")
                }
            }
        })

    }

    fun getUserAccountInfo() {
        if (Helper.getUid() != null) {
            var reference =
                FirebaseDatabase.getInstance().reference.child("Account").child(Helper.getUid()!!);

            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        accountInfo = snapshot.getValue(AccountInfo::class.java);
                        Log.e("accountInfo", accountInfo?.Name.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

}