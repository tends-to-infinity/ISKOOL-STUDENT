package com.iskool.iskool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.iskool.iskool.Activity.AssignmentsActivity;
import com.iskool.iskool.Activity.ExamActivity;
import com.iskool.iskool.Activity.HomeworkActivity;
import com.iskool.iskool.Activity.ProfileActivity;
import com.iskool.iskool.Activity.QuizActivity;
import com.iskool.iskool.Activity.SubjectActivity;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

public class ModelListAdapter extends RecyclerView.Adapter<ModelListAdapter.ViewHolder> {

    ArrayList<ModelClass> modelClasses;
    int lay;
    StudentModel studentModel;

    public ModelListAdapter(ArrayList<ModelClass> modelClasses, int lay, Context context, StudentModel studentModel) {
        this.modelClasses = modelClasses;
        this.lay = lay;
        this.studentModel = studentModel;
        this.context = context;
    }

    public ModelListAdapter(ArrayList<ModelClass> modelClasses, int lay, Context context) {
        this.modelClasses = modelClasses;
        this.lay = lay;
        this.context = context;
    }

    Context context;





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(lay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (modelClasses.get(position).getReff()!=null)
        {
            holder.tvName.setText(modelClasses.get(position).getName());
            holder.rldone.setVisibility(View.GONE);

            if (context instanceof AssignmentsActivity)
            {
                holder.initial.setBackground(context.getDrawable(R.drawable.assinment));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AssignmentsActivity)context).loadPdf(modelClasses.get(position));
                    }
                });
                if (holder.rldone!=null)
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getAssignments().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            holder.rldone.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            else if (context instanceof QuizActivity)
            {
                holder.initial.setBackground(context.getDrawable(R.drawable.quiz));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((QuizActivity)context).startQuiz(modelClasses.get(position));
                    }
                });
                if (holder.rldone!=null)
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getQuiz().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            holder.rldone.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
            else if (context instanceof ExamActivity)
            {
                holder.initial.setBackground(context.getDrawable(R.drawable.quiz));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ExamActivity)context).openExam(modelClasses.get(position));
                    }
                });
            }
            else if (context instanceof HomeworkActivity)
            {

                String parent =modelClasses.get(position).getReff().getParent().getId();
                if (parent.equalsIgnoreCase("TOPICS"))
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getTopics().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            Toast.makeText(context, "pos1", Toast.LENGTH_SHORT).show();
                            holder.rldone.setVisibility(View.VISIBLE);
                        }

                    }
                }
                else if (parent.equalsIgnoreCase("ASSIGNMENTS"))
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getAssignments().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            holder.rldone.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else if (parent.equalsIgnoreCase("QUIZ"))
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getQuiz().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            holder.rldone.setVisibility(View.VISIBLE);
                        }
                    }
                }
                holder.initial.setText(String.valueOf(modelClasses.get(position).getName().charAt(0)));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((HomeworkActivity)context).clicked(modelClasses.get(position));
                    }
                });

            }
            else if (context instanceof SubjectActivity)
            {
                holder.initial.setText(String.valueOf(modelClasses.get(position).getName().charAt(0)));
                if (modelClasses.get(position).getReff().getParent().getId().equalsIgnoreCase("TOPICS"))
                {
                    if (studentModel!=null)
                    {
                        if (studentModel.getTopics().containsKey(modelClasses.get(position).getReff().getId()))
                        {
                            holder.rldone.setVisibility(View.VISIBLE);
                        }

                    }

                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (modelClasses.get(position).getReff().getParent().getId().equalsIgnoreCase("TOPICS"))
                        {


                            ((SubjectActivity)context).loadVid(modelClasses.get(position));

                        }
                        else
                        {
                            ((SubjectActivity)context).nextfrag(modelClasses.get(position));
                        }
                    }
                });
            }
            else if (context instanceof ProfileActivity)
            {
                holder.initial.setText(String.valueOf(modelClasses.get(position).getName().charAt(0)));
                holder.rldone.setVisibility(View.VISIBLE);
            }

        }
        else
        {
//            modelClasses.remove(position);
//            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,initial;
        RelativeLayout rldone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rldone=itemView.findViewById(R.id.rldone);
            tvName = itemView.findViewById(R.id.modelName);
            initial = itemView.findViewById(R.id.tvInitial);

        }

    }


}



