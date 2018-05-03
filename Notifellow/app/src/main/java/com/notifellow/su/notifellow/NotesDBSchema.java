package com.notifellow.su.notifellow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 2/28/2017.
 */

public class NotesDBSchema extends SQLiteOpenHelper {

    private static NotesDBSchema schemaInstance;

    private static final String TAG = NotesDBSchema.class.getSimpleName();

    private static final String TABLE_NAME = "NOTE_TABLE";
    private static final String ID = "ID";
    private static final String TITLE = "tittle";
    private static final String NOTE = "note";
    private static final String EMAIL = "email";
    private static final String IMAGE_PATH = "image_path";
    private static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            NOTE + " TEXT, " +
            IMAGE_PATH + " TEXT, " +
            EMAIL + " TEXT);";
//            "PRIMARY KEY (" + EMAIL + ", " + TITLE + "));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//

    NotesDBSchema(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    public static NotesDBSchema getInstance(Context context) {
        if (schemaInstance == null) {
            schemaInstance = new NotesDBSchema(context.getApplicationContext());
        }
        return schemaInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + NOTE + " TEXT, " + IMAGE_PATH + " TEXT );";
//        db.execSQL(createTable);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean addData(String tittle, String note, String itemPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

//        contentValues.put(ID, id);
        contentValues.put(TITLE, tittle);
        contentValues.put(NOTE, note);
        contentValues.put(IMAGE_PATH, itemPath);

        Log.d(TAG, "addData: Adding " + tittle + "\n" + note + "\nand \n" + itemPath + "\nto " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        //if date as inserted incorrectly it will return -1
        //return result != -1;
        return (int) result != -1;
    }

    /**
     * Returns all the data from database
     *
     * @return
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    /**
     * Returns only the ID that matches the name passed in
     *
     * @param name
     * @return
     */
    public Cursor getItemID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + TITLE + " = '" + name + "'";
        return db.rawQuery(query, null);
    }

    /**
     * Returns only the Image Path that matches the id passed in
     *
     * @param id
     * @return
     */
    public Cursor getItemImagePath(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + IMAGE_PATH + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        return db.rawQuery(query, null);
    }

    /**
     * Updates the name field
     *
     * @param id
     * @param newName
     * @param oldName
     */
    public void updateName(int id, String newName, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + NOTE + " = '" + newName + "' WHERE " + ID + " = '" + id + "'" + " AND NOT " + NOTE + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting NOTE to " + newName);
        db.execSQL(query);
    }

    /**
     * Updates the IMAGE_PATH field
     *
     * @param id
     * @param newImagePath //     * @param oldImagePath
     */
    public void updateImagePath(int id, String newImagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, newImagePath);
        Log.d(TAG, "updateName: Setting imagepath to " + IMAGE_PATH);
        db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[]{String.valueOf(id)});
    }

    /**
     * Updates the NOTE field
     *
     * @param id
     * @param newNote
     * @param oldNote
     */
    public void updateNote(int id, String newNote, String oldNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + TITLE + " = '" + newNote + "' WHERE " + ID + " = '" + id + "'" + " AND " + TITLE + " = '" + oldNote + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newNote);
        db.execSQL(query);
    }

//    public Cursor getAllRowsForEvents() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String[] columns = {ID, TITLE, NOTE, IMAGE_PATH};
//        return db.query(TABLE_NAME, columns, null, null, null, null, null);
//    }
//    /**
//     * Delete from database
//     *
//     * @param id        int
//     * @param tittle     String
//     * @param note      String
//     * @param imagePath String
//     */
//    public void deleteName(int id, String tittle, String note, String imagePath) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = new StringBuilder()
//                .append("DELETE FROM ")
//                .append(TABLE_NAME)
//                .append(" WHERE ")
//                .append(ID)
//                .append(" = '")
//                .append(id)
//                .append("' AND ")
//                .append(TITLE)
//                .append(" = '")
//                .append(tittle)
//                .append("' AND ")
//                .append(NOTE)
//                .append(" = ' ")
//                .append(note)
//                .append(" ' AND ")
//                .append(IMAGE_PATH)
//                .append(" = ' ")
//                .append(imagePath)
//                .append("'").toString();
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting item with id: " + id + " from database.");
//        db.execSQL(query);
//    }

    public String getTitle(String ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{TITLE}, this.ID + " = ? ", new String[]{ID}, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        String title = cursor.getString(cursor.getColumnIndex(TITLE));
        return title;
    }

    public String getEmail(String ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{EMAIL}, this.ID + " = ? ", new String[]{ID}, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        String email = cursor.getString(cursor.getColumnIndex(EMAIL));
        return email;
    }

    public int deleteByID(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int num = db.delete(TABLE_NAME, ID + " = ? ", new String[]{id});
        return num;
    }

    public int deleteByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int num = db.delete(TABLE_NAME, EMAIL + " = ? ", new String[]{email});
        return num;
    }


    public long updateEmailAddresses(String oldMail, String newMail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, newMail);
        Log.i("AlarmDBSchema: ", "Next statement is Update EMAIL!!!");
        return db.update(TABLE_NAME, contentValues, EMAIL + " = ? ", new String[]{oldMail});
    }
}