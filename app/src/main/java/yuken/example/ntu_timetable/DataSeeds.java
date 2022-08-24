package yuken.example.ntu_timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public ArrayList<String> moduleName(ArrayList<String> moduleId,Activity currentActivity)
    {
        databaseConnection = databaseConnection.getInstance(currentActivity);
        databaseConnection.openDatabase();
        ArrayList<String> moduleName = new ArrayList<>();
        String[] moduleIdArray = moduleId.toArray(new String[moduleId.size()]);

        cursor =
                databaseConnection.database.rawQuery("select * from module where id IN ("+ Arrays.toString(moduleIdArray).substring(1,Arrays.toString(moduleIdArray).length()-1)+
                                ")" +
                                " and status " +
                                "= ?",
                        new String[]{status});

        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext()){
                moduleName.add(cursor.getString(cursor.getColumnIndex("moduleName")));

            }
        }
        return moduleName;
    }
    public ArrayList<String> moduleId(String batchId, Activity currentActivity)
    {
        databaseConnection = databaseConnection.getInstance(currentActivity);
        databaseConnection.openDatabase();
        ArrayList<String> moduleId = new ArrayList<>();
        cursor = databaseConnection.database.rawQuery("select * from batchModule where batchId = ? and " +
                    "status = ?",
                new String[]{batchId, status});

        if(cursor.getCount()>0)
        {

            while (cursor.moveToNext()){
                // Log.d("TAG", "moduleId: "+c.getString(c.getColumnIndex("moduleId")));
                moduleId.add(cursor.getString(cursor.getColumnIndex("moduleId")));

            }
        }
        databaseConnection.closeDatabase();
        return moduleId;
    }
    @SuppressLint("Range")
    public int findBatchIdByName(String batchName, Activity currentActivity){
        databaseConnection = databaseConnection.getInstance(currentActivity);
        databaseConnection.openDatabase();
        int batchId = 0;
        cursor = databaseConnection.database.rawQuery("select * from batch where name = ? and status" +
                        " = ?",
                new String[]{batchName, status});

        if(cursor.getCount()>0)
        {

            while (cursor.moveToNext()){
                batchId =  Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            }
        }
        databaseConnection.closeDatabase();
        return batchId;
    }
    @SuppressLint("Range")
    public ArrayList<String> batchName(Activity currentActivity)
    {
        databaseConnection = databaseConnection.getInstance(currentActivity);
        databaseConnection.openDatabase();

        ArrayList<String> batchName = new ArrayList<>();
        cursor = databaseConnection.database.rawQuery("select * from batch where status = ?",
                new String[]{status});

        if(cursor.getCount()>0)
        {

            while (cursor.moveToNext()){
                batchName.add(cursor.getString(cursor.getColumnIndex("name")));

            }
        }
        return batchName;
    }
}
