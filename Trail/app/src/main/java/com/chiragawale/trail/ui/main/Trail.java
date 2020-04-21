package com.chiragawale.trail.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chiragawale.trail.R;
import com.chiragawale.trail.dao.Dao;
import com.chiragawale.trail.dao.DaoImpl;
import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.worker.NotificationWorker;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Trail extends Fragment {
    private OnListFragmentInteractionListener mListener;
    
    Button btnSend;
    TextView tv_total,tv_today;

    Dao dao = new DaoImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trail_layout, container, false);
        btnSend = view.findViewById(R.id.btnStart);
        tv_total = view.findViewById(R.id.tv_total);
        tv_today = view.findViewById(R.id.tv_today);
        tv_total.setText(dao.getEntryList(getContext()).size() + "");
        tv_today.setText(dao.getEntryListToday(getContext()).size() + "");
        Button btnStop = view.findViewById(R.id.btnStop);
        ImageView iv_loading = view.findViewById(R.id.iv_loading);
        YoYo.with(Techniques.FadeIn)
                .repeat(2)
                .duration(800)
                .playOn(tv_today);
        YoYo.with(Techniques.FadeIn)
                .repeat(2)
                .duration(800)
                .playOn(tv_total);
        YoYo.with(Techniques.FadeOut)
                .repeat(Animation.INFINITE)
                .duration(1000)
                .playOn(iv_loading);

        btnSend.setVisibility(View.VISIBLE);

        PeriodicWorkRequest mRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class,15, TimeUnit.MINUTES)
                .addTag("RangeAndTransmit")
                .build();
        final WorkManager mWorkManager = WorkManager.getInstance(getContext());
        btnSend.setOnClickListener(v ->{
            mWorkManager.enqueueUniquePeriodicWork("RangeAndTransmit", ExistingPeriodicWorkPolicy.KEEP,mRequest);
            btnSend.setVisibility(View.GONE);
            btnSend.setAlpha(0.4f);
            iv_loading.setVisibility(View.VISIBLE);
        });

        mWorkManager.getWorkInfosByTagLiveData("RangeAndTransmit").observe(this, workInfos -> {
            if (workInfos != null) {

                for(WorkInfo workInfo : workInfos) {
                    WorkInfo.State state = workInfo.getState();
                    Log.e("WORKER1", state.toString());
                    if (state.toString().equalsIgnoreCase("ENQUEUED") || state.toString().equalsIgnoreCase("RUNNING")) {
                        btnSend.setVisibility(View.GONE);
                        iv_loading.setVisibility(View.VISIBLE);
                    }
                    if (state.toString().equalsIgnoreCase("SUCCEEDED")) {
                        btnSend.setEnabled(true);
                        btnSend.setAlpha(1f);
                        iv_loading.setVisibility(View.GONE);
                        tv_total.setText(dao.getEntryList(getContext()).size() + "");
                        tv_today.setText(dao.getEntryListToday(getContext()).size() + "");
                    }
                    Toast.makeText(getContext(), state.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop.setOnClickListener(v -> {
            Log.e("Trail","Cancelling All work");

            mWorkManager.cancelAllWorkByTag("RangeAndTransmit");
            mWorkManager.cancelAllWorkByTag("RangeAndTransmit");
            Log.e("Trail","Cancelled All work");
            btnSend.setVisibility(View.VISIBLE);
            btnSend.setEnabled(true);
            iv_loading.setVisibility(View.GONE);
        });

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(RealmEntry realmEntry);
    }
}
