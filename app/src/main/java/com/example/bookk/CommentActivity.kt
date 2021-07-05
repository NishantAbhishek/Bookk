package com.example.bookk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookk.Adapter.CommentAdapter
import com.example.bookk.Contract.CommentContract
import com.example.bookk.Model.Books
import com.example.bookk.Model.CommentModel
import com.example.bookk.Presenter.CommentPresenter
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Comment

class CommentActivity : AppCompatActivity(),View.OnClickListener,CommentContract.View{

    private var list = ArrayList<CommentModel>();
    private lateinit var adapter:CommentAdapter;
    private lateinit var presenter:CommentContract.Presenter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
    }

    override fun onStart() {
        super.onStart()
        initialize()
        initializeRecycler()
        presenter.loadData()
    }

    private fun initialize() {
        presenter = CommentPresenter(this,this)
        adapter = CommentAdapter(this,list);
        findViewById<Button>(R.id.submit).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        presenter.onClick(v!!)
    }

    fun initializeRecycler(){
        var recyclerView: RecyclerView? = findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }

    override fun getComment():String{
        return findViewById<EditText>(R.id.writeComment).text.toString()
    }

    override fun putData(list_: ArrayList<CommentModel>) {
        list.clear()
        list.addAll(list_);
        adapter.notifyDataSetChanged()
        Log.e("Comments Length ",list_.size.toString())
    }

    override fun setViews(link: String, name: String) {
        Glide.with(this).load(link).into(findViewById(R.id.imageItem));
        findViewById<TextView>(R.id.name).text = name;
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(findViewById<LinearLayout>(R.id.layout),message, 500).show()
    }

    override fun removeEditComment(){
        return findViewById<EditText>(R.id.writeComment).setText("")
    }
}