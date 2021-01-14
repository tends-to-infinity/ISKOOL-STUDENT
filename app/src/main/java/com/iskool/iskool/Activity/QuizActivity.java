package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool.Fragment.ListFraf;
import com.iskool.iskool.Fragment.QuizFrag;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.QuizModel;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

public class QuizActivity extends AppCompatActivity {

    String path;
    CourseModel courseModel;
    StudentModel studentModel;
    QuizModel quizModel;
    int stage=0;
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
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("QUIZ");
        path = getIntent().getStringExtra("path");
        getdb();
    }

    private void getdb() {
        stage=0;
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
                    FirebaseFirestore.getInstance().document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            courseModel = documentSnapshot.toObject(CourseModel.class);


                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                            if (task1.isSuccessful())
                            {
                                setData();
                                stopProgress();
                            }

                        }
                    });
                }
            }
        });
    }
    private void setData() {
        ListFraf listFraf = new ListFraf(courseModel.getQuiz(),studentModel);
        if (findViewById(R.id.FragContainer)!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,listFraf,null).commit();
        }

    }

    public void startQuiz(ModelClass modelClass)
    {
        stage=1;
        startProgress(this);
        modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                quizModel = documentSnapshot.toObject(QuizModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                stopProgress();
                QuizFrag quizFrag = new QuizFrag(quizModel);
                getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,quizFrag,null).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (stage==1)
        {
            stage=0;
            getdb();
        }
        else if (stage==0)
        {
            super.onBackPressed();
        }
        else
        {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (stage==1)
        {
            stage=0;
            getdb();
        }
        else if (stage==0)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
