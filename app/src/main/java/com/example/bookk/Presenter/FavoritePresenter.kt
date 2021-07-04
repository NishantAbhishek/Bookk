package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.bookk.Contract.FavoriteContract
import com.example.bookk.Model.Books
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.R
import com.example.bookk.View.Fragment.Favorite
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.favourate_item.*

class FavoritePresenter(var favoriteView: FavoriteContract.View, var context: Context?) :
    FavoriteContract.Presenter {
    private var favoriteList:ArrayList<FavoriteModel> = ArrayList();

    override fun getFavorite(){
        if(getUid()!=null){
            var reference = getFavoriteRef();
            reference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        favoriteList.clear()
                        for(bookSnapShot in snapshot.children){
                            val favorite = bookSnapShot.getValue(FavoriteModel::class.java)
                            if(favorite!=null){
                                favoriteList.add(favorite);
                            }
                        }
                        favoriteView.setFavorite(favoriteList = favoriteList)
                        Log.e("Presenter Size:- ",favoriteList.size.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }else{
            favoriteView.showSnackBar("Account problem login Again!!!")
        }
    }

    private fun getFavoriteRef():DatabaseReference{
        return FirebaseDatabase.getInstance().reference.child("Favorite").child(getUid()!!)
    }

    private fun getUid(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun deleteFavorite(favoriteId: String?, size: Int) {
        if(getUid()!=null){
            var dialog: Dialog = Dialog(context!!);
            dialog.setContentView(R.layout.message);
            dialog.findViewById<TextView>(R.id.message).text = "Sure want to Remove?"
            dialog.show()

            dialog.findViewById<Button>(R.id.submit)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if(size==1){
                            favoriteView.removeAllElement()
                        }
                        dialog.dismiss()
                        confirmDeletion(favoriteId)
                    }
                })

        }else{
            favoriteView.showSnackBar("Account problem login Again!!!")
        }
    }

    private fun confirmDeletion(favoriteId: String?){
        var reference = getFavoriteRef();
        var dialog = loadingDialog();
        dialog.setCancelable(false)
        dialog.show()
        reference.child(favoriteId!!).setValue(null).addOnCompleteListener(object :OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    favoriteView.showSnackBar("Removed Successfully")
                    dialog.dismiss()
                }else{
                    favoriteView.showSnackBar("Some Problem Occurred")
                    dialog.dismiss()
                }
            }

        })
    }

    private fun loadingDialog():Dialog{
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;
    }
}