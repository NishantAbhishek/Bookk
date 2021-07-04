package com.example.bookk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookk.Adapter.BorrowedAdapter
import com.example.bookk.Adapter.FavoriteAdapter
import com.example.bookk.Callbacks.BorrowedCallback
import com.example.bookk.Contract.BorrowedActContract
import com.example.bookk.Model.BorrowedModel
import com.example.bookk.Presenter.BorrowedActPresenter
import com.google.android.material.snackbar.Snackbar

class BorrowedActivity : AppCompatActivity(),BorrowedActContract.View,BorrowedCallback{
    private var list = ArrayList<BorrowedModel>();
    private lateinit var adapter:BorrowedAdapter
    private lateinit var presenter:BorrowedActContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrowed)
    }

    override fun onStart() {
        super.onStart()
        initialize()
        initializeRecycler()
        presenter.getAllData()
    }


    fun initialize(){
        adapter = BorrowedAdapter(this,list,this);
        presenter = BorrowedActPresenter(this,this);
    }

    fun  initializeRecycler(){
        var recyclerView: RecyclerView? = findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }


    override fun removelastData() {
    }

    override fun showSnackBar(message: String) {
//        if(message.equals("Some Problem Occurred")){
//            presenter.getAllData()
//        }
        Snackbar.make(findViewById<RelativeLayout>(R.id.layout),message, Snackbar.LENGTH_LONG).show()

    }

    override fun deleteBorrowed(book: BorrowedModel) {
        presenter.deleteBorrowedBook(book,list.size)
    }

    override fun putBook(book: ArrayList<BorrowedModel>) {
        list.clear()
        list.addAll(book)
        adapter.notifyDataSetChanged()
    }

}