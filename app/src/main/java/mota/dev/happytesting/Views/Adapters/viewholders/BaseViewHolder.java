package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Slaush on 18/06/2017.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder
{
    T item;
    public BaseViewHolder(View itemView)
    {
        super(itemView);
    }

    public void bind(T t)
    {
        this.item = t;
        onBind();
    }

    public abstract void onBind();
}
