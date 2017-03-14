package nl.adeda.to_dolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Antonio on 7-3-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    // Define database data
    public static final String DB_NAME = "TODOLISTDB";
    public static final int DB_VERSION = 3;

    public static final String _ID = "_id";
    public static final String TABLE_NAME = "TODOLIST";
    public static final String SUBJECT = "subject";
    public static final String DONE = "done";

    // Create table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, "
            + DONE + " INTEGER" + ");";

    // Example task query
    private static final String SET_EXAMPLE_TASK_1 = "INSERT INTO " + TABLE_NAME +
            " (subject, done) VALUES ('Get groceries', 0);";
    private static final String SET_EXAMPLE_TASK_2 = "INSERT INTO " + TABLE_NAME +
            " (subject, done) VALUES ('Walk the dog', 0);";
    private static final String SET_EXAMPLE_TASK_3 = "INSERT INTO " + TABLE_NAME +
            " (subject, done) VALUES ('Pay rent for February', 0);";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Create table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

        // Set example tasks
        db.execSQL(SET_EXAMPLE_TASK_1);
        db.execSQL(SET_EXAMPLE_TASK_2);
        db.execSQL(SET_EXAMPLE_TASK_3);
    }

    // Update table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
