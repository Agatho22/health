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

public class RegisterActivityF2 extends AppCompatActivity {

    private static final String TAG = "RegisterActivityF2";
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtName, mEtAge, mEtheight, mEtweight; //회원가입 입력필드
    private RadioGroup rg_gender;
    private String gender;
    private Button registerf2;
    int age= 0;
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
        mfirebaseAuth = mfirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mEtAge = findViewById(R.id.et_age);

        mEtAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivityF2.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mEtName = findViewById(R.id.et_name);
        mEtheight = findViewById(R.id.et_height);
        mEtweight = findViewById(R.id.et_weight);
        rg_gender = findViewById(R.id.rg_gender);
        registerf2 = findViewById(R.id.btn_registerf2);


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton genderButton = (RadioButton)findViewById(i);
                gender = genderButton.getText().toString();
            }
        });

        registerf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                FirebaseUser user = mfirebaseAuth.getCurrentUser();
                String email = user.getEmail();
                String uid = user.getUid();
                String name = mEtName.getText().toString().trim();
                int age =cal.get (cal.YEAR)-myCalendar.get(Calendar.YEAR)+1;
                double height = Double.parseDouble(mEtheight.getText().toString());
                double weight = Double.parseDouble(mEtweight.getText().toString());
                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장

                HashMap<Object,String> hashMap = new HashMap<>();
                hashMap.put("Uid",uid);
                hashMap.put("Email",email);
                hashMap.put("Name",name);
                hashMap.put("Age",age+"");
                hashMap.put("Height",height+"");
                hashMap.put("Weight",weight+"");
                hashMap.put("Gender",gender);
                mDatabaseRef.child(uid).setValue(hashMap);

                //가입이 이루어져을시 가입 화면을 빠져나감.
                Intent intent = new Intent(RegisterActivityF2.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivityF2.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText mEtAge = (EditText) findViewById(R.id.et_age);
        mEtAge.setText(sdf.format(myCalendar.getTime()));

    }

}