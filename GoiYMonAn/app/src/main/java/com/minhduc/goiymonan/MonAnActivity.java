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

import java.util.ArrayList;

public class MonAnActivity extends AppCompatActivity {

    ListView lvSanPham;
    ArrayList<MonAn> arraySanPham;
    MonAnAdapter adapter;
    Button btnThemMonMoi;
    DatabaseReference mData;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        NavigationView();

        Intent intent = getIntent();
        final String idLoai = intent.getStringExtra("idCuaLoaiSP");

        mData = FirebaseDatabase.getInstance().getReference();
        btnThemMonMoi = (Button) findViewById(R.id.buttonThemMonAn);
        lvSanPham = (ListView) findViewById(R.id.listviewMonAn);
        arraySanPham = new ArrayList<>();

        adapter = new MonAnAdapter(this, R.layout.dong_mon_an, arraySanPham);
        lvSanPham.setAdapter(adapter);

        btnThemMonMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonAnActivity.this, NhapMonAnActivity.class);
                startActivity(intent);
            }
        });
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MonAnActivity.this, ChiTietMonAnActivity.class);
                intent.putExtra("idCuaSanPham", arraySanPham.get(i).IdSP);
                startActivity(intent);
            }
        });

        // đọc database từ node SanPham
        mData.child("MonAn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonAn monAn = dataSnapshot.getValue(MonAn.class);
                // kiểm tra nếu đúng loại -> add vào mảng
                if(monAn.IdLoai.equals(idLoai)){
                    arraySanPham.add(new MonAn(
                            dataSnapshot.getKey(),
                            monAn.IdLoai,
                            monAn.TenSP,
                            monAn.GiaSP,
                            monAn.HinhSP,
                            monAn.MoTa
                    ));
                }
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
                        Intent intent1 = new Intent(MonAnActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        Intent intent2 = new Intent(MonAnActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuDatHang:
                        Intent intent3 = new Intent(MonAnActivity.this, DanhSachDatHangActivity.class);
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
