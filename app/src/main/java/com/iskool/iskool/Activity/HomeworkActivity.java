package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iskool.iskool.Fragment.HomeworkFragment;
import com.iskool.iskool.Fragment.PdfViewFragmen;
import com.iskool.iskool.Fragment.QuizFrag;
import com.iskool.iskool.Fragment.UploadAnsFrag;
import com.iskool.iskool.Fragment.VideoFragment;
import com.iskool.iskool.Models.AssignmentModel;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.HomeworkModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.QuizModel;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.Models.TopicModel;
import com.iskool.iskool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class HomeworkActivity extends AppCompatActivity {
    String path;
    HomeworkModel homeworkModel;
    CourseModel courseModel;
    StudentModel studentModel;
    TopicModel topicModel;
    QuizModel quizModel;
    AssignmentModel assignmentModel;
    ModelClass modelClas;
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
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("STUDENTS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentModel = documentSnapshot.toObject(StudentModel.class);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("HOMEWORK");
        path = getIntent().getStringExtra("path");

        getDb();
    }

    public void clicked(ModelClass modelClass)
    {
        stage=1;
        modelClas=modelClass;
        startProgress(this);
        String parent =modelClass.getReff().getParent().getId();
        if (parent.equalsIgnoreCase("TOPICS"))
        {
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
                }
            });

        }
        else if (parent.equalsIgnoreCase("ASSIGNMENTS"))
        {
            modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    assignmentModel = documentSnapshot.toObject(AssignmentModel.class);
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    FirebaseStorage.getInstance().getReferenceFromUrl(assignmentModel.getLink()).getBytes(10 * 1024 * 1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    try (FileOutputStream fos = HomeworkActivity.this.openFileOutput(assignmentModel.getSelf().getName() + ".pdf", Context.MODE_PRIVATE)) {

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
                                Log.w("tag", "start");
                                String[] files = HomeworkActivity.this.fileList();

                                File ful = new File(HomeworkActivity.this.getFilesDir(), assignmentModel.getSelf().getName() + ".pdf");
                                PdfViewFragmen pdfViewFragmen = new PdfViewFragmen(Uri.fromFile(ful));
                                stopProgress();
                                getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, pdfViewFragmen, null).commit();
                            }

                        }
                    });
                }
            });

        }
        else if (parent.equalsIgnoreCase("QUIZ"))
        {
            modelClass.getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    quizModel = documentSnapshot.toObject(QuizModel.class);
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    QuizFrag quizFrag = new QuizFrag(quizModel);
                    stopProgress();
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,quizFrag,null).commit();
                }
            });

        }
    }

    private void getDb() {
        stage=0;
        startProgress(this);
        FirebaseFirestore.getInstance().document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);
                documentSnapshot.getReference().collection("HOMEWORK").document(DateFormat.format("yyyyMMdd", calendar).toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot1) {
                        if (documentSnapshot1.exists())
                        {
                            homeworkModel = documentSnapshot1.toObject(HomeworkModel.class);
                        }
                        else
                        {
                            homeworkModel= new HomeworkModel();
                            Toast.makeText(HomeworkActivity.this, "Nahi hai", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        loadHW();
                        stopProgress();
                    }
                });

            }
        });

    }

    private void loadHW() {


            if (findViewById(R.id.FragContainer)!=null)
            {
                HomeworkFragment homeworkFragment = new HomeworkFragment(courseModel,studentModel);
                getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer,homeworkFragment,null).commit();

            }
    }
    @Override
    public void onBackPressed() {

        if (stage==1)
        {
            stage=0;
            getDb();
        }
        else if (stage==0)
        {
            super.onBackPressed();
        }
        else if (stage==2)
        {
            stage=1;
            clicked(modelClas);
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
            getDb();
        }
        else if (stage==0)
        {
            finish();
        }
        else if (stage==2)
        {
            stage=1;
            clicked(modelClas);
        }
        else
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void upAns() {
        stage = 2;
        UploadAnsFrag uploadAnsFrag = new UploadAnsFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, uploadAnsFrag, null).commit();
    }
    public void ansUp(final File ful) {

        final AppCompatDialog appCompatDialog = new AppCompatDialog(this);

        String path;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(assignmentModel.getSelf().getReff().getPath());

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
                    if (studentModel.getAssignments()==null) {
                        studentModel.setAssignments(new HashMap<String, String>());
                    }
                    studentModel.getAssignments().put(assignmentModel.getSelf().getReff().getId(),task.getResult().getStorage().toString());
                    studentModel.getSelf().getReff().set(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getDb();
                            appCompatDialog.dismiss();
                            Toast.makeText(HomeworkActivity.this, "Completed", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                getDb();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeworkActivity.this, "Problem", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
