package mota.dev.happytesting.Views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import mota.dev.happytesting.R;

/**
 * Created by user on 15/05/2017.
 */

public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context context;
    private String [] urls;
    private boolean [] imagesSelected;

    public ImageAdapter(Context context,String [] urls)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.urls = urls;
        imagesSelected = new boolean[urls.length];
    }

    public int getCount() {
        return urls.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        final ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.image_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.check.setId(position);
        holder.img.setId(position);

        putOnClickListeners(holder);

        try {
            Glide.with(context).load(new File(urls[position])).placeholder(android.R.drawable.ic_menu_gallery).into(holder.img);
        } catch (Throwable e) {
            Log.d("GALLERY", "ERROR LOADING!" + e);
        }
        holder.check.setChecked(imagesSelected[position]);
        holder.id = position;
        return view;
    }

    private void putOnClickListeners(final ViewHolder holder)
    {
        holder.check.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int id = cb.getId();
                if (imagesSelected[id]) {
                    cb.setChecked(false);
                    imagesSelected[id] = false;
                } else {
                    cb.setChecked(true);
                    imagesSelected[id] = true;
                }
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                int id = holder.check.getId();
                if (imagesSelected[id]) {
                    holder.check.setChecked(false);
                    imagesSelected[id] = false;
                } else {
                    holder.check.setChecked(true);
                    imagesSelected[id] = true;
                }
            }
        });
    }

    public boolean [] getImagesSelected()
    {
        return imagesSelected;
    }
}

class ViewHolder {
    @BindView(R.id.image_item)
    ImageView img;
    @BindView(R.id.image_check)
    CheckBox check;
    int id;

    ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}