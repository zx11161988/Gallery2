package com.android.gallery3d.faceditor;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.gallery3d.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jingxiang wu on 2017/4/17.
 */

public class FaceListActivity extends ListActivity {
    private final static String TAG = "FaceListActivity";
    private String[] mListTitle = {"姓名", "FaceID"};
    private String[] mListStr = {"雨松MOMO", "男", "25", "北京",
            "xuanyusong@gmail.com"};
    ListView mListView = null;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mListView = getListView();
        mContext = this;
        Uri uri = this.getIntent().getData();
        String filePath = uri.toString();
        Log.d(TAG, "filePath = "+filePath);
        int lengh = mListTitle.length;
        for (int i = 0; i < lengh; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", R.drawable.filtershow_addpoint);
            item.put("title", mListTitle[i]);
            item.put("text", mListStr[i]);
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.facelist,
                new String[]{"image", "title", "text"}, new int[]{R.id.image, R.id.title, R.id.text});
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
                Toast.makeText(mContext, "您选择了标题：" + mListTitle[position] + "内容："
                        + mListStr[position], Toast.LENGTH_LONG).show();

            }
        });
        super.onCreate(savedInstanceState);
    }

    private Drawable getDrawable(String filePath) {
        //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Drawable drawable = Drawable.createFromPath(filePath);
        return drawable;
    }
}
