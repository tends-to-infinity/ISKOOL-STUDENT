package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iskool.iskool.Fragment.ListFraf;
import com.iskool.iskool.Fragment.PdfViewFragmen;
import com.iskool.iskool.Fragment.UploadAnsFrag;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ExamModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class ExamActivity extends AppCompatActivity {
    String path;
    CourseModel courseModel;
    StudentModel studentModel;
    ExamModel examModel;
    TextView tvtimer;
    ModelClass modelClas;
    int phase =0;
    private ProgressDialog progressDoalog;

    int stage = 0;

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
        setContentView(R.layout.activity_exam);
        path = getIntent().getStringExtra("path");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("EXAM");
        tvtimer = findViewById(R.id.timer);
        getdb();

    }

    private void getdb() {
        tvtimer.setVisibility(View.GONE);

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
        ListFraf listFraf = new ListFraf(courseModel.getExams());
        if (findViewById(R.id.FragContainer)!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,listFraf,null).commit();

        }

    }
    public void openExam(ModelClass modelClass)
    {
        stage=1;
        modelClas=modelClass;
        startProgress(this);
        modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 examModel =documentSnapshot.toObject(ExamModel.class);
                if (examModel.getStartTime().compareTo(Timestamp.now())>0)
                {
                    phase=0;
                    Toast.makeText(ExamActivity.this, "Exam will begin at  "+DateFormat.format("MMM d, h:mm a", examModel.getStartTime().toDate()).toString(), Toast.LENGTH_SHORT).show();
                    stopProgress();
                }
                else if (examModel.getStartTime().compareTo(Timestamp.now())<=0)
                {

                    FirebaseStorage.getInstance().getReferenceFromUrl(examModel.getLink()).getBytes(10 * 1024 * 1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    try (FileOutputStream fos = ExamActivity.this.openFileOutput(examModel.getSelf().getName() + ".pdf", Context.MODE_PRIVATE)) {

                                        fos.write(bytes);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                        @Override
                        public void onComplete(@NonNull Task<byte[]> task) {
                            if (task.isSuccessful()) {
                                tvtimer.setVisibility(View.VISIBLE);
                                Log.w("tag", "start");
                                String[] files = ExamActivity.this.fileList();

                                File ful = new File(ExamActivity.this.getFilesDir(), examModel.getSelf().getName() + ".pdf");
                                PdfViewFragmen pdfViewFragmen = new PdfViewFragmen(Uri.fromFile(ful));
                                stopProgress();
                                if ((Timestamp.now().getSeconds()-examModel.getStartTime().getSeconds())<examModel.getDuration()*60)
                                {
                                    phase=1;

                                    Toast.makeText(ExamActivity.this, "Chal raha  hai", Toast.LENGTH_SHORT).show();
                                    long tme = (examModel.getStartTime().toDate().getTime()+examModel.getDuration()*60000)-Timestamp.now().toDate().getTime();
                                    new CountDownTimer(tme,1000) {
                                        @Override
                                        public void onTick(long l) {
                                            long ss= l/1000;
                                            long h,m,s;
                                            h=ss/3600;
                                            m= (ss%3600)/60;
                                            s=(ss%60);
                                            tvtimer.setText(""+h+":"+m+":"+s);

                                        }

                                        @Override
                                        public void onFinish() {
                                            Toast.makeText(ExamActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                                        }
                                    }.start();

                                }
                                else
                                {
                                    phase=2;
                                    tvtimer.setText("00:00:00");

                                }
                                getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, pdfViewFragmen, null).commit();
                            }

                        }
                    });


                }


            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            }
        });
    }

    public void pdfBut()
    {

        if (phase==1)
        {
            stage=2;
            UploadAnsFrag uploadAnsFrag = new UploadAnsFrag();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, uploadAnsFrag, null).commit();

        }
        else if (phase==2)
        {
            Toast.makeText(ExamActivity.this, "nahi", Toast.LENGTH_SHORT).show();

        }
    }

    public void ansUp(File ful) {


        final AppCompatDialog appCompatDialog = new AppCompatDialog(this);

        String path;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(examModel.getSelf().getReff().getPath());

        storageReference.child("SUBMISSION").child(studentModel.getSelf().getReff().getId()).putFile(Uri.fromFile(ful)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                appCompatDialog.show();
                appCompatDialog.setContentView(R.layout.uploading);
                TextView tvper = appCompatDialog.findViewById(R.id.tvUploadper);
                int per = (int) (100 * (taskSnapshot.getBytesTransferred() * 1.0) / (taskSnapshot.getTotalByteCount() * 1.0));
                tvper.setText(per + "% COMPLETED");
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    if (studentModel.getExams()==null) {
                        studentModel.setExams(new HashMap<String, String>());
                    }
                    studentModel.getExams().put(examModel.getSelf().getReff().getId(),task.getResult().getStorage().toString());
                    studentModel.getSelf().getReff().set(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getdb();
                            appCompatDialog.dismiss();
                            Toast.makeText(ExamActivity.this, "Completed", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                getdb();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExamActivity.this, "Problem", Toast.LENGTH_SHORT).show();
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
        else if (stage==2)
        {
            stage=1;
            openExam(modelClas);
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
        else if (stage==2)
        {
            stage=1;
            openExam(modelClas);
        }

        return super.onOptionsItemSelected(item);
    }

}
