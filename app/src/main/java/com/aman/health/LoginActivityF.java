package com.aman.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.royrodriguez.transitionbutton.TransitionButton;

public class LoginActivityF extends AppCompatActivity {

    private static final String TAG = "LoginActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private EditText mEtEmail, mEtpwd; //로그인 입력필드
    private Button btn_register;
    private TransitionButton transitionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_f);

        mfirebaseAuth =  FirebaseAuth.getInstance();


        //버튼 등록하기
        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        transitionButton = findViewById(R.id.transition_button);
        btn_register = findViewById(R.id.btn_registerf);

        //가입 버튼이 눌리면
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivityF.this,RegisterActivityF.class));

            }
        });

        //로그인 버튼이 눌리면
        transitionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString().trim();
                String pwd = mEtpwd.getText().toString().trim();
                // Start the loading animation when the user tap the button
                transitionButton.startAnimation();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSuccessful = true;
                        if (isSuccessful) {
                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                @Override
                                public void onAnimationStopEnd() {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                        }
                    }
            }, 2000);
                mfirebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(LoginActivityF.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "로그인 정보: " + email + " , " + pwd);
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivityF.this, MainActivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(LoginActivityF.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}

/*

Button btn_login = findViewById(R.id.btn_loginf);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString().trim();
                String strPwd = mEtpwd.getText().toString().trim();

                mfirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivityF.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "등록 버튼 " + strEmail + " , " + pwd);
                        if (task.isSuccessful()){
                            //로그인 성공
                            Intent intent = new Intent(LoginActivityF.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivityF.this,"로그인에 실패하셨습니다", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


        Button btn_register = findViewById(R.id.btn_registerf);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivityF.this,RegisterActivityF.class);
                startActivity(intent);
            }
        });
 */