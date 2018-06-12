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
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minhduc.goiymonan.User.ContactActivity;
import com.minhduc.goiymonan.User.DanhSachVideoActivity;
import com.minhduc.goiymonan.User.DatMuaActivity;
import com.minhduc.goiymonan.User.GioHangActivity;
import com.minhduc.goiymonan.User.SQLite;
import com.minhduc.goiymonan.User.UserMainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SQLite database;
    ListView lvLoaiSanPham;
    ImageView hinhmonngon;
    Button btnDoiMonNgon;

    ArrayList<LoaiMonAn> arrayLoaiSP;
    LoaiMonAnAdapter adapter;
    DatabaseReference mData;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    String idRequest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hinhmonngon = (ImageView) findViewById(R.id.imageViewHinhMon);
        Intent intent = getIntent();
        final String idCuaMonngon = intent.getStringExtra("idCuaMonNgon");
        //listview
        lvLoaiSanPham = (ListView) findViewById(R.id.listviewDanhSachLoai);
        arrayLoaiSP = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        adapter = new LoaiMonAnAdapter(this, R.layout.dong_loai_mon_an, arrayLoaiSP);
        lvLoaiSanPham.setAdapter(adapter);



//        FragmentMain fragmentMain = (FragmentMain) getFragmentManager().findFragmentById(R.id.fragmentMain);
        AnhXa();
        // gọi tool bar lên
        NavigationView();


        mData.child("MonNgon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonNgon monNgon = dataSnapshot.getValue(MonNgon.class);
                Picasso.with(MainActivity.this).load(monNgon.HinhSP).into(hinhmonngon);


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
        btnDoiMonNgon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMonNgon = new Intent(MainActivity.this, DanhSachMonNgonActivity.class);
                startActivity(intentMonNgon);
            }
        });
        lvLoaiSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, LoaiMonAnActivity.class);
                intent.putExtra("idCuaLoaiSP", arrayLoaiSP.get(i).IdLoai);
                idRequest =  arrayLoaiSP.get(i).IdLoai;
                startActivityForResult(intent,1234);
            }
        });
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

        //set lại màu
        navigationView.setNavigationItemSelectedListener(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuTrangChu:
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        Intent intent2 = new Intent(MainActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuDatHang:
                        Intent intent3 = new Intent(MainActivity.this, DanhSachDatHangActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1234 && resultCode == RESULT_OK && data != null){
            String name = data.getStringExtra("idCuaMonNgon");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void AnhXa()
    {
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        btnDoiMonNgon = (Button)findViewById(R.id.buttonDoiMonAn);
    }

}

