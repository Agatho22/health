package com.aman.health;

import static com.aman.health.FbData.mDatabase;
import static com.aman.health.FbData.mStorage;
import static com.aman.health.FbData.uid;

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
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class RegisterActivityF2 extends AppCompatActivity {

    public static class UserModel {
        // 사용자 기본정보
        public String profileImageUrl; // 사용자 프로필사진
        public String uid; // 현재 사용자(로그인한)
//    public String pushToken;

    }

    private static final String TAG = "RegisterActivityF2";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtName, mEtAge, mEtheight, mEtweight; //회원가입 입력필드
    private RadioGroup rg_gender;
    private ImageView mEtProfile;
    private String gender;
    private Button registerf2;
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
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_f2);


        //파이어베이스 접근 설정
        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance();
        mEtAge = findViewById(R.id.et_age);

        mEtAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivityF2.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mEtProfile = findViewById(R.id.iv_profile);
        mEtName = findViewById(R.id.et_name);
        mEtheight = findViewById(R.id.et_height);
        mEtweight = findViewById(R.id.et_weight);
        rg_gender = findViewById(R.id.rg_gender);
        registerf2 = findViewById(R.id.btn_registerf2);


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                gender = genderButton.getText().toString();
            }
        });

        mEtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                StorageReference storageReference = mStorage.getReference().child("UsersprofileImages").child("uid/" + uid);
                final File fileLog = new File("resource://com.aman.health/2131230847");


                Task<UploadTask.TaskSnapshot> task = storageReference.putFile(as);

                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                        while (!imageUrl.isComplete()) {

                            UserModel userModel = new UserModel();
                            userModel.uid = uid;
                            userModel.profileImageUrl = imageUrl.getResult().toString();
                            // database에 저장
                            mDatabase.getReference().child("Users").child(uid)
                                    .setValue(userModel);
                        }
                    }
                });

                */

                gotoAlbum();
            }
        });

        registerf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Uri file = Uri.fromFile(new File(pathUri)); // path

                StorageReference storageReference = mStorage.getReference().child("UsersprofileImages").child("uid/" + uid);


                //imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.ellipse);
                // imageUri = Uri.parse("content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F13/ORIGINAL/NONE/596537642");

                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        Log.d(TAG, "이게 uri:   " + imageUri);

                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                        while (!imageUrl.isComplete()) {

                            UserModel userModel = new UserModel();
                            userModel.uid = uid;
                            userModel.profileImageUrl = imageUrl.getResult().toString();
                            // database에 저장
                            mDatabase.getReference().child("Users").child(uid)
                                    .setValue(userModel);
                        }
                    }
                });


                Calendar cal = Calendar.getInstance();
                FirebaseUser user = mfirebaseAuth.getCurrentUser();
                String email = user.getEmail();
                String uid = user.getUid();
                String name = mEtName.getText().toString().trim();
                int age = cal.get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR) + 1;
                double height = Double.parseDouble(mEtheight.getText().toString());
                double weight = Double.parseDouble(mEtweight.getText().toString());
                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장

                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("Uid", uid);
                hashMap.put("Email", email);
                hashMap.put("Name", name);
                hashMap.put("Age", age + "");
                hashMap.put("Height", height + "");
                hashMap.put("Weight", weight + "");
                hashMap.put("Gender", gender);
                mDatabaseRef.child(uid).child("profile").setValue(hashMap);

                //가입이 이루어져을시 가입 화면을 빠져나감.
                Intent intent = new Intent(RegisterActivityF2.this, WellcomeActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivityF2.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            Toast.makeText(RegisterActivityF2.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, "PICK_FROM_ALBUM photopath : " + pathUri);
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


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText mEtAge = (EditText) findViewById(R.id.et_age);
        mEtAge.setText(sdf.format(myCalendar.getTime()));

    }

}