package com.example.bookk.View.Fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookk.Adapter.BookAdapter
import com.example.bookk.BookDetailActivity
import com.example.bookk.Callbacks.ItemClick
import com.example.bookk.Contract.HomeContract
import com.example.bookk.Model.Books
import com.example.bookk.Presenter.HomePresenter
import com.example.bookk.R


class Home : Fragment(),HomeContract.View,View.OnClickListener,ItemClick{
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

        view?.findViewById<EditText>(R.id.edSearch)?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                presenter?.filterBooks(s.toString(),recyclerList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun initializeRecycler(){
        bookAdapter = BookAdapter(context,recyclerList,this);
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
            bookAdapter?.notifyDataSetChanged();
            recyclerList.addAll(books)
            bookAdapter?.notifyDataSetChanged();
        });
    }

    override fun transitionAnimation(imageItem: ImageView, name: TextView, genre: TextView) {
        val intent = Intent(context, BookDetailActivity::class.java)
        var  imageItem_ = Pair.create<View?, String?>(imageItem as View,"imageItem");
        var  name_= Pair.create<View?, String?>(name as View,"name");
        var  genre_ = Pair.create<View?, String?>(genre as View,"genre");

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), imageItem_, name_,genre_)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            startActivity(intent,options.toBundle())
        }else{
            startActivity(intent)
        }
    }

}