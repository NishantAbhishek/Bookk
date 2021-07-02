package com.example.bookk.View.Fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookk.Adapter.BookAdapter
import com.example.bookk.Contract.HomeContract
import com.example.bookk.Model.Books
import com.example.bookk.Presenter.HomePresenter
import com.example.bookk.R

class Home : Fragment(),HomeContract.View,View.OnClickListener{
    private var presenter:HomeContract.Presenter? = null;
    private var recyclerList:ArrayList<Books> = ArrayList<Books>();
    private var bookAdapter:BookAdapter? = null;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        initialize()
        presenter?.getAllBooks()
        initializeRecycler()
    }

    private fun initialize(){
        presenter = HomePresenter(context,this);
        view?.findViewById<View>(R.id.liPopular)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liComedy)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liFantasy)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liHorror)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liMystery)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liDrama)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.liSciFi)?.setOnClickListener(this)
    }

    private fun initializeRecycler(){
        bookAdapter = BookAdapter(context,recyclerList);
        var recyclerView: RecyclerView? = view?.findViewById(R.id.recycler);
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = bookAdapter;
    }

    override fun onClick(v: View?) {
        presenter?.onClick(v)
    }

    override fun putUnderLine(uderlineIndex: Int) {

        var hsv = view?.findViewById<HorizontalScrollView>(R.id.horizontalScroll);

        var underLineViews = presenter?.getUnderLinesViews(view);
        var counter = 0;
        if (underLineViews != null) {
            for(underLineView in underLineViews){
                if(counter==uderlineIndex){
                    underLineView.visibility = View.VISIBLE
                    if(uderlineIndex>=3){
                        hsv?.fullScroll(View.FOCUS_RIGHT)
                    }else{
                        hsv?.fullScroll(View.FOCUS_LEFT)
                    }
                }else{
                    underLineView.visibility = View.GONE
                }
                counter++;
            }
        }
    }

    override fun addBooks(books:ArrayList<Books>){
        activity?.runOnUiThread(Runnable {
            view?.findViewById<View>(R.id.loading)?.visibility = View.GONE
            view?.findViewById<View>(R.id.recycler)?.visibility = View.VISIBLE
            recyclerList.clear()
            recyclerList.addAll(books)
            Log.e("Size--",books.size.toString())
            Log.e("RecyclerList--",recyclerList.size.toString())
            bookAdapter?.notifyDataSetChanged();
        });
    }

}