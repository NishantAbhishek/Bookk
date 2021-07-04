package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.example.bookk.Contract.BorrowedActContract
import com.example.bookk.Helper
import com.example.bookk.Model.BorrowedModel
import com.example.bookk.Model.NoteModel
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BorrowedActPresenter(var borrowedView: BorrowedActContract.View, var context: Context) :
    BorrowedActContract.Presenter {
    private var borrowedList = ArrayList<BorrowedModel>();

    override fun getAllData() {
        var reference =
            FirebaseDatabase.getInstance().reference.child("Borrowed").child(Helper.getUid()!!);
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                borrowedList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        var borrowedModel = data.getValue(BorrowedModel::class.java);
                        borrowedList.add(borrowedModel!!);
                    }
                }else{
                    borrowedView.showSnackBar("No borrowed book yet")
                }
                borrowedView.putBook(borrowedList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun deleteBorrowedBook(book: BorrowedModel, size: Int){
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.message)
        dialog.findViewById<TextView>(R.id.message).text = "Sure want to delete this Book!!"
        dialog.show()
        dialog.findViewById<Button>(R.id.submit).setOnClickListener{
            dialog.dismiss()
            deleteFromDatabase(book)
        }
    }


    fun deleteFromDatabase(book: BorrowedModel){
        if(Helper.getUid()!=null){
            var progressDialog = openProgressDialog();
            var reference = FirebaseDatabase.getInstance().reference.child("Borrowed").child(Helper.getUid()!!).child(book.BorrowId!!);
            reference.setValue(null).addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    progressDialog.dismiss()
                    if(task.isSuccessful){
                        borrowedView.showSnackBar("Removed Successfully")
                    }else{
                        borrowedView.showSnackBar("Some Problem Occurred")
                    }
                }
            })
        }
    }

    fun openProgressDialog(): Dialog {
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false)
        dialog.show();
        return dialog;
    }

}