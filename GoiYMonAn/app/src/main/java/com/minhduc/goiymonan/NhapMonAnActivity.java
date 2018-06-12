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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NhapMonAnActivity extends AppCompatActivity {

    EditText edtTen, edtMoTa,edtGia;
    ImageView imgHinh;
    Button btnThem;
    Spinner spLoaiSP;
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
        setContentView(R.layout.activity_nhap_mon_an);

        AnhXa();
        arrayIdLoai = new ArrayList<>();
        arrayTenLoai = new ArrayList<>();
        //NavigationView();
        // đổ dữ liệu cho spinner loại sản phẩm
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTenLoai);
        spLoaiSP.setAdapter(adapter);

        // khởi tạo database
        mData = FirebaseDatabase.getInstance().getReference();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenmonan =edtTen.getText().toString().trim();
                String giamonan =edtGia.getText().toString().trim();
                String motamonan =edtMoTa.getText().toString().trim();


                if (tenmonan.isEmpty()== true || giamonan.isEmpty()== true || motamonan.isEmpty()== true )
                {
                    Toast.makeText(NhapMonAnActivity.this, "Vui lòng nhập đầy đủ thông tin món ăn", Toast.LENGTH_SHORT).show();
                }
                else  {

                    final int value = Integer.valueOf(giamonan);
                    if (value > 5000000 || value < 5000) {
                        // do what you want
                        Toast.makeText(NhapMonAnActivity.this, "Giá sản phẩm không hợp lệ quá thấp hơn 5.000 hoặc cao hơn 5.000.000", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        /// up load thành cong
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
                                Toast.makeText(NhapMonAnActivity.this, "Upload hình SP lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Toast.makeText(NhapMonAnActivity.this, "Upload hình SP thành công", Toast.LENGTH_SHORT).show();

                                // add vào database
                                MonAn sanpham = new MonAn(
                                        null,
                                        idLoai,
                                        edtTen.getText().toString(),
                                        Integer.parseInt(edtGia.getText().toString()),
                                        String.valueOf(downloadUrl),
                                        edtMoTa.getText().toString()
                                );
                                mData.child("MonAn").push().setValue(sanpham, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError == null){
                                            Toast.makeText(NhapMonAnActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(NhapMonAnActivity.this, "Lỗi thêm món!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }


                }

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
                LoaiMonAn loaimonan = dataSnapshot.getValue(LoaiMonAn.class);
                arrayTenLoai.add(loaimonan.TenLoai);
                arrayIdLoai.add(loaimonan.IdLoai);
                adapter.notifyDataSetChanged();

                spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


//        navigationView.setNavigationItemSelectedListener(null);
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.menuTrangChu:
//                        Intent intent1 = new Intent(NhapMonAnActivity.this, MainActivity.class);
//                        startActivity(intent1);
//                        break;
//                    case R.id.menuDangXuat:
//                        Intent intent2 = new Intent(NhapMonAnActivity.this, DangNhapActivity.class);
//                        startActivity(intent2);
//                        break;
//                    case R.id.menuDatHang:
//                        Intent intent3 = new Intent(NhapMonAnActivity.this, DanhSachDatHangActivity.class);
//                        startActivity(intent3);
//                        break;
//                }
//
//                return false;
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            imgHinh.setImageURI(uri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void AnhXa() {

        edtMoTa = (EditText) findViewById(R.id.editTextMoTaNhap);
        edtTen  = (EditText) findViewById(R.id.editTextTenSPNhap);
        edtGia  = (EditText) findViewById(R.id.editTextGiaSPNhap);
        imgHinh = (ImageView) findViewById(R.id.imageviewHinhSPNhap);
        btnThem = (Button) findViewById(R.id.buttonThemSPNhap);
        spLoaiSP = (Spinner) findViewById(R.id.spinnerLoaiSP);

//        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
//        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
//        toolbar = (Toolbar) findViewById(R.id.myToolbar);
    }
//    public void NavigationView()
//    {
//        setSupportActionBar(toolbar);
//        //set màu của bar
//        toolbar.setBackgroundColor(Color.BLUE);
//        //enable cái icon lên
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // chèn icon ba gạch
//        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
//
//        // sự kiện khi nhấn nút hiện ra cái navigation
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // chạy cái navigation ra thông qua cái Drawer
//                drawerLayout.openDrawer(Gravity.START);
//            }
//        });
//
//        //set lại màu
//
//    }
}
