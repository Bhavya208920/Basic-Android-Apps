package com.example.contentproviderdemo1.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.contentproviderdemo1.model.Util;

public class MyContentProvider extends ContentProvider {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tabName = uri.getLastPathSegment();
        int i = sqLiteDatabase.delete(tabName, selection, null);
        return i;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tabName = uri.getLastPathSegment();
        long id = sqLiteDatabase.insert(tabName,null,values);

        Uri dummyUri = Uri.parse("dummy://someuri/"+id);

        return dummyUri;
    }

    @Override
    public boolean onCreate() {
     dbHelper = new DBHelper(getContext(), Util.DB_NAME,null,Util.DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tabName = uri.getLastPathSegment();
        Cursor cursor = sqLiteDatabase.query(tabName,projection,null,null,null,null,null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tabName = uri.getLastPathSegment();
        int i = sqLiteDatabase.update(tabName, values, selection, null);
        return i;
    }

// Nested Class
class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        // super call will create a database along with version for us

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(Util.CREATE_TAB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
}
