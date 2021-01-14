package com.iskool.iskool.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iskool.iskool.Activity.AssignmentsActivity;
import com.iskool.iskool.Activity.ExamActivity;
import com.iskool.iskool.Activity.HomeworkActivity;
import com.iskool.iskool.Adapter.AddImageRVAdapter;
import com.iskool.iskool.Models.AssignmentModel;
import com.iskool.iskool.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import id.zelory.compressor.Compressor;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadAnsFrag extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Bitmap> imageUris = new ArrayList<>();


    AddImageRVAdapter addImageRVAdapter;
    int index = 0;
    Button btnsubmit;
    Uri tempURI;
    Uri photoURI;
    File photoFile;
//    Exam examModel;
//    Student student;



    public UploadAnsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        return inflater.inflate(R.layout.fragment_upload_ans, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvAddImage);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        addImageRVAdapter = new AddImageRVAdapter(imageUris);
        recyclerView.setAdapter(addImageRVAdapter);
        btnsubmit = view.findViewById(R.id.btnAddAssSubmit);
        LinearLayout llAddImage = view.findViewById(R.id.llAddImage);
        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photoFile = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                        "com.iskool.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);


            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (imageUris.size() == 0) {
                        Toast.makeText(getActivity(), "Add Image", Toast.LENGTH_SHORT).show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        builder.setTitle("Are You Sure");
                        builder.setMessage("Are you Sure you want to upload");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                PdfDocument pdfDocument = new PdfDocument();
                                for (int u = 0; u < imageUris.size(); u++) {
                                    Bitmap bitmap = imageUris.get(u);


                                    PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                                    PdfDocument.Page page = pdfDocument.startPage(myPageInfo);


                                    page.getCanvas().drawBitmap(bitmap, 0, 0, null);
                                    pdfDocument.finishPage(page);
                                }
                                try (FileOutputStream fos = getActivity().openFileOutput("name" + ".pdf", Context.MODE_PRIVATE)) {

                                    pdfDocument.writeTo(fos);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                File ful = new File(getActivity().getFilesDir(), "name" + ".pdf");
                                if (getActivity() instanceof AssignmentsActivity)
                                {
                                    ((AssignmentsActivity) getActivity()).ansUp(ful);
                                }
                                else  if (getActivity() instanceof ExamActivity)
                                {
                                    ((ExamActivity) getActivity()).ansUp(ful);
                                }
                                else if (getActivity() instanceof HomeworkActivity)
                                {
                                    ((HomeworkActivity)getActivity()).ansUp(ful);
                                }
                                pdfDocument.close();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();

                    }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("detail", "" + requestCode + resultCode + data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            Log.e("Size", ((photoFile.length()) / 1024) + "");
            try {
                File compressedImageFile = new Compressor(Objects.requireNonNull(getActivity())).compressToFile(photoFile);
                Log.e("Size", ((compressedImageFile.length()) / 1024) + "");
                Bitmap bitma = BitmapFactory.decodeFile(compressedImageFile.getPath());

                imageUris.add(index, bitma);
                addImageRVAdapter.notifyItemChanged(index);
                index++;

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents


        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
