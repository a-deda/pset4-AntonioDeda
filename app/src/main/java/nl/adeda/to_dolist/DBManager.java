package nl.adeda.to_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Antonio on 7-3-2017.
 */

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    protected int databaseLength;

    // Constructor
    public DBManager(Context c) {
        context = c;
    }

    // Open database connection
    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }

    // Insert item into database
    public void insert(String task, int done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, task);
        contentValues.put(DatabaseHelper.DONE, done);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    // Get items from database
    public Cursor fetch() {
        String[] columns = new String[] {
                DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DONE
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null,
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        databaseLength = cursor.getCount();

        return cursor;
    }

    // Update database
    public int update(long _id, String task, int done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, task);
        contentValues.put(DatabaseHelper.DONE, done);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues,
                DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    // Delete database
    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID +
                " = " + _id, null);
    }
}
