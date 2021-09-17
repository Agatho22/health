package com.aman.health;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FoundActivityF extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private EditText mEtEmail, mEtpwd; //로그인 입력필드
    private EditText editTextUserEmail;
    private Button btn_continue;
    private ProgressDialog progressDialog;
    private TextView back_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        //버튼 등록하기
        editTextUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        progressDialog = new ProgressDialog(this);
        mfirebaseAuth = FirebaseAuth.getInstance();
        back_login = (TextView) findViewById(R.id.back_login);
        btn_continue.setOnClickListener(this);

        //뒤로가기 버튼이 눌리면
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoundActivityF.this, LoginActivityF.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == btn_continue) {
            progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
            progressDialog.show();
            //비밀번호 재설정 이메일 보내기
            String emailAddress = editTextUserEmail.getText().toString().trim();
            mfirebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FoundActivityF.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivityF.class));
                            } else {
                                Toast.makeText(FoundActivityF.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });



        }
    }
}