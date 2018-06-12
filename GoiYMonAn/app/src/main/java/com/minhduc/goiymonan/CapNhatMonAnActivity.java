package com.minhduc.goiymonan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CapNhatMonAnActivity extends AppCompatActivity {
    EditText edtTen, edtGia, edtMoTa;
    ImageView imgHinh;
    Button btnCapNhat, btnHuy;
    Spinner spLoaiHS;
    ArrayList<String> arrayTenLoai, arrayIdLoai;
    ArrayAdapter adapter;
    String idLoai = "";

    int REQUEST_CODE_CHOOSE = 789;

    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_mon_an);
        AnhXa();
        arrayIdLoai = new ArrayList<>();
        arrayTenLoai = new ArrayList<>();
        NavigationView();
        // đổ dữ liệu cho spinner loại sản phẩm
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTenLoai);
        spLoaiHS.setAdapter(adapter);

        // khởi tạo database
        mData = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        final String idSanPham = intent.getStringExtra("idCuaSanPham");

        mData.child("MonAn").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MonAn monAn = dataSnapshot.getValue(MonAn.class);
                edtTen.setText(monAn.TenSP.toString());
                edtGia.setText(monAn.GiaSP +"");
                edtMoTa.setText(monAn.MoTa.toString());
                Picasso.with(CapNhatMonAnActivity.this).load(monAn.HinhSP).into(imgHinh);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference mountainsRef = storageRef.child("SanPham/hinhanh"+ System.currentTimeMillis() +".png");
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(CapNhatMonAnActivity.this, "Upload hình món ăn lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(CapNhatMonAnActivity.this, "Upload hình món ăn thành công", Toast.LENGTH_SHORT).show();
                        // add vào database
                        MonAn sanpham = new MonAn(
                                idSanPham,
                                idLoai,
                                edtTen.getText().toString(),
                                Integer.parseInt(edtGia.getText().toString()),
                                String.valueOf(downloadUrl),
                                edtMoTa.getText().toString()
                        );
                        mData.child("MonAn").child(idSanPham).setValue(sanpham, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    Toast.makeText(CapNhatMonAnActivity.this, "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CapNhatMonAnActivity.this, LoaiMonAnActivity.class);
                                }else {
                                    Toast.makeText(CapNhatMonAnActivity.this, "Lỗi cập nhật món ăn!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_CHOOSE);
            }
        });

        mData.child("LoaiSanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LoaiMonAn loaisp = dataSnapshot.getValue(LoaiMonAn.class);
                arrayTenLoai.add(loaisp.TenLoai);
                arrayIdLoai.add(loaisp.IdLoai);
                adapter.notifyDataSetChanged();

                spLoaiHS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        idLoai = arrayIdLoai.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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
                        Intent intent1 = new Intent(CapNhatMonAnActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        Intent intent2 = new Intent(CapNhatMonAnActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuDatHang:
                        Intent intent3 = new Intent(CapNhatMonAnActivity.this, DanhSachDatHangActivity.class);
                        startActivity(intent3);
                        break;


                }

                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            imgHinh.setImageURI(uri);
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        edtGia  = (EditText) findViewById(R.id.editTextGiaSPSua);
        edtMoTa = (EditText) findViewById(R.id.editTextMoTaSua);
        edtTen  = (EditText) findViewById(R.id.editTextTenSPSua);
        imgHinh = (ImageView) findViewById(R.id.imageviewHinhSPSua);
        btnCapNhat = (Button) findViewById(R.id.buttonThemSPSua);
        spLoaiHS = (Spinner) findViewById(R.id.spinnerLoaiSPSua);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
    }

}
