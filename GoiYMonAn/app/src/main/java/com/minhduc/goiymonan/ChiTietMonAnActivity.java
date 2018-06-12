package com.minhduc.goiymonan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhduc.goiymonan.User.ChiTietMonAnUserActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietMonAnActivity extends AppCompatActivity {

    Button btnSua, btnXoa;
    TextView txtTen, txtGia, txtMoTa;
    ImageView imgHinh;
    public String idSanPham;
    DatabaseReference mData;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_an);

        mData = FirebaseDatabase.getInstance().getReference();

        AnhXa();
        NavigationView();
        Intent intent = getIntent();
       idSanPham = intent.getStringExtra("idCuaSanPham");

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacNhanXoaHS();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietMonAnActivity.this, CapNhatMonAnActivity.class);
                intent.putExtra("idCuaSanPham", idSanPham);
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
                Picasso.with(ChiTietMonAnActivity.this).load(sanpham.HinhSP).into(imgHinh);
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
                        Intent intent1 = new Intent(ChiTietMonAnActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        Intent intent2 = new Intent(ChiTietMonAnActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuDatHang:
                        Intent intent3 = new Intent(ChiTietMonAnActivity.this, DanhSachDatHangActivity.class);
                        startActivity(intent3);
                        break;


                }

                return false;
            }
        });

    }
    private void XacNhanXoaHS(){
        AlertDialog.Builder xacNhanXoa = new AlertDialog.Builder(this);
        xacNhanXoa.setMessage("Bạn có muốn xóa món ăn này không");
        xacNhanXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData.child("MonAn").child(idSanPham).removeValue();
                Toast.makeText(ChiTietMonAnActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
        xacNhanXoa.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        xacNhanXoa.show();

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
    private void AnhXa() {
        imgHinh = (ImageView) findViewById(R.id.imageviewSanPhamDetail);
        txtGia= (TextView) findViewById(R.id.textviewGiaDetail);
        txtMoTa = (TextView) findViewById(R.id.textviewMoTa);
        txtTen = (TextView) findViewById(R.id.textviewTenSPDetail);
        btnSua = (Button) findViewById(R.id.buttonSua);
        btnXoa = (Button) findViewById(R.id.buttonXoa);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
    }
}
