package mota.dev.happytesting.Views.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.items.ItemReportViewModel;
import mota.dev.happytesting.databinding.ReportItemBinding;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 30/05/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;

    public ReportAdapter()
    {
        reportList = new ArrayList<>();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ReportItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.report_item,
                parent, false);
        return new ReportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position)
    {
        Log.d("APPS","BINDEANDO:"+reportList.get(position));
        holder.bindReport(reportList.get(position));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void setReportList(List<Report> reports)
    {
        reportList.clear();
        reportList.addAll(reports);
        notifyDataSetChanged();
    }


    public class ReportViewHolder extends RecyclerView.ViewHolder
    {
        ReportItemBinding binding;

        private ReportViewHolder(ReportItemBinding binding)
        {
            super(binding.itemApp);
            this.binding = binding;
        }

        private void bindReport(Report report)
        {
            if(binding.getViewModel() == null)
                binding.setViewModel(new ItemReportViewModel(report, itemView.getContext()));
            else
                binding.getViewModel().setReport(report);
        }
    }
}
