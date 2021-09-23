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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoundActivityF extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private EditText mEtEmail, mEtpwd; //로그인 입력필드
    private EditText editTextUserEmail;
    private Button btn_continue;
    private ProgressDialog progressDialog;
    private TextView back_login;
    private long backKeyPressedTime = 0;

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
        setContentView(R.layout.activity_found);

        //버튼 등록하기
        editTextUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        progressDialog = new ProgressDialog(this);
        mfirebaseAuth = FirebaseAuth.getInstance();
        back_login = (TextView) findViewById(R.id.back_login);
        btn_continue.setOnClickListener(this);

        //아이디가 있으신가요 버튼이 눌리면
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
        String emailAddress = editTextUserEmail.getText().toString().trim();
        Pattern p = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher m = p.matcher(emailAddress);

        if(!m.matches())
        {
            Toast.makeText(FoundActivityF.this,"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        if (view == btn_continue) {
            progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
            progressDialog.show();
            //비밀번호 재설정 이메일 보내기

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