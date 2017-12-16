package br.com.findfer.findfer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by infsolution on 10/11/17.
 */

public class DAO extends SQLiteOpenHelper {
    public static final String dbName = "findferdb";
    public static int versionDb = 1;
    public DAO(Context context) {
        super(context, dbName, null, versionDb);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user(id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255) NOT NULL, fone VARCHAR(18) NOT NULL, password VARCHAR(12) NOT NULL, " +
                "type_account INTEGER NOT NULL, id_market INTEGER NOT NULL , " +
                "id_coordinates INTEGER NO NULL, email VARCHAR(255), image VARCHAR(255), " +
                "register_date VARCHAR(255) NOT NULL, cod_user INTEGER NOT NULL, actual_latitude REAL, actual_longitude REAL);";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql="DROP TABLE IF EXISTS user;";
        db.execSQL(sql);
    }
}
