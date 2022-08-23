package yuken.example.ntu_timetable;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class LoginDataLayer {

    private SQLiteOpenHelper openHelper;
    public static DatabaseConnection databaseConnection;
    private  static LoginDataLayer instance;
    private Cursor cursor = null;
    private String status = "Active";
    String encryptedPassword = null;

    public String login(String username, String password, Activity currentActivity) throws GeneralSecurityException {
        databaseConnection = databaseConnection.getInstance(currentActivity);
        databaseConnection.openDatabase();
        cursor = null;
        encryptedPassword = AESCrypt.encrypt(username,password);
        cursor = databaseConnection.database.rawQuery("select * from users where universityId = ?" +
                " and password = ? and " +
                "status =" +
                " ?", new String[]{username,encryptedPassword, status});
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext()){
                return cursor.getString(6);
            }
        }
        else
        {
            return "false";
        }
        databaseConnection.closeDatabase();
        return "false";
    }


}
