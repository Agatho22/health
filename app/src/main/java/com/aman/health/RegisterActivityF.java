package com.aman.health;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivityF extends AppCompatActivity {

    public class UserModel {
        // 사용자 기본정보
        public String profileImageUrl; // 사용자 프로필사진
        public String uid; // 현재 사용자(로그인한)
//    public String pushToken;

    }

    private static final String TAG = "RegisterActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private EditText mEtEmail, mEtpwd, mEtpwd2; //회원가입 입력필드
    private Button mBtnRegister;
    private ImageView mEtProfile;
    private TextView backagain;
    private long backKeyPressedTime = 0;

    public static final int PICK_FROM_ALBUM = 1;
    private Uri imageUri;
    private String pathUri;
    private File tempFile;


    @Override
    public void onBackPressed() {
        // 기존의 뒤로가기 버튼의 기능제거
        // super.onBackPressed();

        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_f);


        //파이어베이스 접근 설정
        mfirebaseAuth = mfirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();


        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        mEtpwd2 = findViewById(R.id.et_pwd2);
        backagain = (TextView) findViewById(R.id.backagain);
        mBtnRegister = findViewById(R.id.btn_registerf);
        mEtProfile = findViewById(R.id.iv_profile);

        //아이디가 있으신가요 버튼이 눌리면
        backagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivityF.this, LoginActivityF.class);
                startActivity(intent);
            }
        });

        mEtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAlbum();
            }
        });


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString().trim();
                String strPwd = mEtpwd.getText().toString().trim();
                String strPwd2 = mEtpwd2.getText().toString().trim();

                if (strPwd.equals(strPwd2)) {
                    Log.d(TAG, "등록 버튼 " + strEmail + " , " + strPwd);
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivityF.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    mfirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivityF.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //가입 성공시
                            mDialog.dismiss();
                            if (task.isSuccessful()) {

                                final String uid = task.getResult().getUser().getUid();
                                final Uri file = Uri.fromFile(new File(pathUri)); // path

                                StorageReference storageReference = mStorage.getReference()
                                        .child("UsersprofileImages").child("uid/" + uid);

                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while (!imageUrl.isComplete()) ;

                                        UserModel userModel = new UserModel();
                                        userModel.uid = uid;
                                        userModel.profileImageUrl = imageUrl.getResult().toString();

                                        // database에 저장
                                        mDatabase.getReference().child("Users").child(uid)
                                                .setValue(userModel);
                                    }
                                });

                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(RegisterActivityF.this, RegisterActivityF2.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivityF.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterActivityF.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.
                            }
                        }
                    });

                    //비밀번호 오류시
                } else {

                    Toast.makeText(RegisterActivityF.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            Toast.makeText(RegisterActivityF.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 코드 일치
                // Uri
                imageUri = data.getData();
                pathUri = getPath(data.getData());
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);
                mEtProfile.setImageURI(imageUri); // 이미지 띄움
                break;
            }
        }
    }

    // uri 절대경로 가져오기
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

    private void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
}