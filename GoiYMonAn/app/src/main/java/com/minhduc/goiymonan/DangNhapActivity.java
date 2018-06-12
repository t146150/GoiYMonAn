package com.minhduc.goiymonan;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.minhduc.goiymonan.User.ContactActivity;
import com.minhduc.goiymonan.User.DatMuaActivity;
import com.minhduc.goiymonan.User.UserMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DangNhapActivity extends AppCompatActivity {

    DatabaseReference mData;
    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    Button btnLogin;
    TextView txtTitle, textview4;
    EditText edtTK, edtMK;
    private FirebaseAuth mAuth;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CallbackManager callbackManager;
    String email, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_dang_nhap);

        callbackManager = CallbackManager.Factory.create();
        AnhXa();
        NavigationView();
        mAuth = FirebaseAuth.getInstance();
//
        txtTitle.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        setLoginButton();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangNhap();
            }
        });

        try {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(
                        "com.minhduc.goiymonan",
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }  catch (NoSuchAlgorithmException e) {

        }
    }
    private void setLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                result();

                Toast.makeText(DangNhapActivity.this,  "Chào mừng " + name + " đã đăng nhập ứng dụng GoiYMonAn", Toast.LENGTH_SHORT).show();
                Intent intentSecond_User = new Intent(DangNhapActivity.this, UserMainActivity.class);
                startActivity(intentSecond_User);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    private  void result(){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON", response.getJSONObject().toString());
                try {

                    email = object.getString("email");
                    name = object.getString("name");
                    textview4.setText(name);
                    // add account facebook len firebase
                    String emailFirebase = email.toString();
                    String nameFirebase = name.toString();
                   // profilePictureView.setProfil ;
                    // xu ly ben firebase
//                    UserAccount userAccount = new UserAccount(
//                            null,
//                            emailFirebase,
//                            nameFirebase
//                    );
//                    mData.child("UserFacebook").push().setValue(userAccount, new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    if (databaseError == null){
//                        Toast.makeText(DatMuaActivity.this, "Đặt mua thành công\n chúng tôi sẽ sớm liên hệ với bạn", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                    else {
//                        Toast.makeText(DatMuaActivity.this, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
//                    }
//                        }
//                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
    private  void DangNhap()
    {
        String adminemail =edtTK.getText().toString().trim();
        String adminpassword =edtMK.getText().toString().trim();
        if (adminemail.isEmpty()== true || adminpassword.isEmpty()== true)
        {
            Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin tài khoản", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(adminemail, adminpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // thông báo và hiệu ứng chuyển màn hinh nếu đăng nhập thành công
                                Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intentSecond = new Intent(DangNhapActivity.this, MainActivity.class);
                                startActivity(intentSecond);

                            }
                            else {
                                // thông báo và hiệu ứng đăng nhập sai tài khoản
                                Toast.makeText(DangNhapActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
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

        //set lại màu
        navigationView.setNavigationItemSelectedListener(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                switch (item.getItemId()) {
//                    case R.id.menuTrangChu:
//                        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        break;
//
//                }
//
                return false;
            }
        });
    }
    private void AnhXa() {
        profilePictureView =(ProfilePictureView)findViewById(R.id.friendProfilePicture);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        txtTitle = (TextView) findViewById(R.id.textView);
        textview4= (TextView) findViewById(R.id.textView4);
        edtTK = (EditText) findViewById(R.id.editText);
        edtMK = (EditText) findViewById(R.id.editText2);
        navigationView = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}
