package yuken.example.ntu_timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String result  = null;
    String finalUserRole ;
    public interface fullNameCallBack {
        void onCallback(String fullName);
    }
    public void getFullName(fullNameCallBack fullnameCallBack){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullname =
                        documentSnapshot.getString("firstname").toString() +" "+documentSnapshot.getString("lastname").toString();
                fullnameCallBack.onCallback(fullname);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    public void checkUserLevel(callBackUserLevel callBackUserLevel){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("userRole").toString().equals("admin")){
                    finalUserRole = "admin";
                    callBackUserLevel.onCallback(finalUserRole);
                }
                else if(documentSnapshot.getString("userRole") .equals("student")){
                    finalUserRole = "student";
                    callBackUserLevel.onCallback(finalUserRole);

                }
                else if(documentSnapshot.getString("userRole") .equals("lecturer")){
                    finalUserRole = "lecturer";
                    callBackUserLevel.onCallback(finalUserRole);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    public interface callBackUserLevel {
        void onCallback(String userRole);
    }
    public interface getValueCallback {
        void onCallback(String fieldValues);
    }
    public void getValueByField(
            getValueCallback getLectureIdCallback,String table,String field,String documentId){
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

    public interface getTheValueByFieldCallBack {
        void onCallback(String fieldValues);
    }

    public void getFiledValue(
            getTheValueByFieldCallBack getTheValueByFieldCallBack,String table,String field,
            String value){
        firebaseFirestore.collection(table)
            .whereEqualTo(field,value)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){

                           for (QueryDocumentSnapshot document : task.getResult()) {
                               result = document.getId();
                           }
                           getTheValueByFieldCallBack.onCallback(result);
                       }
                   }
               }
            );
    }
}
