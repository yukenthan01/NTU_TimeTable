package yuken.example.ntu_timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSeeds {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String result  = null;
    public interface getLectureIdCallback {
        void onCallback(String fieldValues);
    }
    public void getValueByField(
            getLectureIdCallback getLectureIdCallback,String table,String field,String documentId){
        firebaseFirestore.collection(table)
        .whereEqualTo(FieldPath.documentId(),documentId)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful()){

                       for (QueryDocumentSnapshot document : task.getResult()) {
                           result = document.getString(field);
                       }
                       getLectureIdCallback.onCallback(result);
                   }
               }
           }
        );
    }
}
