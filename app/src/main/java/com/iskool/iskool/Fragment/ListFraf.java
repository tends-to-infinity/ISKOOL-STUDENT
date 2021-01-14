package com.iskool.iskool.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iskool.iskool.Activity.SubjectActivity;
import com.iskool.iskool.Adapter.ModelListAdapter;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ListFraf extends Fragment {

    ArrayList<ModelClass> list;
    RecyclerView rvModelList;
    StudentModel studentModel;

    public ListFraf(ArrayList<ModelClass> list, StudentModel studentModel) {
        this.list = list;
        this.studentModel = studentModel;
    }

    public ListFraf(ArrayList<ModelClass> list) {
        this.list = list;
    }

    public ListFraf() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_fraf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvModelList = view.findViewById(R.id.rvListModel);

        if (list!=null)
        {
            ModelListAdapter modelListAdapter = new ModelListAdapter(list,R.layout.subject_chapters_view_rv,getActivity(),studentModel);

            if (getActivity()  instanceof SubjectActivity)
            {
                GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                rvModelList.setAdapter(modelListAdapter);
                rvModelList.setLayoutManager(manager);
                rvModelList.setHasFixedSize(true);

            }
            else
            {
                RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                rvModelList.setAdapter(modelListAdapter);
                rvModelList.setLayoutManager(layoutManager);
                rvModelList.setHasFixedSize(true);

            }

        }


    }
}


