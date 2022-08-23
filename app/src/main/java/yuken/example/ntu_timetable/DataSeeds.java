package yuken.example.ntu_timetable;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;

public class DataSeeds {
    Cursor cursor ;

    String encryptedPassword = null;
    private String status = "Active";
    public static DatabaseConnection databaseConnection;


    public void adminAccountCreation(Activity current) throws GeneralSecurityException {
        databaseConnection = databaseConnection.getInstance(current);
        databaseConnection.openDatabase();
        encryptedPassword = AESCrypt.encrypt("N0000000","123456");
        cursor = databaseConnection.database.rawQuery("select * from users where userRole = ? and" +
                " status =" +
                " ?", new String[]{"Admin", status});
        if(cursor.getCount()<=0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("universityId","N0000000");
            contentValues.put("firstName","Admin");
            contentValues.put("lastName","Admin");
            contentValues.put("email","admin@gmail.com");
            contentValues.put("password",encryptedPassword);
            contentValues.put("userRole","Admin");
            contentValues.put("status",status);
            long result = databaseConnection.database.insert("users", null,contentValues);
        }
        databaseConnection.closeDatabase();

    }
}
