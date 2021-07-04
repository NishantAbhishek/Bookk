package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bookk.Callbacks.EmailVerifiedCallback
import com.example.bookk.Contract.BookDetailContract
import com.example.bookk.Helper
import com.example.bookk.Model.Books
import com.example.bookk.R
import com.example.bookk.View.Fragment.Home
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.note_dialog.*
import java.util.*
import kotlin.collections.HashMap

class BookDetailPresenter(
    private var bookView: BookDetailContract.View,
    private var context: Context
) : BookDetailContract.Presenter {
    var book: Books = bookObject();
    var userLoggedIn: Boolean = false;
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scrollTop -> bookView.scrollTop()
            R.id.scrollDown -> bookView.scrollBottom()
            R.id.note -> noteCreateDialog()
            R.id.favorite -> addToFavorite()
        }
    }

    private fun noteCreateDialog() {
        if (userLoggedIn) {
            Helper.isEmailVerified(object :EmailVerifiedCallback{
                override fun isEmailVerified(emailVerified: Boolean) {
                    if(emailVerified){
                        var dialog: Dialog = Dialog(context);
                        dialog.setContentView(R.layout.note_dialog);
                        dialog.show()
                        dialog.findViewById<Button>(R.id.submit)
                            .setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    var noteText = dialog.findViewById<EditText>(R.id.note).text.toString();
                                    dialog.dismiss()
                                    if (!TextUtils.isEmpty(noteText)) {
                                        saveNote(noteText)
                                    }
                                }
                            })
                    }else{
                        bookView.showSnackBar("Verify Email from Account Page!!!")
                    }
                }
            })

        } else {
            bookView.showSnackBar("Please Create An Account First")
        }
    }

    private fun addToFavorite() {
        if (userLoggedIn) {
            Helper.isEmailVerified(object :EmailVerifiedCallback{
                override fun isEmailVerified(emailVerified: Boolean) {
                    if(emailVerified){
                        var dialog: Dialog = Dialog(context);
                        dialog.setContentView(R.layout.message);
                        dialog.show()

                        dialog.findViewById<Button>(R.id.submit)
                            .setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    dialog.dismiss()
                                    saveFavorite()
                                }
                            })
                    }else{
                        bookView.showSnackBar("Verify Email from Account Page!!!")
                    }
                }
            })
        } else {
            bookView.showSnackBar("Please Create An Account First")
        }
    }

    private fun saveFavorite() {
        var data: HashMap<String, Any?> = HashMap();
        var favoriteId: String? = book.Id;
        data["ChildA"] = "Books";
        data["ChildB"] = Helper.genreToNode(book.Genre)
        data["ChildC"] = book.Id
        data["FavoriteId"] = book.Id;
        data["Genre"] = book.Genre
        data["Name"] = book.Name
        data["Image"] = book.Image
        data["Release"] = book.Release

        var dialog = loadingDialog();
        dialog.setCancelable(false)
        dialog.show()

        if (getUid() != null) {
            var databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("Favorite")
                    .child(getUid()!!)
            databaseReference.child(favoriteId!!).setValue(data)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            bookView.showSnackBar("Favorite Saved SuccessFully")
                            dialog.dismiss()
                        } else {
                            bookView.showSnackBar("Some Problem Occurred")
                            dialog.dismiss()
                        }
                    }
                })
        }

    }

    private fun saveNote(note: String?) {
        var data: HashMap<String, Any?> = HashMap();
        var noteId: String = System.currentTimeMillis().toString();
        data["ChildA"] = "Books";
        data["ChildB"] = Helper.genreToNode(book.Genre)
        data["ChildC"] = book.Id
        data["NoteId"] = noteId;
        data["Date"] = Helper.convertMillisToDate(System.currentTimeMillis())
        data["Genre"] = book.Genre
        data["Name"] = book.Name
        data["Image"] = book.Image
        data["Note"] = note;

        var dialog = loadingDialog();
        dialog.setCancelable(false)
        dialog.show()

        if (getUid() != null) {
            var databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("Note")
                    .child(getUid()!!)
            databaseReference.child(noteId).setValue(data)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            bookView.showSnackBar("Note Saved SuccessFully")
                            dialog.dismiss()
                        } else {
                            bookView.showSnackBar("Some Problem Occurred")
                            dialog.dismiss()
                        }
                    }
                })
        }
    }

    private fun loadingDialog(): Dialog {
        var dialog = Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;
    }

    private fun getUid(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid;
    }

    private fun bookObject(): Books {
        var sharedPreference = context.getSharedPreferences(
            context.getString(R.string.cache),
            AppCompatActivity.MODE_PRIVATE
        );
        userLoggedIn =
            sharedPreference.getBoolean(context.getString(R.string.user_loggedIn), false);
        return Books(
            CommentId = sharedPreference.getString(context.getString(R.string.cache_commentId), ""),
            Description = sharedPreference.getString(
                context.getString(R.string.cache_Description),
                ""
            ),
            Genre = sharedPreference.getString(context.getString(R.string.cache_genre), ""),
            Image = sharedPreference.getString(context.getString(R.string.cache_image), ""),
            Language = sharedPreference.getString(context.getString(R.string.cache_language), ""),
            Name = sharedPreference.getString(context.getString(R.string.cache_name), ""),
            Id = sharedPreference.getString(context.getString(R.string.cache_bookId), ""),
            Page = sharedPreference.getInt(context.getString(R.string.cache_page), 180),
            Popularity = sharedPreference.getInt(context.getString(R.string.cache_popularity), 4),
            Rating = sharedPreference.getInt(context.getString(R.string.cache_rating), 4),
            Release = sharedPreference.getInt(context.getString(R.string.cache_Year), 180)
        )
    }

    override fun getBookObject(){
        var sharedPreference = context.getSharedPreferences(
            context.getString(R.string.cache),
            AppCompatActivity.MODE_PRIVATE
        );
        var startType = sharedPreference.getInt(context.getString(R.string.start_type),Helper.HOMESTART);
        if(startType==Helper.HOMESTART){
            bookView.setBookDetail(book,startType)
        }else{
            var childA = sharedPreference.getString(context.getString(R.string.childA),"");
            var childB = sharedPreference.getString(context.getString(R.string.childB),"");
            var childC = sharedPreference.getString(context.getString(R.string.childC),"");
            databaseBook(childA!!,childB!!,childC!!,startType)
        }
    }

    private fun databaseBook(childA:String,childB:String,childC:String,startType:Int){
        var reference = FirebaseDatabase.getInstance().reference.child(childA).
        child(childB).child(childC);
        Log.e("child:- ","A $childA B $childB C $childC")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var book_ = snapshot.getValue(Books::class.java)
                if(book_!=null){
                    bookView.setBookDetail(book_!!,startType)
                }else{
                    bookView.showSnackBar("Some problem occured")
                }
            }
        })

    }

}