package com.aman.health;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivityF extends AppCompatActivity {



    private static final String TAG = "RegisterActivityF";
    

    private EditText mEtEmail, mEtpwd, mEtpwd2; //회원가입 입력필드
    private Button mBtnRegister;
    private TextView backagain;
    private long backKeyPressedTime = 0;
    private FirebaseAuth mfirebaseAuth;




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
        mfirebaseAuth = FirebaseAuth.getInstance();

        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        mEtpwd2 = findViewById(R.id.et_pwd2);
        backagain = (TextView) findViewById(R.id.backagain);
        mBtnRegister = findViewById(R.id.btn_registerf);
        
        




        //아이디가 있으신가요 버튼이 눌리면
        backagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivityF.this, LoginActivityF.class);
                startActivity(intent);
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
                                
                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(RegisterActivityF.this, RegisterActivityF2.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivityF.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterActivityF.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                //해당 메소드 진행을 멈추고 빠져나감.
                            }
                        }
                    });

                    //비밀번호 오류시
                } else {

                    Toast.makeText(RegisterActivityF.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}