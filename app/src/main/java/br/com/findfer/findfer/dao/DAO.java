package br.com.findfer.findfer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by infsolution on 16/10/17.
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
                "name VARCHAR(255) NOT NULL, name_user VARCHAR(255) NOT NULL, " +
                "password VARCHAR(12) NOT NULL, type_account INTEGER NOT NULL, " +
                "market_place INTEGER NOT NULL , coodinates INTEGER NO NULL);";
        db.execSQL(sql);
        sql ="CREATE TABLE log(loged INTEGER NOT NULL)";
        db.execSQL(sql);
        sql = "insert into log(loged) values(0);";
        db.execSQL(sql);
        sql = "CREATE TABLE coordinates(id_coordinates INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "latitude VARCHAR(255) NOT NULL, logitude VARCHAR(255) NOT NULL, " +
                "id_user INTEGER NOT NULL )";
        db.execSQL(sql);
        sql = "CREATE TABLE record(id_record INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, " +
                "fone VARCHAR(25) NOT NULL, code VARCHAR(6) NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql="DROP TABLE IF EXISTS user;";
        db.execSQL(sql);
        sql="DROP TABLE IF EXISTS log;";
        db.execSQL(sql);
        sql="DROP TABLE IF EXISTS coordinates;";
        db.execSQL(sql);
        sql="DROP TABLE IF EXISTS record;";
        db.execSQL(sql);
    }
}
