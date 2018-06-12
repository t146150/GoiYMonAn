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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhduc.goiymonan.DangNhapActivity;
import com.minhduc.goiymonan.MonAn;
import com.minhduc.goiymonan.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietMonAnUserActivity extends AppCompatActivity {
    Button btnMua;
    TextView txtTen, txtGia, txtMoTa;
    ImageView imgHinh;

    DatabaseReference mData;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_an_user);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();
        NavigationView();
        Intent intent = getIntent();
        final String idSanPham = intent.getStringExtra("idCuaSanPham");

        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietMonAnUserActivity.this, DatMuaActivity.class);
                intent.putExtra("idSanPhamMua", idSanPham);
                startActivity(intent);
            }
        });
        // đọc dữ liệu từ id của sản phẩm
        mData.child("MonAn").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MonAn sanpham = dataSnapshot.getValue(MonAn.class);
                txtTen.setText(sanpham.TenSP);

                DecimalFormat dinhdangSo = new DecimalFormat("###,###,###");
                txtGia.setText(dinhdangSo.format(sanpham.GiaSP) + "Đ");
                txtMoTa.setText(sanpham.MoTa);
                Picasso.with(ChiTietMonAnUserActivity.this).load(sanpham.HinhSP).into(imgHinh);
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
                        Intent intent1 = new Intent(ChiTietMonAnUserActivity.this, UserMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        LoginManager.getInstance().logOut();
                        Intent intent2 = new Intent(ChiTietMonAnUserActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuGioHang:
                        Intent intent3 = new Intent(ChiTietMonAnUserActivity.this, GioHangActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.menuContact:
                        Intent intent4 = new Intent(ChiTietMonAnUserActivity.this, ContactActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.menuVideo:
                        Intent intent5 = new Intent(ChiTietMonAnUserActivity.this, DanhSachVideoActivity.class);
                        startActivity(intent5);
                        break;

                }

                return false;
            }
        });
    }

    private void AnhXa() {
        imgHinh = (ImageView) findViewById(R.id.imageviewSanPhamDetailUser);
        txtGia= (TextView) findViewById(R.id.textviewGiaDetailUser);
        txtMoTa = (TextView) findViewById(R.id.textviewMoTaUser);
        txtTen = (TextView) findViewById(R.id.textviewTenSPDetailUser);
        btnMua = (Button) findViewById(R.id.buttonMuaUser);
        navigationView = (NavigationView) findViewById(R.id.myNavigationViewUser);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbarUser);
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
    }
}
