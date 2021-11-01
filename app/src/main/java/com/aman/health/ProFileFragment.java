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

    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabasePFRef; //실시간 데이터베이스
    private TextView info_email, info_name, info_age, info_height, info_weight;
    private ImageView iv_pfimg;
    private BarChart barchart;


    DatabaseReference mDatabaseRef = null;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기

        assert uid != null;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("walkcount");
        barchart = view.findViewById(R.id.barchart);
        List<BarEntry> entry_chart = new ArrayList<>();




        mDatabaseRef.limitToFirst(7).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    int key = Integer.parseInt(Objects.requireNonNull(snapshot.getKey()).substring(4,8));
                    entry_chart.add(new BarEntry(key, snapshot.getValue(long.class)));
                    //entry_chart.add(new Entry(key, snapshot.getValue(long.class)));
                    Log.d("차트", ""+ key);

                }
                Log.d("차트", ""+ entry_chart);

                BarDataSet barDataSet = new BarDataSet(entry_chart, "Step");
                //여기에 lineDataSet 설정
                barDataSet.setColor(Color.parseColor("#3F52E3"));
                barDataSet.setValueTextSize(10f);


                BarData barData = new BarData();
                barData.setDrawValues(false);
                barData.setBarWidth(0.6f);

                barData.addDataSet(barDataSet);

                XAxis xAxis = barchart.getXAxis(); // x 축 설정
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
                xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value)

                        {
                            /*
                            Date date = new Date((long)value);
                            Log.d("날짜", ""+ date);
                            Log.d("날짜", ""+ (long)value);
                            //Specify the format you'd like
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                             */
                            return String.valueOf(value).substring(0, 2) + "/" + String.valueOf(value).substring(2, 4);
                            //return sdf.format((long)value);
                        }
                    });
                xAxis.setDrawLabels(true);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(true);

                YAxis yAxisLeft = barchart.getAxisLeft(); //Y축의 왼쪽면 설정
                yAxisLeft.setDrawGridLines(false);
                yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
                yAxisLeft.setAxisMinimum(0f);
                yAxisLeft.setAxisMaximum(10000f);
                yAxisLeft.setGranularity(5000f);
                yAxisLeft.setDrawAxisLine(false);

                //오른쪽 축 비활성화
                YAxis yAxisRight = barchart.getAxisRight();
                yAxisRight.setDrawLabels(false);
                yAxisRight.setDrawAxisLine(false);
                yAxisRight.setDrawGridLines(false);


                barchart.setData(barData);
                barchart.setDescription(null);
                barchart.setPinchZoom(false);
                barchart.animateY(1000);
                barchart.setTouchEnabled(false);
                barchart.invalidate();
                barchart.setDrawGridBackground(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
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




        mfirebaseAuth = mfirebaseAuth.getInstance();
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



    public class TheFormatYouWant extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            Date date = new Date((long)value);
            //Specify the format you'd like
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
            return sdf.format(date);

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
