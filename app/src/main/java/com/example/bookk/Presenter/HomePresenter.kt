package com.example.bookk.Presenter

import android.content.Context
import android.util.Log
import android.view.View
import com.example.bookk.Contract.HomeContract
import com.example.bookk.Model.Books
import com.example.bookk.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomePresenter(
    private var context: Context?,
    private var homeView: HomeContract.View
) : HomeContract.Presenter {

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
            R.id.liPopular->{homeView.putUnderLine(0)}
            R.id.liComedy->{homeView.putUnderLine(1)}
            R.id.liFantasy->{homeView.putUnderLine(2)}
            R.id.liHorror->{homeView.putUnderLine(3)}
            R.id.liMystery->{homeView.putUnderLine(4)}
            R.id.liDrama->{homeView.putUnderLine(5)}
            R.id.liSciFi->{homeView.putUnderLine(6)}
        }
    }

    override fun getAllBooks(){
        val nodes:Array<String> = arrayOf("ComedyGenre",
            "DramaGenre","FantasyGenre","Horror","MysteryGenre","ScienceFictionGenre");
        val myref = FirebaseDatabase.getInstance().getReference("Books");

        Thread(object :Runnable{
            override fun run() {
                var books:ArrayList<Books> = ArrayList<Books>();
                for(node:String in nodes){
                    var ref = myref.child(node);
                    ref.addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                for(bookSnapShot in snapshot.children){
                                    val book = bookSnapShot.getValue(Books::class.java)
                                    if (book != null) {
                                        books.add(book)
                                        var name = book?.Name
                                        if (name != null) {
                                            Log.e("Name:- ",name)
                                        }else{
                                            Log.e("Name:- ","Null")
                                        }
                                    }
                                }
                                homeView.addBooks(books)
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



}