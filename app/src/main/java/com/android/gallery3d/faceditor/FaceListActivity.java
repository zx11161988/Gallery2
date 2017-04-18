package com.android.gallery3d.faceditor;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.gallery3d.R;
import com.android.gallery3d.app.GalleryAppImpl;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.face.FaceManager;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jingxiang wu on 2017/4/17.
 */

public class FaceListActivity extends ListActivity {
    private final static String TAG = "FaceListActivity";
    ListView mListView = null;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    Context mContext;
    GalleryAppImpl mGalleryAppImpl;
    FaceManager mFaceManager;
    FaceInfo mFaceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mListView = getListView();
        mContext = this;
        Uri uri = this.getIntent().getData();
        String filePath = uri.toString();
        Log.d(TAG, "filePath = "+filePath);
        mGalleryAppImpl = (GalleryAppImpl)this.getApplication();
        mFaceManager = mGalleryAppImpl.getFaceManager();
        mFaceInfo = mFaceManager.getFaceInfo(filePath);

        int lengh = mFaceInfo.faceLists.size();
        for (int i = 0; i < lengh; i++) {
            FaceInfo.Info info = mFaceInfo.faceLists.get(i);
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image",getBitmap(info.getFaceThumbNailPath));
            item.put("face_name", "FaceName:");
            item.put("face_name_value", info.faceName);
            item.put("face_id", "FaceID:");
            item.put("face_id_value", info.faceID);

            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.facelist,
                new String[]{"image", "face_name", "face_name_value", "face_id", "face_id_value"},
                new int[]{R.id.image, R.id.face_name, R.id.face_name_value, R.id.face_id, R.id.face_id_value});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            /**
             * Binds the specified data to the specified view.
             * <p>
             * When binding is handled by this ViewBinder, this method must return true.
             * If this method returns false, SimpleAdapter will attempts to handle
             * the binding on its own.
             *
             * @param view               the view to bind the data to
             * @param data               the data to bind to the view
             * @param textRepresentation a safe String representation of the supplied data:
             *                           it is either the result of data.toString() or an empty String but it
             *                           is never null
             * @return true if the data was bound to the view, false otherwise
             */
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                Log.d(TAG, "view = "+view +" data = "+data);
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView imageView = (ImageView)view;
                    imageView.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        setListAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "您选择了FaceName：" + mData.get(position).get("face_name_value") + "FaceID："
                        + mData.get(position).get("face_id_value"), Toast.LENGTH_LONG).show();

            }
        });
        super.onCreate(savedInstanceState);
    }

    private Bitmap getBitmap(String filePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            FileDescriptor fd = fis.getFD();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, option);
            Log.d(TAG, "getBitmap filePath = " + filePath +" bitmap = "+bitmap);
            return bitmap;
        } catch (Exception ex) {
            Log.d(TAG, "getBitmap "+ex);
        } finally {
            Utils.closeSilently(fis);
        }
        return null;
    }
}
