package mota.dev.happytesting.ViewModel.items;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.view.View;

import java.util.Observable;

import butterknife.ButterKnife;
import butterknife.OnLongClick;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.activities.FullImageActivity;
import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ItemImageViewModel extends Observable
{
    private Context context;
    private Image image;
    public  ObservableBoolean checked;
    public ItemImageViewModel(Image image, Context context)
    {
        this.image = image;
        this.context = context;
        checked = new ObservableBoolean();
    }

    public void setImage(Image image)
    {
        this.image = image;
        checked.set(false);
    }

    public void openImageActivity(View view)
    {
        Intent i = new Intent(context, FullImageActivity.class);
        i.putExtra("image",image.getDir());
        context.startActivity(i);
    }
}
