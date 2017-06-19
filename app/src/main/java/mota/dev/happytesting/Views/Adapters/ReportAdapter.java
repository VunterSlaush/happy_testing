package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;
import mota.dev.happytesting.Views.adapters.viewholders.ReportViewHolder;
import mota.dev.happytesting.databinding.ReportItemBinding;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 30/05/2017.
 */

public class ReportAdapter extends BaseRecyclerAdapter<Report> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ReportItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.report_item,
                parent, false);
        return new ReportViewHolder(binding);
    }
}
