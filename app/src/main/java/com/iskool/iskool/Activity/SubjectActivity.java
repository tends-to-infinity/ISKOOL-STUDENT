package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool.Fragment.ListFraf;
import com.iskool.iskool.Fragment.VideoFragment;
import com.iskool.iskool.Models.ChapterModel;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.Models.SubjectModel;
import com.iskool.iskool.Models.TopicModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {
    String path;
    CourseModel courseModel;
    SubjectModel subjectModel;
    StudentModel studentModel;
    ChapterModel chapterModel;
    TopicModel topicModel;
    int stage =0;
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
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SUBJECTS");
        path = getIntent().getStringExtra("path");
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
                                setData(courseModel.getSubjects());
                                stopProgress();
                            }

                        }
                    });
                }
            }
        });
    }

    private void setData(ArrayList<ModelClass> list) {
        ListFraf listFraf = new ListFraf(list,studentModel);
        if (findViewById(R.id.FragContainer)!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,listFraf,null).commit();
        }

    }
    public void nextfrag(ModelClass modelClass)
    {
        startProgress(this);
        if (stage==0)
        {
            stage=1;
            modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    subjectModel = documentSnapshot.toObject(SubjectModel.class);
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    setData(subjectModel.getChapters());
                    stopProgress();
                }
            });
        }
        else if (stage == 1)
        {
            stage =2;
            modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    chapterModel = documentSnapshot.toObject(ChapterModel.class);
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    setData(chapterModel.getTopics());
                    stopProgress();
                }
            });
        }

    }
    public void loadVid(ModelClass modelClass)
    {
        startProgress(this);
        stage = 3;
        modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                topicModel = documentSnapshot.toObject(TopicModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                VideoFragment videoFragment = new VideoFragment(topicModel);
                getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,videoFragment,null).commit();
                stopProgress();

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (stage==0)
        {

            super.onBackPressed();
        }
        else if (stage==1)
        {
            stage=0;
            setData(courseModel.getSubjects());
        }
        else if (stage==2)
        {
            stage=1;
            setData(subjectModel.getChapters());
        }
        else if (stage==3)
        {
            stage=2;
            setData(chapterModel.getTopics());
        }
        else
        {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            if (stage==0)
            {
                finish();
            }
            else if (stage==1)
            {
                stage=0;
                setData(courseModel.getSubjects());
            }
            else if (stage==2)
            {
                stage=1;
                setData(subjectModel.getChapters());
            }
            else if (stage==3)
            {
                stage=2;
                setData(chapterModel.getTopics());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void vidFin() {
        stage=2;
        setData(chapterModel.getTopics());
    }
}
