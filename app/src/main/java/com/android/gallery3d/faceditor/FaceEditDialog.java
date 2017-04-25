/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gallery3d.faceditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.gallery3d.R;
import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.filtershow.FilterShowActivity;
import com.android.gallery3d.filtershow.imageshow.MasterImage;
import com.android.gallery3d.filtershow.pipeline.ImagePreset;
import com.android.gallery3d.filtershow.pipeline.ProcessingService;
import com.android.gallery3d.filtershow.tools.SaveImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FaceEditDialog extends DialogFragment implements View.OnClickListener{
    private final static String TAG = "FaceEditDialog";
    TextView mFaceName;
    EditText mFaceNameEditor;
    TextView mFaceNameID;
    FaceInfo.Info mInfo;

    public FaceEditDialog(FaceInfo.Info info) {
        mInfo = info;
    }
    private class Watcher implements TextWatcher {
        private EditText mEditText;
        Watcher(EditText text) {
            mEditText = text;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            textChanged(mEditText);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView ");
        View view = inflater.inflate(R.layout.face_edit_dialog, container);
        mFaceName = (TextView) view.findViewById(R.id.face_name_dialog);
        mFaceNameEditor = (EditText) view.findViewById(R.id.face_name_dialog_editor);
        mFaceNameEditor.addTextChangedListener(new Watcher(mFaceNameEditor));
        mFaceNameID = (TextView)view.findViewById(R.id.face_dialog_id);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.done).setOnClickListener(this);
        getDialog().setTitle("Face Editor");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.done:
                dismiss();
                break;
        }
    }

    private void textChanged(EditText text) {
        mInfo.faceName =  String.valueOf(text.getText());
        mInfo.faceID = Integer.toString(mInfo.faceName.hashCode());
        Log.d(TAG, "texChanged: "+ mInfo.faceName +" | "+mInfo.faceID);
        mFaceNameID.setText(mInfo.faceID);
        if (mInfo.faceName != null) {
            mInfo.mTagByManual = true;
        }
    }

}
