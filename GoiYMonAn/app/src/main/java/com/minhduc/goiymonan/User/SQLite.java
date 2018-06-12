package com.minhduc.goiymonan.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by minhduc on 5/15/2017.
 */

public class SQLite extends SQLiteOpenHelper {
    // name là tên database của mình, SQLiteDatabase.CursorFactory factory : là dạng con trỏ để chỉ tới dữ liệu để duyệt dữ liệu, int version: phiên bản SQLite bao nhiêu
    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // truy vấn không trả kết quả
    //tạo hàm tạo database với tham số truyền vào là sql
    public void QueryData(String sql )
    {
        // tạo database
        SQLiteDatabase database = getWritableDatabase();  //getRead là đọc, getWrite là ghi và đọc được
        // thực thi lệnh
        database.execSQL(sql);
    }
    // Tạo hàm lấy dữ liệu database giờ con trỏ Cursor lấy được dữ liệu gì thì mình gọi nó để lấy nó ra
    public Cursor GetData(String sql)
    {
        // có thể dùng getRead hoặc getWrite vì cả 2 đều đọc được
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

