package mota.dev.happytesting.Views.adapters.viewholders;

import android.support.v7.widget.RecyclerView;

import mota.dev.happytesting.ViewModel.items.ItemReportViewModel;
import mota.dev.happytesting.databinding.ReportItemBinding;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ReportViewHolder extends BaseViewHolder<Report>
{
    ReportItemBinding binding;

    public ReportViewHolder(ReportItemBinding binding)
    {
        super(binding.itemApp);
        this.binding = binding;
    }

    @Override
    public void onBind()
    {
        if(binding.getViewModel() == null)
            binding.setViewModel(new ItemReportViewModel(item, itemView.getContext()));
        else
            binding.getViewModel().setReport(item);
    }
}