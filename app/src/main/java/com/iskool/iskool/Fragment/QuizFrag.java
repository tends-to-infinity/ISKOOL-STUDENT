package com.iskool.iskool.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool.Models.QuizModel;
import com.iskool.iskool.Models.Squiz;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFrag extends Fragment {

    TextView tvQues;
    Button o1,o2,o3,o4,next,prev;
    ArrayList<Integer> answered = new ArrayList<>();
    int pos;
    QuizModel quizModel;
    Button btnSubmit;
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


    public QuizFrag(QuizModel quizModel) {
        this.quizModel = quizModel;
    }

    public QuizFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQues = view.findViewById(R.id.tvQues);
        o1=view.findViewById(R.id.op1);
        o2=view.findViewById(R.id.op2);
        o3=view.findViewById(R.id.op3);
        o4=view.findViewById(R.id.op4);
        next=view.findViewById(R.id.nexts);
        prev=view.findViewById(R.id.prev);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        createques(pos);
    }
    private void createques(final int pos) {
        allwhite();
        if (answered.size()>pos)
        {
            if (answered.get(pos)==0)
            {
                o1.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));
            }
            if (answered.get(pos)==1)
            {
                o2.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));
            }
            if (answered.get(pos)==2)
            {
                o3.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));
            }
            if (answered.get(pos)==3)
            {
                o4.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));
            }
        }
        tvQues.setText(quizModel.getQues().get(pos));
        o1.setText(quizModel.getOptions().get(String.valueOf(pos)).get(0));
        o2.setText(quizModel.getOptions().get(String.valueOf(pos)).get(1));
        o3.setText(quizModel.getOptions().get(String.valueOf(pos)).get(2));
        o4.setText(quizModel.getOptions().get(String.valueOf(pos)).get(3));
        o1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allwhite();
                o1.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));

                answered.add(pos,0);
                if (answered.size()>pos+1)
                {
                    answered.remove(pos+1);
                }

            }
        });
        o2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                allwhite();
                o2.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));

                answered.add(pos,1);
                if (answered.size()>pos+1)
                {
                    answered.remove(pos+1);
                }

            }
        });
        o3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allwhite();
                o3.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));

                answered.add(pos,2);
                if (answered.size()>pos+1)
                {
                    answered.remove(pos+1);
                }

            }
        });
        o4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allwhite();
                o4.setBackground(getActivity().getDrawable(R.drawable.button_stroke_dark));


                answered.add(pos,3);
                if (answered.size()>pos+1)
                {
                    answered.remove(pos+1);
                }

            }
        });
        if (pos==0)
        {
            prev.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);

        }
        else
        {
            prev.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);

            next.setVisibility(View.VISIBLE);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createques(pos-1);
                }
            });
        }
        if (pos <quizModel.getQues().size()-1)
        {
            btnSubmit.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (answered.size()<=pos)
                    {
                        answered.add(-1);

                    }

                    createques(pos+1);
                }
            });
        }
        else
        {
            btnSubmit.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (answered.size()<=pos)
                    {
                        answered.add(-1);

                    }
                    int t=0;
                    for (int i=0;i<answered.size();i++)
                    {
                        if (quizModel.getAnswer().get(i)==answered.get(i))
                        {
                            t++;
                        }

                    }
                    Toast.makeText(getActivity(), ""+answered.toString()+"Total = "+t, Toast.LENGTH_SHORT).show();
                    startProgress(getActivity());
                    final int finalT = t;
                    FirebaseFirestore.getInstance().collection("STUDENTS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            StudentModel student = documentSnapshot.toObject(StudentModel.class);
                            student.getQuiz().put(quizModel.getSelf().getReff().getId(),new Squiz(answered, finalT));
                            documentSnapshot.getReference().set(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    stopProgress();
                                    getActivity().finish();
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    private void allwhite() {

        o1.setBackground(getActivity().getDrawable(R.color.white));
        o2.setBackground(getActivity().getDrawable(R.color.white));
        o3.setBackground(getActivity().getDrawable(R.color.white));
        o4.setBackground(getActivity().getDrawable(R.color.white));

    }

}

