package com.example.bookk.Presenter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.bookk.Contract.HomeContract
import com.example.bookk.Model.Books
import com.example.bookk.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class HomePresenter(
    private var context: Context?,
    private var homeView: HomeContract.View
) : HomeContract.Presenter {
    private var allBooks:ArrayList<Books> = ArrayList<Books>();

    override fun getUnderLinesViews(view: View?): Array<View> {
        return arrayOf(
            view?.findViewById(R.id.udrPopular) as View,
            view?.findViewById(R.id.udrComedy) as View,
            view?.findViewById(R.id.udrFantasy) as View,
            view?.findViewById(R.id.udrHorror) as View,
            view?.findViewById(R.id.udrMystery) as View,
            view?.findViewById(R.id.udrDrama) as View,
            view?.findViewById(R.id.udrSciFi) as View
        );
    }

    override fun onClick(view: View?){
        when(view?.id){
            R.id.liPopular->{homeView.putUnderLine(0);separateBooks("Popular")}
            R.id.liComedy->{homeView.putUnderLine(1);separateBooks("Comedy")}
            R.id.liFantasy->{homeView.putUnderLine(2);separateBooks("Fantasy")}
            R.id.liHorror->{homeView.putUnderLine(3);separateBooks("Horror")}
            R.id.liMystery->{homeView.putUnderLine(4);separateBooks("Mystery")}
            R.id.liDrama->{homeView.putUnderLine(5);separateBooks("Drama")}
            R.id.liSciFi->{homeView.putUnderLine(6);separateBooks("SciFi")}
        }
    }

    fun separateBooks(separationType:String){
        var filterBooks:ArrayList<Books> = ArrayList();
        if(allBooks.size>0&&separationType!="Popular"){
            for(book:Books in allBooks){
                when(book.Genre){
                    separationType->filterBooks.add(book)
                }
            }
            homeView.addBooks(filterBooks)
        }else{
            homeView.addBooks(allBooks)
        }
    }

    override fun getAllBooks(){
        val nodes:Array<String> = arrayOf("ComedyGenre",
            "DramaGenre","FantasyGenre","Horror","MysteryGenre","ScienceFictionGenre");
        val myref = FirebaseDatabase.getInstance().getReference("Books");

        Thread(object :Runnable{
            override fun run() {
                for(node:String in nodes){
                    var ref = myref.child(node);
                    ref.addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                for(bookSnapShot in snapshot.children){
                                    val book = bookSnapShot.getValue(Books::class.java)
                                    if (book != null) {
                                        allBooks.add(book)
                                        var name = book?.Name
                                        if (name != null) {
                                            Log.e("Name:- ",name)
                                        }else{
                                            Log.e("Name:- ","Null")
                                        }
                                    }
                                }
                                homeView.addBooks(allBooks)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Cancelled:- ",error.details)
                        }
                    })
                }
            }
        }).start()
    }

    var timer:Timer = Timer();
    override fun filterBooks(input: String?, list: ArrayList<Books>) {
        if(allBooks.size>0 && !TextUtils.isEmpty(input) && input!=null){
            timer.cancel()
            timer = Timer();

            timer.schedule(object: TimerTask(){
                override fun run() {
                    homeView.addBooks(queryList(input));
                }
            },600)
        }else{
            homeView.addBooks(list);
        }
    }

    fun queryList(input:String):ArrayList<Books>{
        var filterBooks:ArrayList<Books> = ArrayList();
        for(book in allBooks){
            if(book.Name?.toUpperCase()!!.contains(input.toUpperCase()) || book.Genre?.toUpperCase()!!.contains(input.toUpperCase())){
                filterBooks.add(book)
            }
        }
        return filterBooks;
    }

}