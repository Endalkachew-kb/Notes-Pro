package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
        EditText title ,content;
        TextView pageTitleTextView;
        ImageButton saveNote;
        String PTitle,PContent,docId;
        boolean isEditMode=false;
        TextView delete;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        title=findViewById(R.id.notes_title_text);
        content=findViewById(R.id.notes_content_text);
        saveNote=findViewById(R.id.save_note_btn);
        pageTitleTextView=findViewById(R.id.page_title);
        delete=findViewById(R.id.delete_note_text_view_btn);

        //receive data
        PTitle=getIntent().getStringExtra("title");
        PContent=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }
        title.setText(PTitle);
        content.setText(PContent);
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            delete.setVisibility(View.VISIBLE);
        }


        saveNote.setOnClickListener((v)-> saveNote());
        delete.setOnClickListener((v)->deleteNoteFromFirebase());
            }
        void saveNote(){
            String noteTitle=title.getText().toString();
            String noteContent=content.getText().toString();
            if(noteTitle==null||noteTitle.isEmpty()){
                title.setError("Title is required");
                return;
            }
            Note note=new Note();
            note.setTitle(noteTitle);
            note.setContent(noteContent);
            note.setTimestamp(Timestamp.now());
            saveNoteToFirebase(note);

        }

    void saveNoteToFirebase(Note note){
      DocumentReference documentReference;
      if(isEditMode){
          //update the note
          documentReference=Utility.getCollectionReferenceForNotes().document(docId);
      }else{
          //create new note
          documentReference=Utility.getCollectionReferenceForNotes().document();
      }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                    finish();
                }else{
                    //notes is not added
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding Note ");
                }
            }
        });
    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

            documentReference=Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    //notes is not added
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting Note ");
                }
            }
        });
    }
}