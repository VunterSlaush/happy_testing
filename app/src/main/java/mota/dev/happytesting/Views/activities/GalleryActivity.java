package mota.dev.happytesting.Views.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.adapters.ImageAdapter;

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
        Log.d(getClass().getName(),"Column Count:"+cursor.getColumnCount());
        count = cursor.getCount();
        strUrls = new String[count];
        mNames = new String[count];

        for (int i = 0; i < count; i++)
        {
            cursor.moveToPosition(i);
            strUrls[i] = cursor.getString(1);
            mNames[i] = cursor.getString(3);
        }
    }

    private Cursor generateGalleryCursor()
    {
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy);
        if(cursor == null)
            Log.d(getClass().getName(),"CURSOR NULO!");
        return cursor;
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
