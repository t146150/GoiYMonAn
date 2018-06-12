package com.minhduc.goiymonan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DanhSachMonNgonActivity extends AppCompatActivity {

    ListView lvSanPham;
    ArrayList<MonAn> arraySanPham;
    MonAnAdapter adapter;
    DatabaseReference mData;
    String idSP = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_mon_ngon);
        Intent intent = getIntent();
        mData = FirebaseDatabase.getInstance().getReference();

        lvSanPham = (ListView) findViewById(R.id.listviewMonNgon);
        arraySanPham = new ArrayList<>();

        adapter = new MonAnAdapter(this, R.layout.dong_mon_an, arraySanPham);
        lvSanPham.setAdapter(adapter);


        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
//                Intent intent = new Intent(DanhSachMonNgonActivity.this, MainActivity.class);
//                intent.putExtra("idCuaSanPham", arraySanPham.get(i).IdSP);
//                startActivity(intent);
                idSP =   arraySanPham.get(i).IdSP;
                MonNgon monNgon = new MonNgon(
                        null,
                        idSP,
                        arraySanPham.get(i).TenSP,
                        arraySanPham.get(i).GiaSP,
                        arraySanPham.get(i).HinhSP,
                        arraySanPham.get(i).MoTa
                );
                mData.child("MonNgon").push().setValue(monNgon, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){
                            Toast.makeText(DanhSachMonNgonActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DanhSachMonNgonActivity.this, MainActivity.class);
                            intent.putExtra("idCuaMonNgon", arraySanPham.get(i).IdSP);
                            setResult(RESULT_OK,intent);
                            startActivity(intent);
                        }else {
                            Toast.makeText(DanhSachMonNgonActivity.this, "Lỗi thêm món!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // đọc database từ node SanPham
        mData.child("MonAn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonAn monAn = dataSnapshot.getValue(MonAn.class);
                arraySanPham.add(monAn);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}