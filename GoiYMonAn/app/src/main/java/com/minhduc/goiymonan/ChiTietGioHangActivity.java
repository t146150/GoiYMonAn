package com.minhduc.goiymonan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.minhduc.goiymonan.User.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietGioHangActivity extends AppCompatActivity {
    ImageView imgHinhGioHang;
    TextView txtTenGioHangSanPham, txtGioHangGia, txtTimeGioHang,txtGioHangMoTa,txtThongTinKhacGioHang;
    public String idSanPham;
    DatabaseReference mData;
  //  DatabaseReference mData2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_gio_hang);

        mData = FirebaseDatabase.getInstance().getReference();
     //   mData2 = FirebaseDatabase.getInstance().getReference();
        AnhXa();

        Intent intent = getIntent();
        idSanPham = intent.getStringExtra("idCuaSanPham");


        // đọc dữ liệu từ id của sản phẩm
        mData.child("MonAn").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MonAn sanpham = dataSnapshot.getValue(MonAn.class);
                txtTenGioHangSanPham.setText(sanpham.TenSP);
                DecimalFormat dinhdangSo = new DecimalFormat("###,###,###");
                txtGioHangGia.setText(dinhdangSo.format(sanpham.GiaSP) + "Đ");
                txtGioHangMoTa.setText(sanpham.MoTa);
                Picasso.with(ChiTietGioHangActivity.this).load(sanpham.HinhSP).into(imgHinhGioHang);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //lay thoi gian
//        mData2.child("ORDER").child(idSanPham).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DatHang datHang = dataSnapshot.getValue(DatHang.class);
//                txtThongTinKhacGioHang.setText("Email đặt hàng của tôi " +datHang.Email +"/n" +
//                        "SDT đặt hàng của tôi " +datHang.SoDienThoai +"/n" +
//                        "Chú thích của tôi " +datHang.GhiChu +"/n"
//
//                );
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }


    private void AnhXa() {
        txtTenGioHangSanPham= (TextView) findViewById(R.id.textviewGioHangDetail);
        imgHinhGioHang = (ImageView) findViewById(R.id.imageviewGioHangDetail);
        txtGioHangGia= (TextView) findViewById(R.id.textviewGiaGioHangDetail);
        txtTimeGioHang = (TextView) findViewById(R.id.textviewThoiGianDatMua);
        txtGioHangMoTa = (TextView) findViewById(R.id.textviewMoTaGioHangSanPham);
        txtThongTinKhacGioHang= (TextView) findViewById(R.id.textviewThongTinKhacCuaGioHang);

    }
}