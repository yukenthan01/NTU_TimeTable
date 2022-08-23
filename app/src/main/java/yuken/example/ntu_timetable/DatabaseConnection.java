package yuken.example.ntu_timetable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection {
    private SQLiteOpenHelper openHelper;
    public SQLiteDatabase database;
    private  static DatabaseConnection instance;
    private Cursor cursor = null;
    private String status = "Active";
    String encryptedPassword = null;

    public DatabaseConnection(Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseConnection getInstance(Context context)
    {
        if(instance==null)
        {
            instance = new DatabaseConnection(context);
        }
        return instance;
    }

    //opening the db
    public void openDatabase(){
        this.database=openHelper.getWritableDatabase();
    }

    //closing the db
    public void closeDatabase()
    {
        if(database!=null)
        {
            database.close();
        }
    }

}
