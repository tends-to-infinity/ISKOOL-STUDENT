package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

public class ClassroomActivity extends AppCompatActivity {
    StudentModel studentModel;
    int stage = 0;
    CourseModel courseModel;
    LinearLayout llHomework,llAssignments,llClassroom,llQuiz,llExams,llHome;
    private ProgressDialog progressDoalog;
    TextView tvName;
    LinearLayout llprofile;
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
        setContentView(R.layout.activity_classroom);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CLASSROOM");
        tvName = findViewById(R.id.tvName);
        llprofile=findViewById(R.id.llprofile);

        getdb();
    }

    private void getdb() {

        startProgress(this);
        FirebaseFirestore.getInstance().collection("STUDENTS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentModel = documentSnapshot.toObject(StudentModel.class);

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    tvName.setText("Welcome "+studentModel.getSelf().getName()+"!");
                    Toast.makeText(ClassroomActivity.this, "Welcome "+studentModel.getSelf().getName(), Toast.LENGTH_SHORT).show();
                    studentModel.getCourse().getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            courseModel = documentSnapshot.toObject(CourseModel.class);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            loadClassroom();
                            stopProgress();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ClassroomActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadClassroom() {

        llAssignments=findViewById(R.id.llAssignments);
        llClassroom=findViewById(R.id.llClassroom);
        llExams=findViewById(R.id.llExams);
        llHomework=findViewById(R.id.llHomework);
        llQuiz=findViewById(R.id.llQuiz);
        llHome= findViewById(R.id.llHome);
        llprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,ProfileActivity.class).putExtra("path",studentModel.getSelf().getReff().getPath()));
            }
        });

        llAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,AssignmentsActivity.class).putExtra("path",courseModel.getSelf().getReff().getPath()));
            }
        });
        llHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,HomeworkActivity.class).putExtra("path",courseModel.getSelf().getReff().getPath()));

            }
        });
        llClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,SubjectActivity.class).putExtra("path",courseModel.getSelf().getReff().getPath()));

            }
        });
        llQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,QuizActivity.class).putExtra("path",courseModel.getSelf().getReff().getPath()));

            }
        });

        llExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassroomActivity.this,ExamActivity.class).putExtra("path",courseModel.getSelf().getReff().getPath()));

            }
        });

    }




}
