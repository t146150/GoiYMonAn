package com.minhduc.goiymonan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minhduc.goiymonan.User.ContactActivity;
import com.minhduc.goiymonan.User.DanhSachVideoActivity;
import com.minhduc.goiymonan.User.GioHangActivity;
import com.minhduc.goiymonan.User.UserMainActivity;

import java.util.ArrayList;

public class LoaiMonAnActivity extends AppCompatActivity {
    Button btnThemLoai;
    ListView lvLoaiSP;
    ArrayList<LoaiMonAn> arrayLoaiSP;
    LoaiMonAnAdapter adapter;

    DatabaseReference mData;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_mon_an);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        btnThemLoai = (Button) findViewById(R.id.buttonThemLoaiMonAn) ;
        NavigationView();
        mData = FirebaseDatabase.getInstance().getReference();

        lvLoaiSP = (ListView) findViewById(R.id.listviewLoaiMonAn);
        arrayLoaiSP = new ArrayList<>();

        adapter = new LoaiMonAnAdapter(this, R.layout.dong_loai_mon_an, arrayLoaiSP);
        lvLoaiSP.setAdapter(adapter);
        btnThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoaiMonAnActivity.this, NhapLoaiMonAnActivity.class);
                startActivity(intent);
            }
        });
        lvLoaiSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LoaiMonAnActivity.this, MonAnActivity.class);
                intent.putExtra("idCuaLoaiSP", arrayLoaiSP.get(i).IdLoai);
                startActivity(intent);
            }
        });

        // đọc dữ liệu từ firebasbe -> node LoaiSanPham
        mData.child("LoaiSanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LoaiMonAn loaiSP = dataSnapshot.getValue(LoaiMonAn.class);
                arrayLoaiSP.add(loaiSP);
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


        navigationView.setNavigationItemSelectedListener(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuTrangChu:
                        Intent intent1 = new Intent(LoaiMonAnActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        Intent intent2 = new Intent(LoaiMonAnActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuDatHang:
                        Intent intent3 = new Intent(LoaiMonAnActivity.this, DanhSachDatHangActivity.class);
                        startActivity(intent3);
                        break;


                }

                return false;
            }
        });
    }
    public void NavigationView()
    {
        setSupportActionBar(toolbar);
        //set màu của bar
        toolbar.setBackgroundColor(Color.BLUE);
        //enable cái icon lên
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // chèn icon ba gạch
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);

        // sự kiện khi nhấn nút hiện ra cái navigation
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chạy cái navigation ra thông qua cái Drawer
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        //set lại màu

    }
}