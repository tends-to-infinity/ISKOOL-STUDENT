package com.iskool.iskool.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.iskool.iskool.Adapter.ModelListAdapter;
import com.iskool.iskool.Models.CourseModel;
import com.iskool.iskool.Models.HomeworkModel;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.Calendar;

public class HomeworkFragment extends Fragment {



    private HomeworkModel homeworkModel;
    private ModelListAdapter modelListAdapter,modelListAdapter2,modelListAdapter3;
    private RecyclerView rvTOpics,rvQuiz,rvAss;

    private TextView tvDate;
    StudentModel studentModel;
    private CourseModel courseModel;
    TextView topic,quiz,ass;
    ImageView ivprevious,ivnext;
    private Calendar calendar = Calendar.getInstance();

    public HomeworkFragment() {
        // Required empty public constructor
    }

    public HomeworkFragment(CourseModel courseModel, StudentModel studentModel) {
        this.courseModel = courseModel;
        this.studentModel=studentModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homework, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAss=view.findViewById(R.id.rvAssignments);
        rvQuiz=view.findViewById(R.id.rvQuiz);
        rvTOpics=view.findViewById(R.id.rvTopics);
        String dateText = DateFormat.format("dd-MM-yyyy", calendar).toString();
        topic = view.findViewById(R.id.topictitle);
        quiz = view.findViewById(R.id.quiztitle);
        ass = view.findViewById(R.id.asstitle);
        ivprevious=view.findViewById(R.id.ivprevious);
        ivnext = view.findViewById(R.id.ivnext);

        tvDate = view.findViewById(R.id.tvDate);
        tvDate.setText(dateText);
        ivnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+1);
                getHw(calendar);
            }
        });
        ivprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
                getHw(calendar);
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        getHw(calendar);


    }

    private void getHw(Calendar calendar) {


        courseModel.getSelf().getReff().collection("HOMEWORK").document(DateFormat.format("yyyyMMdd", calendar).toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    homeworkModel=documentSnapshot.toObject(HomeworkModel.class);
                }
                else
                {
                    homeworkModel= new HomeworkModel();
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                loadData();
            }
        });


    }

    private void loadData() {
        if (homeworkModel!=null)
        {
            tvDate.setText(DateFormat.format("dd-MM-yyyy", calendar).toString());
            if (homeworkModel.getSelf()!=null)
            {

                tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDate();
                    }
                });
            }

            if (homeworkModel.getTopics()!=null)
            {
                topic.setVisibility(View.VISIBLE);

                intialise();
            }
            else
            {
                topic.setVisibility(View.GONE);
                rvTOpics.setAdapter(null);

            }
            if (homeworkModel.getAssignments()!=null)
            {
                quiz.setVisibility(View.VISIBLE);

                intiAss();
            }
            else
            {
                quiz.setVisibility(View.GONE);

                rvAss.setAdapter(null);

            }
            if (homeworkModel.getQuiz()!=null)
            {
                ass.setVisibility(View.VISIBLE);

                intiQuiz();
            }
            else
            {
                ass.setVisibility(View.GONE);

                rvQuiz.setAdapter(null);
            }
        }

    }



    private void setDate() {

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {


                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("dd-MM-yyyy", calendar).toString();
                tvDate.setText(dateText);
                calendar=calendar1;


                getHw(calendar1);

                //dates=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();



    }

    private void intialise() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter = new ModelListAdapter(homeworkModel.getTopics(),R.layout.subject_chapters_view_rv,getActivity(),studentModel);

        rvTOpics.setAdapter(modelListAdapter);
        rvTOpics.setLayoutManager(manager);
    }
    private void intiQuiz() {

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter3 = new ModelListAdapter(homeworkModel.getQuiz(),R.layout.subject_chapters_view_rv,getActivity(),studentModel);

        rvQuiz.setAdapter(modelListAdapter3);
        rvQuiz.setLayoutManager(manager);

    }

    private void intiAss() {

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter2 = new ModelListAdapter(homeworkModel.getAssignments(),R.layout.subject_chapters_view_rv,getActivity(),studentModel);

        rvAss.setAdapter(modelListAdapter2);
        rvAss.setLayoutManager(manager);
    }

}
