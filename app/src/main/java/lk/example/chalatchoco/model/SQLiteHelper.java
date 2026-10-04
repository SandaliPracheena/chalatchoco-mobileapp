package lk.example.chalatchoco.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chalatchoco.db";
    private static final int DATABASE_VERSION = 2;

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("CREATE TABLE product (\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    price TEXT    NOT NULL,\n" +
                "    image BLOB    NOT NULL\n" +
                ");");


        sqLiteDatabase.execSQL("CREATE TABLE cart (\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                "    product_id  INTEGER    NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    user  TEXT    NOT NULL,\n" +
                "    price TEXT    NOT NULL,\n" +
                "    image BLOB    NOT NULL\n" +
                ");");



            sqLiteDatabase.execSQL("CREATE TABLE orders (\n" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    product_id TEXT    NOT NULL,\n" +
                    "    name       TEXT    NOT NULL,\n" +
                    "    user       TEXT    NOT NULL,\n" +
                    "    mobile     TEXT    NOT NULL,\n" +
                    "    type       TEXT    NOT NULL,\n" +
                    "    price      TEXT    NOT NULL,\n" +
                    "     image     BLOB    NOT NULL\n"+
                    ");\n");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (oldVersion < 2) {

            sqLiteDatabase.execSQL("CREATE TABLE cart (\n" +
                    "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    "                  NOT NULL,\n" +
                    "    product_id  INTEGER    NOT NULL,\n" +
                    "    name  TEXT    NOT NULL,\n" +
                    "    user  TEXT    NOT NULL,\n" +
                    "    price TEXT    NOT NULL,\n" +
                    "    image BLOB    NOT NULL\n" +
                    ");");

            sqLiteDatabase.execSQL("CREATE TABLE orders (\n" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    product_id TEXT    NOT NULL,\n" +
                    "    name       TEXT    NOT NULL,\n" +
                    "    user       TEXT    NOT NULL,\n" +
                    "    mobile     TEXT    NOT NULL,\n" +
                    "    type       TEXT    NOT NULL,\n" +
                    "    price      TEXT    NOT NULL,\n" +
                    "     image     BLOB    NOT NULL\n"+
                    ");\n");



        }





    }


    // Method to insert an image
    public void insertImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image);
        db.insert("chalatchoco.db", null, contentValues);
    }

    // Method to retrieve an image by id
//    public byte[] getImage(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_IMAGE},
//                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
//                null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
//            cursor.close();
//            return image;
//        }
//        return null;
//    }
}
