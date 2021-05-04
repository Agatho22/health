package com.aman.health;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register, button5,button6;
    private int count = 5;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        info = findViewById(R.id.info);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);


        //버튼5 클릭시 텍스트 아이디  값 초기화
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_id.setText(null);
            }
        });
        //버튼6 클릭시  텍스트 비밀번호 값 초기화
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pass.setText(null);
            }
        });


        // 회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                            System.out.println("hongchul" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(com.aman.health.LoginActivity.this, com.aman.health.MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPass", userPass);
                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                if(count == 0){
                                    Toast.makeText(getApplicationContext(),"로그인 시도 5회 시도 횟수를 초과했습니다.\n잠시후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                                    btn_login.setEnabled(false); // 로그인 버튼 막기
                                    info.setText("로그인 남은 시도 횟수 : "+ count); // ID, PW 틀리면 로그인 남은 시도 횟수 알려주는거
                                }else if(count == 2 ){
                                    count --;
                                    info.setText("로그인 남은 시도 횟수 : "+ count); // ID, PW 틀리면 로그인 남은 시도 횟수 알려주는거
                                    Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.\n1회 더 실패시 로그인 정지 ",Toast.LENGTH_SHORT).show();
                                }else {
                                    count --;
                                    info.setText("로그인 남은 시도 횟수 : "+ count); // ID, PW 틀리면 로그인 남은 시도 횟수 알려주는거
                                    Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.         "+count+"번 남았습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(com.aman.health.LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }
}


