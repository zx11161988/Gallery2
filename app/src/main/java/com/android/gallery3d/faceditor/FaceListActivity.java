package com.android.gallery3d.faceditor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.classification.Svm;
import com.android.gallery3d.R;
import com.android.gallery3d.app.GalleryAppImpl;
import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.face.FaceManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by jingxiang wu on 2017/4/17.
 */

public class FaceListActivity extends FragmentActivity implements FaceAdapter.ClickCallback {
    private final static String TAG = "FaceListActivity";
    ListView mListView = null;
    FaceAdapter mAdapter;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    Context mContext;
    GalleryAppImpl mGalleryAppImpl;
    FaceManager mFaceManager;
    FaceInfo mFaceInfo;
    private WeakReference<ProgressDialog> mSavingProgressDialog;
    Svm mClassification;
    Info mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.face_main);
        mContext = this;
        mListView = (ListView) findViewById(R.id.face_list);
        mAdapter = new FaceAdapter(this, this);
        mListView.setAdapter(mAdapter);
        Uri uri = this.getIntent().getData();
        String filePath = uri.toString();
        Log.d(TAG, "filePath = " + filePath);
        super.onCreate(savedInstanceState);
        mClassification = new Svm();
        mInfo = new Info();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.face_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.m_fae_save) {
            String progressText = "Training ......";
            ProgressDialog progress = ProgressDialog.show(this, "", progressText, true, false);
            mSavingProgressDialog = new WeakReference<ProgressDialog>(progress);
            ClassficationTask task = new ClassficationTask(this);
            task.execute();
        }
        return true;
    }

    @Override
    public void onClick(FaceInfo.Info info) {
        Log.d(TAG, "onClick " + info);
        DialogFragment dialog = new FaceEditDialog(info);
        dialog.show(getSupportFragmentManager(), "ExportDialogFragment");
    }

    public String[] getTrainList() {
        ArrayList<FaceInfo.Info> infoList = mAdapter.getFaceInfoList();
        int length = infoList.size();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            if (infoList.get(i).mTagByManual) {
                String facePath = infoList.get(i).getFaceThumbNailPath;
                list.add(facePath);
                Log.d(TAG, "<getTrainList> list["+i+"] = " + facePath);
            }
        }
        length = list.size();
        String[] trainList = new String[length];
        for (int i = 0; i < length; i++) {
            trainList[i] = list.get(i);
        }
        return trainList;
    }

    public int[] getTrainLab() {
        ArrayList<FaceInfo.Info> infoList = mAdapter.getFaceInfoList();
        int length = infoList.size();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            if (infoList.get(i).mTagByManual) {
                String facePath = infoList.get(i).faceID;
                list.add(facePath);
                Log.d(TAG, "<getTrainLab> list["+i+"] = " + facePath);
            }
        }
        length = list.size();
        int[] labs = new int[length];
        for (int i = 0; i < length; i++) {
            labs[i] = Integer.parseInt(list.get(i));
            Log.d(TAG, "<getTrainLab> labs["+i+"] = " + labs[i]);
        }
        return labs;
    }

    public String[] getPredictList() {
        ArrayList<FaceInfo.Info> infoList = mAdapter.getFaceInfoList();
        int length = infoList.size();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            if (!infoList.get(i).mTagByManual) {
                String facePath = infoList.get(i).getFaceThumbNailPath;
                list.add(facePath);
                Log.d(TAG, "<getPredictList> list["+i+"] = " + facePath);
            }
        }
        length = list.size();
        String[] trainList = new String[length];
        for (int i = 0; i < length; i++) {
            trainList[i] = list.get(i);
        }
        return trainList;
    }

    public void hideSavingProgress() {
        if (mSavingProgressDialog != null) {
            ProgressDialog progress = mSavingProgressDialog.get();
            if (progress != null)
                progress.dismiss();
        }
    }

    public class ClassficationTask extends AsyncTask<URL, Integer, Long> {
        private  FaceListActivity mActivity;
        public ClassficationTask(FaceListActivity activity) {
            mActivity = activity;
        }
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Long doInBackground(URL... params) {
            mActivity.getTrainList();
            mActivity.getTrainLab();
            mActivity.getPredictList();
            int[] predictLabs = mClassification.onClassfication(
                    mActivity.getTrainList(),
                    mActivity.getTrainLab(),
                    mActivity.getPredictList()
                    );
            for (int i = 0; i < predictLabs.length; i++) {
                Log.d(TAG, "<doInBackground> predictLabs["+i+"] = "+ predictLabs[i]);
            }
            Log.d(TAG, "doInBackground.....................");
            return null;
        }

        protected void onPostExecute(Long result) {
            Log.d(TAG, "onPostExecute.....................");
            mActivity.hideSavingProgress();
        }
    }
}
