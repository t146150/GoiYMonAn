package com.minhduc.goiymonan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NhapLoaiMonAnActivity extends AppCompatActivity {
    ImageView imgHinh;
    EditText edtTen, edtId;
    Button btnThem, btnHuy;

    int REQUESR_CODE_IMAGE = 123;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_loai_mon_an);mDatabase = FirebaseDatabase.getInstance().getReference();
        NavigationView();
        AnhXa();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference mountainsRef =
                        storageRef.child("LoaiSP/hinhanh"+ System.currentTimeMillis() +".png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(NhapLoaiMonAnActivity.this, "Upload hình Loại SP thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(NhapLoaiMonAnActivity.this, "Upload hình Loại SP thành công.", Toast.LENGTH_SHORT).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        // nếu lưu hình loại SP thành công -> lưu database
                        LoaiMonAn loaisp = new LoaiMonAn(
                                edtId.getText().toString().trim(),
                                edtTen.getText().toString().trim(),
                                String.valueOf(downloadUrl)
                        );
                        mDatabase.child("LoaiSanPham").push().setValue(loaisp, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    Toast.makeText(NhapLoaiMonAnActivity.this, "Thêm loại SP thành công.", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(NhapLoaiMonAnActivity.this, "Thêm loại SP thất bại!", Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent, REQUESR_CODE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUESR_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void NavigationView()
    {
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

        //set lại màu

    }
    private void AnhXa() {
        btnHuy = (Button) findViewById(R.id.buttonHuyLoai);
        btnThem = (Button) findViewById(R.id.buttonThemLoai);
        edtId = (EditText) findViewById(R.id.editIDLoai);
        edtTen = (EditText) findViewById(R.id.editLoaiNhap);
        imgHinh = (ImageView) findViewById(R.id.imagviewLoaiSanPham);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
    }
}
