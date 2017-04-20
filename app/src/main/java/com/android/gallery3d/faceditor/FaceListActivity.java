package com.android.gallery3d.faceditor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.gallery3d.R;
import com.android.gallery3d.app.GalleryAppImpl;
import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.face.FaceManager;

import java.lang.ref.WeakReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.face_main);
        mContext = this;
        mListView = (ListView)findViewById(R.id.face_list);
        mAdapter = new FaceAdapter(this, this);
        mListView.setAdapter(mAdapter);
        Uri uri = this.getIntent().getData();
        String filePath = uri.toString();
        Log.d(TAG, "filePath = "+filePath);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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
            doTrain();
        }
        return true;
    }
    @Override
    public void onClick(FaceInfo.Info info) {
        Log.d(TAG, "onClick "+info);
        DialogFragment dialog = new FaceEditDialog(info);
        dialog.show(getSupportFragmentManager(), "ExportDialogFragment");
    }

    private void doTrain() {

    }
    private void hideSavingProgress() {
        if (mSavingProgressDialog != null) {
            ProgressDialog progress = mSavingProgressDialog.get();
            if (progress != null)
                progress.dismiss();
        }
    }
}
