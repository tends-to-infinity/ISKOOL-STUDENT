package com.iskool.iskool.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iskool.iskool.Activity.ClassroomActivity;
import com.iskool.iskool.Adapter.TopicSelectorAdapter;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    EditText etUserName ,etPassword, etEmail,etpass ,etStname;
    Button btnLogin,btnSignUp;
    String email,pass,emails,passs,scid,scname,sname;
    ArrayList<ModelClass> courses=new ArrayList<>();
    ModelClass course;
    Spinner spinner;
    TextView ltos,stol;
    LinearLayout llSignUp, llLogin;
    ProgressDialog progressDoalog;
    void  startProgress(Context context)
    {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.show();

        progressDoalog.setContentView(R.layout.progress);
        progressDoalog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDoalog.setCanceledOnTouchOutside(false);

    }
    void  stopProgress()
    {
        progressDoalog.dismiss();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName =findViewById(R.id.etLoginEmail);
        etStname=findViewById(R.id.etStudentName);
        etPassword=findViewById(R.id.etLoginPassword);
        btnLogin=findViewById(R.id.btnLoginLogin);
        etEmail=findViewById(R.id.etSignUpEmail);
        etpass=findViewById(R.id.etSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        spinner=findViewById(R.id.spinnerSignUp);
        stol=findViewById(R.id.tvSignupLogin);
        ltos=findViewById(R.id.tvLoginSignup);
        llLogin=findViewById(R.id.llLoginLogin);
        llSignUp=findViewById(R.id.llSignUpSignUp);

        FirebaseFirestore.getInstance().collection("COURSES").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    CourseModel courseModel = documentSnapshot.toObject(CourseModel.class);
                    courses.add(courseModel.getSelf());
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    setSpinner();
                    ltos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llSignUp.setVisibility(View.VISIBLE);
                            llLogin.setVisibility(View.GONE);

                        }
                    });
                    stol.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llSignUp.setVisibility(View.GONE);
                            llLogin.setVisibility(View.VISIBLE);

                        }
                    });
                    btnSignUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            email=etEmail.getText().toString();
                            sname=etStname.getText().toString();
                            pass=etpass.getText().toString();
                            if(email.equals("")||sname.equals("")||pass.equals(""))
                            {
                                Toast.makeText(LoginActivity.this, "Khali", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                signUp();
                            }
                        }
                    });
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            email=etUserName.getText().toString();
                            pass=etPassword.getText().toString();
                            if (email.equals("")||pass.equals(""))
                            {
                                Toast.makeText(LoginActivity.this, "Khali", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                login();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void login() {
        startProgress(this);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    stopProgress();
                    finish();
                    startActivity(new Intent(LoginActivity.this,ClassroomActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp() {

        startProgress(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                DocumentReference sreff= FirebaseFirestore.getInstance().collection("STUDENTS").document(task.getResult().getUser().getUid());
                StudentModel studentModel = new StudentModel();
                studentModel.setSelf(new ModelClass(sname,sreff));
                studentModel.setCourse(course);
                sreff.set(studentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        stopProgress();
                        finish();
                        startActivity(new Intent(LoginActivity.this, ClassroomActivity.class));

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setSpinner() {

        TopicSelectorAdapter topicSelectorAdapter = new TopicSelectorAdapter(this,courses);
        spinner.setAdapter(topicSelectorAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 course = courses.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                course=null;

            }
        });
    }
}
