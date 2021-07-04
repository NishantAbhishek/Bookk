package com.example.bookk.Presenter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.bookk.Contract.NoteContract
import com.example.bookk.Helper
import com.example.bookk.Model.FavoriteModel
import com.example.bookk.Model.NoteModel
import com.example.bookk.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotePresenter(var noteView:NoteContract.View,var context: Context?):NoteContract.Presenter {
    private var noteList:ArrayList<NoteModel> = ArrayList();
    override fun getAllNotes(){
        if(Helper.getUid()!=null){
            var reference = FirebaseDatabase.getInstance().reference.child("Note").child(Helper.getUid()!!)
            reference.addValueEventListener(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError){
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        noteList.clear()
                        for(noteSnapshot in snapshot.children){
                            var note = noteSnapshot.getValue(NoteModel::class.java);
                            if(note!=null){
                                noteList.add(note)
                            }else{
                                Log.e("Note:- ","Null");
                            }
                        }
                        noteView.addNote(noteList);
                    }
                }
            })
        }
    }

    override fun deleteNote(note: NoteModel, size: Int) {
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.message)
        dialog.findViewById<TextView>(R.id.message).text = "Sure want to delete this Note!!"
        dialog.show()
        dialog.findViewById<Button>(R.id.submit).setOnClickListener{
            if(size==1){
                noteView.removelastData()
            }
            dialog.dismiss()
            deleteFromDatabase(note)
        }
    }

    fun deleteFromDatabase(note: NoteModel){
        if(Helper.getUid()!=null){
            var progressDialog = openProgressDialog();
            var reference = FirebaseDatabase.getInstance().reference.child("Note").child(Helper.getUid()!!).child(note.NoteId!!);
            reference.setValue(null).addOnCompleteListener(object :OnCompleteListener<Void>{
                override fun onComplete(task: Task<Void>) {
                    progressDialog.dismiss()
                    if(task.isSuccessful){
                        noteView.showSnackBar("Removed Successfully")
                    }else{
                        noteView.showSnackBar("Sorry!! some problem Occured")
                    }
                }
            })
        }
    }

    fun openProgressDialog():Dialog{
        var dialog = Dialog(context!!);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false)
        dialog.show();
        return dialog;
    }

    override fun editNote(note: NoteModel) {
        var editDialog = Dialog(context!!);
        editDialog.setContentView(R.layout.note_dialog)
        editDialog.findViewById<EditText>(R.id.note).setText(note.Note)
        editDialog.findViewById<Button>(R.id.submit).setOnClickListener{
            note.Note = editDialog.findViewById<EditText>(R.id.note).text.toString()
            editDialog.dismiss()
            editDatabase(note)
        }
        editDialog.show()
    }


    fun editDatabase(note: NoteModel){
        if(Helper.getUid()!=null){
            var progressDialog = openProgressDialog();
            var reference = FirebaseDatabase.getInstance().reference.child("Note").child(Helper.getUid()!!).child(note.NoteId!!);
            reference.setValue(note).addOnCompleteListener(object :OnCompleteListener<Void>{
                override fun onComplete(task: Task<Void>) {
                    progressDialog.dismiss()
                    if(task.isSuccessful){
                        noteView.showSnackBar("Edit success")
                    }else{
                        noteView.showSnackBar("Sorry!! please try again")
                    }
                }
            })
        }
    }
}