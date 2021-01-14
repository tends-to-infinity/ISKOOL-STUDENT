package com.iskool.iskool.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.iskool.iskool.Activity.AssignmentsActivity;
import com.iskool.iskool.Activity.ExamActivity;
import com.iskool.iskool.Activity.HomeworkActivity;
import com.iskool.iskool.Models.AssignmentModel;
import com.iskool.iskool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PdfViewFragmen extends Fragment {
    Button btnassUpload;
    PDFView pdfView;

    Uri uri;
    private ProgressDialog progressDoalog;
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

    public PdfViewFragmen(Uri uri) {
        this.uri = uri;
    }

    public PdfViewFragmen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnassUpload = view.findViewById(R.id.btnassUpload);
        pdfView = view.findViewById(R.id.pdfView);
        startProgress(getActivity());
        pdfView.fromUri(uri).onLoad(new OnLoadCompleteListener() {
            @Override
            public void loadComplete(int nbPages) {
                stopProgress();


                btnassUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() instanceof AssignmentsActivity)
                        {
                            ((AssignmentsActivity)getActivity()).upAns();
                        }
                        if (getActivity() instanceof ExamActivity)
                        {
                            ((ExamActivity)getActivity()).pdfBut();
                        }
                        if (getActivity() instanceof HomeworkActivity)
                        {
                            ((HomeworkActivity)getActivity()).upAns();
                        }

                    }
                });
            }
        }).load();
    }
}
