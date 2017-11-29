package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cloudstorage.UploadCloud;

/**
 * Created by liyang on 11/29/17.
 */
public class DBService {

    DBHelper dbHelper;

    public DBService(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean fileIsExists(String[] fileName) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DBContract.FileEntry._ID,
                DBContract.FileEntry.COLUMN_NAME,
                DBContract.FileEntry.COLUMN_STATUS
        };
        String selection = DBContract.FileEntry.COLUMN_NAME + " = ?";
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DBContract.FileEntry.COLUMN_NAME + " DESC";

        Cursor cursor = db.query(
                DBContract.FileEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                fileName,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DBContract.FileEntry._ID));
            itemIds.add(itemId);
        }

        cursor.close();

        return itemIds.size() > 0;
    }

    public boolean addTransmitRecord(String fileName) {
// Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.FileEntry.COLUMN_NAME, "");
        values.put(DBContract.FileEntry.COLUMN_STATUS, -1);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBContract.FileEntry.TABLE_NAME, null, values);

        return newRowId > 0;
    }

    public boolean modifyTransmitRecord(UploadCloud uploadCloud) {
        return true;
    }
}
