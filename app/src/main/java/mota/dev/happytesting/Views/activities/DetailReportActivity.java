package mota.dev.happytesting.Views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.detail.DetailReportViewModel;
import mota.dev.happytesting.Views.adapters.ObservationAdapter;
import mota.dev.happytesting.databinding.ActivityDetailReportBinding;

public class DetailReportActivity extends BindeableActivity {

    DetailReportViewModel viewModel;
    private ActivityDetailReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        initDataBinding();
        setupAdapters();
        setupObserver(viewModel);
        viewModel.setReportData(getIntent().getIntExtra("report_id",-1),
                                 getIntent().getStringExtra("report_name"));
    }

    private void setupAdapters()
    {
        ObservationAdapter adapter = new ObservationAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.observationList.setAdapter(adapter);
        binding.observationList.setLayoutManager(lm);
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof DetailReportViewModel)
        {
            ObservationAdapter adapter = (ObservationAdapter)binding.observationList.getAdapter();
            DetailReportViewModel viewModel = (DetailReportViewModel)observable;
            adapter.setList(viewModel.getObservations());
           // binding.observationList.getItemAnimator().endAnimations();
        }
    }

    @Override
    public void initDataBinding()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_report);
        viewModel = new DetailReportViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
            viewModel.onActivityResult(data.getExtras());
    }
}
