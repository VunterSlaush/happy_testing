package mota.dev.happytesting.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.Adapters.ImageAdapter;

/**
 * Created by Slaush on 07/05/2017.
 */

public class GalleryActivity extends Activity
{

    @BindView(R.id.grid_photos)
    GridView grdImages;
    @BindView(R.id.select_images)
    Button btnSelect;

    private ImageAdapter imageAdapter;
    private Uri[] mUrls = null;
    private String[] strUrls = null;
    private String[] mNames = null;
    private int count;


    /**
     * Overrides methods
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        ButterKnife.bind(this);

        loadGalleryImages();
        imageAdapter = new ImageAdapter(GalleryActivity.this,strUrls);
        grdImages.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    private void loadGalleryImages()
    {
        Cursor cursor = generateGalleryCursor();
        cursor.moveToFirst();

        count = cursor.getCount();
        mUrls = new Uri[count];
        strUrls = new String[count];
        mNames = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            mUrls[i] = Uri.parse(cursor.getString(1));
            strUrls[i] = cursor.getString(1);
            mNames[i] = cursor.getString(3);
            //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
        }
    }

    private Cursor generateGalleryCursor()
    {
        final String orderBy = MediaStore.Images.Media._ID;
        return getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy);
    }

    @OnClick(R.id.select_images)
    public void select()
    {
        boolean [] imagesSelected = imageAdapter.getImagesSelected();
        final int len = imagesSelected.length;
        int cnt = 0;
        String selectImages = "";
        for (int i = 0; i < len; i++) {
            if (imagesSelected[i]) {
                cnt++;
                selectImages = selectImages + strUrls[i] + "|";
            }
        }
        if (cnt == 0)
        {
            Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
        }
        else
        {

            Log.d("SelectedImages", selectImages);
            Intent i = new Intent();
            i.putExtra("data", selectImages);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }



}
