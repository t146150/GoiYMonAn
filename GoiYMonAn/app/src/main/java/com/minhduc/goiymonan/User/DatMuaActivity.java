package com.minhduc.goiymonan.User;

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
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minhduc.goiymonan.DangNhapActivity;
import com.minhduc.goiymonan.DatHang;
import com.minhduc.goiymonan.NhapMonAnActivity;
import com.minhduc.goiymonan.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatMuaActivity extends AppCompatActivity {
    EditText edtHoten, edtEmail, edtSoDT, edtChiChu;
    Button btnDatMua;
    DatabaseReference mData;
    String idSP;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    boolean checkTen = false, checkSDT = false, checkEmail = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_mua);
        AnhXa();
        NavigationView();
        mData = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        idSP = intent.getStringExtra("idSanPhamMua");

        btnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacNhanMua();
            }
        });

        navigationView.setNavigationItemSelectedListener(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuTrangChu:
                        Intent intent1 = new Intent(DatMuaActivity.this, UserMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.menuDangXuat:
                        LoginManager.getInstance().logOut();
                        Intent intent2 = new Intent(DatMuaActivity.this, DangNhapActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.menuGioHang:
                        Intent intent3 = new Intent(DatMuaActivity.this, GioHangActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.menuContact:
                        Intent intent4 = new Intent(DatMuaActivity.this, ContactActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.menuVideo:
                        Intent intent5 = new Intent(DatMuaActivity.this, DanhSachVideoActivity.class);
                        startActivity(intent5);
                        break;

                }

                return false;
            }
        });
    }
    private void XacNhanMua(){
        AlertDialog.Builder xacNhanMua = new AlertDialog.Builder(this);
        xacNhanMua.setMessage("Bạn có muốn đặt hàng sản phẩm này không");
        xacNhanMua.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hoten =edtHoten.getText().toString().trim();
                String email =edtEmail.getText().toString().trim();
                String sdt =edtSoDT.getText().toString().trim();

                if (hoten.isEmpty()== true || email.isEmpty()== true || sdt.isEmpty()== true )
                {
                    Toast.makeText(DatMuaActivity.this, "Vui lòng nhập đầy đủ thông tin đặt mua", Toast.LENGTH_SHORT).show();
                }
                else {
                    //check ho ten
                    String chuoi_mauHoTen = "[a-zA-z]*([,.\\\\s]+[a-z]*)*";
                    Pattern pattern = Pattern.compile(chuoi_mauHoTen);
                    Matcher matcher = pattern.matcher(hoten);
                    if (matcher.matches()==true)
                    {
                        checkTen = true;
                    }
                    else{
                        Toast.makeText(DatMuaActivity.this, "Vui lòng nhập đúng định dạng tên", Toast.LENGTH_SHORT).show();
                    }
                    //check email
                    //311878742
                    String chuoi_mauEmail = "\\w+@\\w+\\.\\w+";
                    //=”\\w+@\\w+\\.\\w+”;
                    Pattern pattern2 = Pattern.compile(chuoi_mauEmail);
                    Matcher matcher2 = pattern2.matcher(email);
                    if (matcher2.matches()==true)
                    {
                        checkEmail = true;
                    }
                    else{
                        Toast.makeText(DatMuaActivity.this, "Vui lòng nhập đúng định dạng Email", Toast.LENGTH_SHORT).show();

                    }
                    //check sdt
                    String chuoi_mauSDT = "0\\d{9,10}";

                    Pattern pattern3 = Pattern.compile(chuoi_mauSDT);
                    Matcher matcher3 = pattern3.matcher(sdt);
                    if (matcher3.matches()==true)
                    {
                        checkSDT = true;
                    }
                    else{
                        Toast.makeText(DatMuaActivity.this, "Vui lòng nhập đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
                    }
                    // thoa man cac yeu to
                    if (checkTen ==true && checkEmail == true && checkSDT ==true) {
                        UploadDuLieuMua();
                    }
                }

            }
        });
        xacNhanMua.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        xacNhanMua.show();
    }

    public void UploadDuLieuMua() {
        // upload len database
        DatHang datHang = new DatHang(
                null,
                idSP,
                edtHoten.getText().toString(),
                edtEmail.getText().toString(),
                edtSoDT.getText().toString(),
                edtChiChu.getText().toString()
        );
        mData.child("ORDER").push().setValue(datHang, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null){
                    Toast.makeText(DatMuaActivity.this, "Đặt mua thành công\n chúng tôi sẽ sớm liên hệ với bạn", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(DatMuaActivity.this, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //upload database SQL
        Time today = new Time(Time.getCurrentTimezone());

        today.setToNow();
        String time =  today.format("%k:%M:%S") +"";
        String ghichu =  edtChiChu.getText().toString().trim();
        UserMainActivity.database.
                QueryData("INSERT INTO GioHangUser VALUES(null, '" +idSP + "', '" + time + "', '"  +  ghichu+ "')" );


        // gui thong bao email toi user
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{edtEmail.getText().toString()});
//                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//                i.putExtra(Intent.EXTRA_TEXT   , "bạn đã đặt mua"+ idSP);
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(DatMuaActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }
        // gui email thong bao toi admin

        Intent i2 = new Intent(Intent.ACTION_SEND);
        i2.setType("message/rfc822");
        i2.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ducle624@gmail.com"});
        i2.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i2.putExtra(Intent.EXTRA_TEXT   , "Toi muon dat mua "+ idSP);
        try {
            startActivity(Intent.createChooser(i2, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DatMuaActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
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
    private void AnhXa()
    {
        btnDatMua = (Button) findViewById(R.id.buttonDatMua);
        edtHoten = (EditText) findViewById(R.id.editTextHoten);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSoDT = (EditText) findViewById(R.id.editTextSoDienThoai);
        edtChiChu = (EditText) findViewById(R.id.editTextGhiChu);
        navigationView = (NavigationView) findViewById(R.id.myNavigationViewUser);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbarUser);
    }
}