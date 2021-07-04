package com.example.bookk

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat.setTransitionName
import com.bumptech.glide.Glide
import com.example.bookk.Contract.BookDetailContract
import com.example.bookk.Model.Books
import com.example.bookk.Presenter.BookDetailPresenter
import com.google.android.material.snackbar.Snackbar

class BookDetailActivity : AppCompatActivity(),View.OnClickListener,BookDetailContract.View{
    var commentId:String? = ""
    var presenter:BookDetailContract.Presenter? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        Helper.reloadUser()
        presenter = BookDetailPresenter(this,this);
    }

    override fun onStart() {
        super.onStart()
        presenter?.getBookObject()
    }

    override fun setBookDetail(book: Books,startType:Int) {
        Glide.with(applicationContext).load(book.Image).into(findViewById<ImageView>(R.id.imageItem))
        findViewById<TextView>(R.id.name).text = book.Name
        findViewById<TextView>(R.id.genre).text = book.Genre+" Genre"
        findViewById<TextView>(R.id.language).text = book.Language
        findViewById<TextView>(R.id.description).text = book.Description+getString(R.string.lorem)
        findViewById<RatingBar>(R.id.ratingBar).rating = book.Rating!!.toFloat()
        findViewById<TextView>(R.id.page).text = book.Page.toString()
        findViewById<TextView>(R.id.year).text = book.Release.toString()

        if(startType==Helper.FAVORATESTART){
            findViewById<LinearLayout>(R.id.favorite).visibility = View.GONE
            findViewById<LinearLayout>(R.id.note).visibility = View.GONE
        }

        findViewById<LinearLayout>(R.id.scrollDown).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.scrollTop).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.note).setOnClickListener(this)
        findViewById<LinearLayout>(R.id.favorite).setOnClickListener(this)
    }


    override fun onBackPressed() {
        finish()
    }
    override fun onClick(v: View?) {
        presenter?.onClick(v)
    }

    override fun scrollBottom() {
        findViewById<ScrollView>(R.id.scrollView).fullScroll(ScrollView.FOCUS_DOWN)
    }

    override fun scrollTop() {
        findViewById<ScrollView>(R.id.scrollView).fullScroll(ScrollView.FOCUS_UP)
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(findViewById<RelativeLayout>(R.id.layout),message, Snackbar.LENGTH_LONG).show()
    }

}