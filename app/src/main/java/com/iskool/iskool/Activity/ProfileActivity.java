package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool.Adapter.ModelListAdapter;
import com.iskool.iskool.Login.LoginActivity;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    String path;
    StudentModel studentModel;
    TextView tvName,tvCourse;
    RecyclerView rvprofileTopicRv;
    ArrayList<ModelClass> topics = new ArrayList<>();
    LinearLayout llLogout;
    ImageView proback;
    private ProgressDialog progressDoalog;
    void startProgress(Context context) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.show();

        progressDoalog.setContentView(R.layout.progress);
        progressDoalog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDoalog.setCanceledOnTouchOutside(false);

    }

    void stopProgress() {
        progressDoalog.dismiss();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         path = getIntent().getStringExtra("path");
        rvprofileTopicRv=findViewById(R.id.rvprofileTopicRv);
        tvName = findViewById(R.id.tvProfileName);
        llLogout=findViewById(R.id.llLogout);
        proback = findViewById(R.id.proback);
        tvCourse = findViewById(R.id.tvProfileCourseName);
        loadSt();
    }

    private void loadSt() {
        startProgress(this);
        FirebaseFirestore.getInstance().document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentModel = documentSnapshot.toObject(StudentModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tvName.setText(studentModel.getSelf().getName());
                stopProgress();
                proback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                tvCourse.setText(studentModel.getCourse().getName());
                Iterator iterator = studentModel.getTopics().entrySet().iterator();
                while (iterator.hasNext())
                {
                    Map.Entry mapElement = (Map.Entry)iterator.next();
                    topics.add((ModelClass)mapElement.getValue());
                }
                ModelListAdapter modelListAdapter = new ModelListAdapter(topics,R.layout.subject_chapters_view_rv,ProfileActivity.this);
                GridLayoutManager manager = new GridLayoutManager(ProfileActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
                rvprofileTopicRv.setAdapter(modelListAdapter);
                rvprofileTopicRv.setLayoutManager(manager);
                llLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }
}
