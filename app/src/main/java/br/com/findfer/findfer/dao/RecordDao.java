package br.com.findfer.findfer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import br.com.findfer.findfer.model.Record;

/**
 * Created by infsolution on 28/10/17.
 */

public class RecordDao {
    private DAO dao;
    private Record record;
    private SQLiteDatabase database;
    public RecordDao(Context context){
        dao = new DAO(context);
    }

    public void open ()throws SQLException {
        database = dao.getWritableDatabase();
    }
    public void close(){
        dao.close();
    }
    public long insertRecord(Record record){
        long ret = 0;
        if(getRecord() == null){
            ContentValues cv = new ContentValues();
            cv.put("name", record.getName());
            cv.put("fone",record.getFone());
            cv.put("code",record.getCode());
            ret = dao.getWritableDatabase().insert("record",null,cv);
            dao.close();
            return ret;
        }
        return -1;
    }
    public Record getRecord(){
        String sql = "SELECT * FROM record";
        Cursor c = dao.getReadableDatabase().rawQuery(sql, null);
        if(c != null && c.moveToFirst()){
            Record rec = new Record(c.getString(c.getColumnIndex("fone")));
            rec.setName(c.getString(c.getColumnIndex("name")));
            rec.setCode(c.getString(c.getColumnIndex("code")));
            return rec;
        }
        return null;
    }

}
