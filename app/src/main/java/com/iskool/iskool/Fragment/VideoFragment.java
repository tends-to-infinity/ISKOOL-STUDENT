package com.iskool.iskool.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.iskool.iskool.Activity.SubjectActivity;
import com.iskool.iskool.Adapter.TopicSelectorAdapter;
import com.iskool.iskool.Models.ModelClass;
import com.iskool.iskool.Models.StudentModel;
import com.iskool.iskool.Models.TopicModel;
import com.iskool.iskool.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {




    private String topicPath;
    private PlayerView playerView;
    private Button btnNext;
    private TopicModel topicModel;

    public VideoFragment(TopicModel topicModel) {
        this.topicModel = topicModel;
    }

    private SimpleExoPlayer player;
    private Spinner spinnerSpeed;
    private ArrayList<ModelClass> list = new ArrayList<>();
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    ImageView fullscreenButton;
    boolean fullscreen = false;
    StudentModel student;
    private PlaybackStateListener playbackStateListener;


    private static final String TAG = SubjectActivity.class.getName();
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






    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startProgress(view.getContext());
        playbackStateListener = new PlaybackStateListener();



        spinnerSpeed=view.findViewById(R.id.spinnerVidioSpeed);
        list.add(new ModelClass("1.00X",null));
        list.add(new ModelClass("1.25X",null));
        list.add(new ModelClass("1.50X",null));
        list.add(new ModelClass("1.75X",null));
        list.add(new ModelClass("2.00X",null));

        playerView=view.findViewById(R.id.plaayerr);
        btnNext=view.findViewById(R.id.btnTopicNext);
        btnNext.setVisibility(View.GONE);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getContext());
        playerView.setPlayer(player);
        TopicSelectorAdapter topicSelectorAdapter = new TopicSelectorAdapter(getActivity(),list);
        spinnerSpeed.setAdapter(topicSelectorAdapter);


        if (topicModel!=null&&topicModel.getVpath()!=null)
        {
            FirebaseFirestore.getInstance().collection("STUDENTS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    student = documentSnapshot.toObject(StudentModel.class);
                }
            });
            FirebaseStorage.getInstance().getReferenceFromUrl(topicModel.getVpath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    MediaSource mediaSource = buildMediaSource(uri);
                    player.setPlayWhenReady(playWhenReady);
                    player.seekTo(currentWindow, playbackPosition);
                    player.prepare(mediaSource, false, false);
                    player.addListener(playbackStateListener);
                    player.prepare(mediaSource, false, false);
                    fullscreen = false;
                    fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
                    fullscreenButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(fullscreen) {
                                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.exo_controls_fullscreen_enter));


                                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                                params.width = params.MATCH_PARENT;
                                params.height = (int) ( 200 * getContext().getResources().getDisplayMetrics().density);
                                playerView.setLayoutParams(params);
                                fullscreen = false;
                            }else{
                                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.exo_controls_fullscreen_exit));
                                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                                params.width = params.MATCH_PARENT;
                                params.height = params.MATCH_PARENT;
                                playerView.setLayoutParams(params);
                                fullscreen = true;
                            }
                        }
                    });
                    spinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            float f= (float) (1.0+(i*1.0/4.0));
                            PlaybackParameters param = new PlaybackParameters(f);
                            player.setPlaybackParameters(param);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            PlaybackParameters param = new PlaybackParameters(1f);
                            player.setPlaybackParameters(param);
                        }
                    });


                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "No Content Added", Toast.LENGTH_SHORT).show();
        }
    }




    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }


    private class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    startProgress(playerView.getContext());
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    stopProgress();



                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (student.getTopics()==null)
                            {
                                student.setTopics(new HashMap<String, ModelClass>());
                            }
                            student.getTopics().put(topicModel.getSelf().getReff().getId(),topicModel.getSelf());
                            student.getSelf().getReff().set(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (getActivity() instanceof SubjectActivity)
                                    {
                                        ((SubjectActivity)getActivity()).vidFin();
                                    }
                                }
                            });

                        }
                    });
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }






}
