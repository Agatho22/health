package com.aman.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivityF extends AppCompatActivity {

    private static final String TAG = "LoginActivityF";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText mEtEmail, mEtpwd; //로그인 입력필드
    private Button btn_login;
    private TextView register;
    private CheckBox cb_auto;
    private SharedPreferences appData;
    private boolean saveLoginData;
    private String sv_email,sv_pwd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_f);



        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        mfirebaseAuth =  FirebaseAuth.getInstance();

        //버튼 등록하기
        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        btn_login = findViewById(R.id.btn_loginf);
        register = (TextView) findViewById(R.id.register);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);



        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            /*
            mEtEmail.setText(sv_email);
            mEtpwd.setText(sv_pwd);
            cb_auto.setChecked(saveLoginData);
            */
            Intent intent = new Intent(LoginActivityF.this, MainActivity.class);
            startActivity(intent);
            finish();


        }







        //로그인 버튼이 눌리면
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString().trim();
                String pwd = mEtpwd.getText().toString().trim();

                if (!mEtEmail.getText().toString().equals("") && !mEtpwd.getText().toString().equals("")) {
                    loginUser(mEtEmail.getText().toString(), mEtpwd.getText().toString());
                } else {
                    Toast.makeText(LoginActivityF.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();

                }


            }
        });


        //가입 버튼이 눌리면
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivityF.this,RegisterActivityF.class));
            }
        });
    }

    public void loginUser(String email, String password) {
        mfirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "로그인 정보: " + email + " , " + password);
                        if (task.isSuccessful()) {
                                    FirebaseUser user = mfirebaseAuth.getCurrentUser();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                    save();
                                    startActivity(intent);
                                    finish();
                            }
                            else {
                            // 로그인 실패
                            Toast.makeText(LoginActivityF.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                             }

                        }
                });
    }


    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", cb_auto.isChecked());
        editor.putString("ID", mEtEmail.getText().toString().trim());
        editor.putString("PWD", mEtpwd.getText().toString().trim());

        if (cb_auto.isChecked() == false){
            editor.clear();
        }
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        sv_email = appData.getString("ID", "");
        sv_pwd = appData.getString("PWD", "");
    }

    // 자동로그인
    private void autologin() {
        if(mEtEmail !=null && mEtpwd != null) {
            if(mEtEmail.getText().toString().equals(sv_email) && mEtpwd.getText().toString().equals(sv_pwd)) {

                Toast.makeText(LoginActivityF.this, sv_email +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivityF.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            else { Toast.makeText(LoginActivityF.this, "아이디 비밀번호 불일치", Toast.LENGTH_SHORT).show(); }


        } else { Toast.makeText(LoginActivityF.this, sv_email +"아이디 비밀번호 = null", Toast.LENGTH_SHORT).show(); }

    }
}
