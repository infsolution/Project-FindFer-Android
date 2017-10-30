package br.com.findfer.findfer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.User;

/**
 * Created by infsolution on 19/10/17.
 */

public class CoordinateDao {
    private DAO dao;
    private Coordinates coordinates;
    private SQLiteDatabase database;
    public CoordinateDao(Context context){
        dao = new DAO(context);
    }
    public void open ()throws SQLException {
        database = dao.getWritableDatabase();
    }
    public void close(){
        dao.close();
    }
    public long insert(Coordinates coordinates){
        long res=0;
            ContentValues cv = new ContentValues();
            cv.put("latitude",coordinates.getLatitude());
            cv.put("name_user",coordinates.getLongitude());
            cv.put("id_user",coordinates.getUser());
            res = dao.getWritableDatabase().insert("coordinates",null,cv);
            dao.close();
        return res;
    }
    public Coordinates getCoordinateUser(long id){
        String sql = "SELECT * FROM coordinates WHERE id_user = '"+id+"';";
        Cursor c = dao.getReadableDatabase().rawQuery(sql, null);
        if(c != null && c.moveToFirst()) {
            return new Coordinates(c.getDouble(c.getColumnIndex("latitude")), c.getDouble(c.getColumnIndex("longitude")));
        }
        return new Coordinates(0.0,0.0);
    }
}
