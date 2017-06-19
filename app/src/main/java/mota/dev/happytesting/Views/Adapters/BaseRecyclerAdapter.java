package mota.dev.happytesting.Views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;

/**
 * Created by Slaush on 18/06/2017.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> list;

    public BaseRecyclerAdapter()
    {
        list = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setList(List<T> items)
    {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }


}
