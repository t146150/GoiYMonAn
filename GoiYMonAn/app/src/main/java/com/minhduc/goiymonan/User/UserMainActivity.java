package com.minhduc.goiymonan.User;

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
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minhduc.goiymonan.DangNhapActivity;
import com.minhduc.goiymonan.LoaiMonAn;
import com.minhduc.goiymonan.LoaiMonAnAdapter;
import com.minhduc.goiymonan.MonNgon;
import com.minhduc.goiymonan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {
    ListView lvLoaiSanPham;
    Button btnDoiMonNgon;
    ImageView hinhmonngon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String idMonNgon ="";
    ArrayList<LoaiMonAn> arrayLoaiSP;
    LoaiMonAnAdapter adapter;
    DatabaseReference mData;
    public static SQLite database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Intent intent = getIntent();
        final String idLoai = intent.getStringExtra("idCuaLoaiSP");
        //listview
        lvLoaiSanPham = (ListView) findViewById(R.id.listviewDanhSachLoaiUser);
        arrayLoaiSP = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        adapter = new LoaiMonAnAdapter(this, R.layout.dong_loai_mon_an, arrayLoaiSP);
        lvLoaiSanPham.setAdapter(adapter);

        database = new SQLite(this, "UserData.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS GioHangUser( id Integer PRIMARY KEY AUTOINCREMENT, idSP VARCHAR  , time VARCHAR, ghichu VARCHAR ) ");
//        FragmentMain fragmentMain = (FragmentMain) getFragmentManager().findFragmentById(R.id.fragmentMain);
        AnhXa();
        // gọi tool bar lên
        NavigationView();

        lvLoaiSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(UserMainActivity.this, MonAnUserActivity.class);
                intent.putExtra("idCuaLoaiSP", arrayLoaiSP.get(i).IdLoai);
                startActivity(intent);
            }
        });
        mData.child("MonNgon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MonNgon monNgon = dataSnapshot.getValue(MonNgon.class);
                Picasso.with(UserMainActivity.this).load(monNgon.HinhSP).into(hinhmonngon);
                idMonNgon = monNgon.IdSP;
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
        hinhmonngon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idMonNgon = "";

                Intent intent = new Intent(UserMainActivity.this, ChiTietMonAnUserActivity.class);
                intent.putExtra("idCuaSanPham",idMonNgon);
                Toast.makeText(UserMainActivity.this, "" +idMonNgon, Toast.LENGTH_SHORT).show();
                startActivity(intent);
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuTrangChu:
                        Intent intent1 = new Intent(UserMainActivity.this, UserMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        LoginManager.getInstance().logOut();
                        Intent intent2 = new Intent(UserMainActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuGioHang:
                        Intent intent3 = new Intent(UserMainActivity.this, GioHangActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.menuContact:
                        Intent intent4 = new Intent(UserMainActivity.this, ContactActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.menuVideo:
                        Intent intent5 = new Intent(UserMainActivity.this, DanhSachVideoActivity.class);
                        startActivity(intent5);
                        break;

                }

                return false;
            }
        });
    }

    public void NavigationView() {
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
        navigationView.setNavigationItemSelectedListener(null);


    }



    private void AnhXa()
    {
        navigationView = (NavigationView) findViewById(R.id.myNavigationViewUser);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbarUser);
        hinhmonngon = (ImageView) findViewById(R.id.imageViewHinhMonUser);
    }

}

