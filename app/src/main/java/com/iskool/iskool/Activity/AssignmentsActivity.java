package com.iskool.iskool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.iskool.iskool.Fragment.ListFraf;
import com.iskool.iskool.Fragment.PdfViewFragmen;
import com.iskool.iskool.Fragment.UploadAnsFrag;
import com.iskool.iskool.Models.AssignmentModel;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentsActivity extends AppCompatActivity {
    String path;
    CourseModel courseModel;
    StudentModel studentModel;
    AssignmentModel assignmentModel;

    int stage = 0;
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
        setContentView(R.layout.activity_assignments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ASSIGNMENT");
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
                if (task.isSuccessful()) {
                    FirebaseFirestore.getInstance().document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            courseModel = documentSnapshot.toObject(CourseModel.class);


                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                            if (task1.isSuccessful()) {
                                stopProgress();
                                setData();
                            }

                        }
                    });
                }
            }
        });
    }

    private void setData() {
        ListFraf listFraf = new ListFraf(courseModel.getAssignments(),studentModel);
        if (findViewById(R.id.FragContainer) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, listFraf, null).commit();
        }

    }

    public void loadPdf(ModelClass modelClass) {
        stage = 1;
        startProgress(this);
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
                                try (FileOutputStream fos = AssignmentsActivity.this.openFileOutput(assignmentModel.getSelf().getName() + ".pdf", Context.MODE_PRIVATE)) {

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
                            String[] files = AssignmentsActivity.this.fileList();

                            File ful = new File(AssignmentsActivity.this.getFilesDir(), assignmentModel.getSelf().getName() + ".pdf");
                            PdfViewFragmen pdfViewFragmen = new PdfViewFragmen(Uri.fromFile(ful));
                            stopProgress();
                            getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, pdfViewFragmen, null).commit();
                        }

                    }
                });
            }
        });
    }

    public void upAns() {
        UploadAnsFrag uploadAnsFrag = new UploadAnsFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragContainer, uploadAnsFrag, null).commit();
    }

    public void ansUp(final File ful) {

                    final AppCompatDialog appCompatDialog = new AppCompatDialog(this);

                    String path;
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(assignmentModel.getSelf().getReff().getPath());
                    appCompatDialog.setTitle("Uploading");

                    storageReference.child("SUBMISSION").child(studentModel.getSelf().getReff().getId()).putFile(Uri.fromFile(ful)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            appCompatDialog.show();
                            appCompatDialog.setCanceledOnTouchOutside(false
                            );
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
                                        getdb();
                                        appCompatDialog.dismiss();
                                        Toast.makeText(AssignmentsActivity.this, "Completed", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AssignmentsActivity.this, "Problem", Toast.LENGTH_SHORT).show();
                        }
                    });


    }

}
