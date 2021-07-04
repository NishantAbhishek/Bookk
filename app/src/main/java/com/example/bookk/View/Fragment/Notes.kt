package com.example.bookk.View.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookk.Adapter.NoteAdapter
import com.example.bookk.Callbacks.NoteCallback
import com.example.bookk.Contract.NoteContract
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Model.NoteModel
import com.example.bookk.Presenter.NotePresenter
import com.example.bookk.R
import com.google.android.material.snackbar.Snackbar

class Notes : Fragment(),NoteContract.View,NoteCallback{
    private lateinit var adapter:NoteAdapter;
    private var recyclerList:ArrayList<NoteModel> = ArrayList<NoteModel>();
    private lateinit var presenter:NoteContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onStart() {
        super.onStart()
        initialize()
        initializeRecycler()
        presenter.getAllNotes()
    }

    override fun deleteNote(note: NoteModel) {
        presenter.deleteNote(note,recyclerList.size)
    }

    override fun editNote(note: NoteModel) {
        presenter.editNote(note)
    }

    override fun removelastData(){
        recyclerList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun initializeRecycler(){
        var recyclerView: RecyclerView? = view?.findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
    }

    private fun initialize(){
        presenter = NotePresenter(this,context);
        adapter = NoteAdapter(context,recyclerList,this);
    }


    override fun addNote(note: ArrayList<NoteModel>){
        recyclerList.clear()
        recyclerList.addAll(note)
        adapter.notifyDataSetChanged()
    }

    override fun showSnackBar(message: String) {
        if(message.equals("Sorry!! some problem Occured")){
            presenter.getAllNotes()
        }
        Snackbar.make(requireView().findViewById<RelativeLayout>(R.id.layout),message, 350).show()
    }

}