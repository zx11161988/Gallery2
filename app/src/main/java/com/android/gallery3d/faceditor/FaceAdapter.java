package com.android.gallery3d.faceditor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.gallery3d.R;
import com.android.gallery3d.app.GalleryAppImpl;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.face.FaceManager;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by jingxiang wu on 2017/4/19.
 */

public class FaceAdapter extends BaseAdapter {
    private final static String TAG = "FaceAdapter";
    private Context mContext;
    private GalleryAppImpl mGalleryAppImpl;
    private FaceManager mFaceManager;
    private ArrayList<FaceInfo.Info> mList = new ArrayList<FaceInfo.Info>();
    private LayoutInflater mInflater;
    private ClickCallback mClickListener;
    public interface ClickCallback{
        public void onClick(FaceInfo.Info info);
    }

    public final class ViewHolder {
        public RelativeLayout layout;
        public ImageView faceView;
        public ImageView stateView;
        public TextView faceIDName;
        public Button imageButton;
    }
    public FaceAdapter(Activity context, ClickCallback listener) {
        mGalleryAppImpl = (GalleryAppImpl)context.getApplication();
        mFaceManager = mGalleryAppImpl.getFaceManager();
        mList = mFaceManager.getFaceList();
        mInflater = LayoutInflater.from(context);
        mClickListener = listener;
    }
    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public ArrayList<FaceInfo.Info> getFaceInfoList() {
        return mList;
    }
    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.face, null);
            holder.faceView = (ImageView)convertView.findViewById(R.id.face);
            holder.stateView = (ImageView)convertView.findViewById(R.id.state);
            holder.faceIDName = (TextView)convertView.findViewById(R.id.face_id_name);
            holder.imageButton = (Button) convertView.findViewById(R.id.edit_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        initView(holder, position);
        return convertView;
    }

    private void initView(ViewHolder holder, int position) {
        final FaceInfo.Info info = mList.get(position);
        holder.faceView.setImageBitmap(getBitmap(info.getFaceThumbNailPath));
        if (info.mTagByManual) {
            holder.stateView.setImageResource(R.drawable.btn_shutter_pressed);
        } else {
            holder.stateView.setImageResource(R.drawable.photoeditor_effect_redeye);
        }
        holder.faceIDName.setText("ID:"+ info.faceID+" "+" \nName:"+info.faceName);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(info);
                }
            }
        });
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
