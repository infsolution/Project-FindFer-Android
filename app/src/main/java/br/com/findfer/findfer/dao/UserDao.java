package br.com.findfer.findfer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import br.com.findfer.findfer.model.User;

/**
 * Created by infsolution on 10/11/17.
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
        if(getUser() == null){
            ContentValues cv = new ContentValues();
            cv.put("name",user.getName());
            cv.put("fone",user.getFone());
            cv.put("password",user.getPassword());
            cv.put("type_account",user.getTypeAccount());
            cv.put("id_market",user.getIdMarket());
            cv.put("image",user.getImage());
            cv.put("id_coordinates", user.getIdCoordinate());
            cv.put("register_date",user.getDateRegister());
            cv.put("id_market",user.getIdMarket());
            cv.put("cod_user",user.getCodUser());
            cv.put("actual_latitude",user.getActualLatitude());
            cv.put("actual_longitude",user.getActualLongitude());
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
            user.setCodUser(c.getInt(c.getColumnIndex("cod_user")));
            user.setName(c.getString(c.getColumnIndex("name")));
            user.setFone(c.getString(c.getColumnIndex("fone")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            user.setTypeAccount(c.getInt(c.getColumnIndex("type_account")));
            user.setImage(c.getString(c.getColumnIndex("image")));
            user.setIdCoordinate(c.getLong(c.getColumnIndex("id_coordinates")));
            user.setEmail(c.getString(c.getColumnIndex("email")));
            user.setDateRegister(c.getString(c.getColumnIndex("register_date")));
            user.setIdMarket(c.getLong(c.getColumnIndex("id_market")));
            user.setActualLatitude(c.getDouble(c.getColumnIndex("actual_latitude")));
            user.setActualLongitude(c.getDouble(c.getColumnIndex("actual_longitude")));
            return user;
        }
        return null;
    }

    public long updateUser(User user){
        ContentValues cv = new ContentValues();
        cv.put("password",user.getPassword());
        cv.put("email",user.getEmail());
        cv.put("type_account",user.getTypeAccount());
        cv.put("id_market",user.getIdMarket());
        return dao.getWritableDatabase().update("user",cv,null,null);
    }
    public long updateCoordinate(User user){
        ContentValues cv = new ContentValues();
        cv.put("actual_latitude",user.getActualLatitude());
        cv.put("actual_longitude",user.getActualLongitude());
       return dao.getWritableDatabase().update("user",cv,null,null);
    }

}
