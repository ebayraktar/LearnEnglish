package com.bayraktar.learnenglish.API;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.Interfaces.IFirebase;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseHelper {

    private final String TAG = "TAG";

    private final String WORDS = "words";

    private IFirebase firebaseListener;
    private CollectionReference WORDS_REF;
    private Context context;

    public FirebaseHelper(Context context, IFirebase firebaseListener) {
        this.context = context;
        this.firebaseListener = firebaseListener;
    }

    public void initialize() {
        FirebaseApp.initializeApp(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WORDS_REF = db.collection(WORDS);
    }

    public void searchWord(String word) {
        WORDS_REF.whereEqualTo("word", word).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert null != queryDocumentSnapshots;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Word word = documentSnapshot.toObject(Word.class);
                    Log.d(TAG, "searchWord onEvent: " + word.getEn());
                }
            }
        });
    }

    public void searchSpeech(String parts_of_speech) {
        WORDS_REF.whereEqualTo("parts_of_speech", parts_of_speech).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert null != queryDocumentSnapshots;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Word word = documentSnapshot.toObject(Word.class);
                    Log.d(TAG, "searchSpeech onEvent: " + word.getEn());
                }
            }
        });
    }

    public void updateTR(String word, final String tr) {
        WORDS_REF.whereEqualTo("word", word).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert null != queryDocumentSnapshots;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Word word = documentSnapshot.toObject(Word.class);
                    WORDS_REF.document(WORDS).update("tr", tr);
                }
            }
        });
    }

    public void getWordList() {
        WORDS_REF.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Word> words = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if (document.exists()) {
                                    words.add(document.toObject(Word.class));
                                }
                            }
                            firebaseListener.onGetWordList(words);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            firebaseListener.onGetWordList(null);
                        }
                    }
                });
    }

    public void setWord(final Word word) {
        Map<String, Object> value = new HashMap<>();
        value.put("en", word.getEn());
        value.put("parts_of_speech", word.getParts_of_speech());
        value.put("tr", word.getTr());
        value.put("sp", word.getSp());
        WORDS_REF.add(value)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firebaseListener.onSetWord(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firebaseListener.onSetWord(false);
                    }
                });
    }
}