package com.aman.health;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//MPAndroidChart import
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProFileFragment extends Fragment {

    private DatabaseReference mDatabasePFRef; //실시간 데이터베이스
    private TextView info_email, info_name, info_age, info_height, info_weight;
    private ImageView iv_pfimg;
    private BarChart barchart, waterbarchart;


    DatabaseReference mDatabaseRef = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

        assert uid != null;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        barchart = view.findViewById(R.id.barchart);
        waterbarchart = view.findViewById(R.id.Waterbarchart);


        mDatabaseRef.child("walkcount").limitToLast(7).addValueEventListener(new ValueEventListener() {
            final List<BarEntry> step_value = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int key = parseInt(Objects.requireNonNull(snapshot.getKey()).substring(4, 8));
                    step_value.add(new BarEntry(key, snapshot.getValue(long.class)));
                    //Log.d("차트", "" + key);
                }
                //Log.d("차트", "" + step_value);
                FbData.StepChartset(step_value, barchart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Failed to read value.", error.toException());
            }
        });

        mDatabaseRef.child("watercount").limitToLast(7).addValueEventListener(new ValueEventListener() {
            final List<BarEntry> water_value = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int key = parseInt(Objects.requireNonNull(snapshot.getKey()).substring(4, 8));
                    water_value.add(new BarEntry(key, snapshot.getValue(long.class)));

                }
                //Log.d("차트", "" + water_value);
                FbData.WaterChartset(water_value, waterbarchart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Failed to read value.", error.toException());
            }
        });

        /*
        for(int a=1, b=1; a<11; a++, b+=2) {
            entry_chart.add(new Entry(a, b));
        }
        */

        /* 만약 (2, 3) add하고 (2, 5)한다고해서
        기존 (2, 3)이 사라지는게 아니라 x가 2인곳에 y가 3, 5의 점이 찍힘 */

        info_email = view.findViewById(R.id.info_email);
        info_name = view.findViewById(R.id.info_name);
        info_age = view.findViewById(R.id.info_age);
        info_height = view.findViewById(R.id.info_height);
        info_weight = view.findViewById(R.id.info_weight);
        iv_pfimg = view.findViewById(R.id.iv_pfimg);


        mDatabasePFRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("profile"); // 파이어베이스 realtime database 에서 정보 가져오기
        DatabaseReference email = mDatabasePFRef.child("Email");    // 이메일
        DatabaseReference name = mDatabasePFRef.child("Name");
        DatabaseReference age = mDatabasePFRef.child("Age");
        DatabaseReference height = mDatabasePFRef.child("Height");
        DatabaseReference weight = mDatabasePFRef.child("Weight");

        // uid = 파이어베이스 유저 고유 uid , nickname = 데이터 베이스 child 명

        getFireBaseProfileImage(uid);


        // 이메일 띄워줌
        email.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_email.setText(value);
                Log.e("test", "나이 " + name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // 이메일 띄워줌
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_name.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        age.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_age.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        height.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_height.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        weight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_weight.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }


    public static class MyValueformatter extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(value).substring(0, 2) + "/" + String.valueOf(value).substring(2, 4);
        }
    }

    /**
     * 이미지 (파이어베이스 스토리지에서 가져오기)
     */
    private void getFireBaseProfileImage(String uid) {
        //우선 디렉토리 파일 하나만든다.
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/uid");
        //이 파일안에 저 디렉토리가 있는지 확인
        if (!file.isDirectory()) { //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
        downloadImg(uid); //이미지 다운로드해서 가져오기 메서드
    }

    /**
     * 이미지 다운로드해서 가져오기 메서드
     */
    private void downloadImg(String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        storageRef.child("UsersprofileImages").child("uid/" + uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Glide.with(getContext()).load(uri).circleCrop().into(iv_pfimg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //실패시
            }
        });
    }
}
