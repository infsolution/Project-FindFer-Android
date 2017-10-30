package br.com.findfer.findfer.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.User;

/**
 * Created by infsolution on 16/10/17.
 */

public class UserDao {
    private DAO dao;
    private User user;
    private SQLiteDatabase database;
    public UserDao(Context context){
        dao = new DAO(context);
    }
    public void open ()throws SQLException {
        database = dao.getWritableDatabase();
    }
    public void close(){
        dao.close();
    }
    public long insert(User user){
        long res=0;
        if(getUser().getNameUser().equals("Default")){
            ContentValues cv = new ContentValues();
            cv.put("name",user.getUser());
            cv.put("name_user",user.getNameUser());
            cv.put("password",user.getPassword());
            cv.put("account",user.getAccount().getTypeAccount());
            cv.put("market_place",user.getMarketPlace());
            res = dao.getWritableDatabase().insert("user",null,cv);
            dao.close();
        }
        return res;
    }
    public User getUser(){
        String sql = "SELECT * FROM user;";
        Cursor c = dao.getReadableDatabase().rawQuery(sql, null);
        if(c != null && c.moveToFirst()){
            user = new User("");
            user.setIdUser(c.getInt(c.getColumnIndex("id_user")));
            user.setUser(c.getString(c.getColumnIndex("name")));
            user.setNameUser(c.getString(c.getColumnIndex("name_user")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            //user.setAccount();
            user.setMarketPlace(c.getLong(c.getColumnIndex("market_place")));
            return user;
        }
        return user = new User("Default");
    }
    /*public boolean getNameUser(String nameUser){
        String sql = "SELECT id_user FROM user WHERE name_user = '"+nameUser+"';";
        Cursor c = dao.getReadableDatabase().rawQuery(sql, null);
        return c == null;
    }*/
    public int getState(){
        String sql = "SELECT * FROM log;";
        Cursor c = dao.getReadableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex("loged"));
    }

}
