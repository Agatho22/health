package com.aman.health;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivityF extends AppCompatActivity {

    private static final String TAG = "RegisterActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail, mEtpwd, mEtpwd2; //회원가입 입력필드
    private Button mBtnRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_f);


        //파이어베이스 접근 설정
        mfirebaseAuth = mfirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");






        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        mEtpwd2 = findViewById(R.id.et_pwd2);

        mBtnRegister = findViewById(R.id.btn_registerf);




        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString().trim();
                String strPwd = mEtpwd.getText().toString().trim();
                String strPwd2 = mEtpwd2.getText().toString().trim();

                if(strPwd.equals(strPwd2)) {
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
                }else{

                    Toast.makeText(RegisterActivityF.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }



}