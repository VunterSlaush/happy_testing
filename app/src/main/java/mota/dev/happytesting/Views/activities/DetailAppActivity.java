package mota.dev.happytesting.Views.activities;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Observable;
import java.util.Observer;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.detail.DetailAppViewModel;
import mota.dev.happytesting.Views.adapters.ReportAdapter;
import mota.dev.happytesting.databinding.ActivityDetailAppBinding;

public class DetailAppActivity extends BindeableActivity {
    private DetailAppViewModel viewModel;
    private ActivityDetailAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setupObserver(viewModel);
        setupAdapters();
        viewModel.setApp(getIntent().getIntExtra("app_id",-2));
    }

    private void setupAdapters()
    {
        ReportAdapter reportAdapter = new ReportAdapter();
        reportAdapter.setHasStableIds(true);
        binding.reportsList.setAdapter(reportAdapter);
        binding.reportsList.setLayoutManager(new LinearLayoutManager(this));
        binding.editorsList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1));
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshReports();
            }
        });
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof DetailAppViewModel)
        {
            binding.swipeContainer.setRefreshing(false);
            ReportAdapter adapter = (ReportAdapter) binding.reportsList.getAdapter();
            DetailAppViewModel viewModel = (DetailAppViewModel) observable;
            binding.reportsList.getItemAnimator().endAnimations();
            ArrayAdapter<String> adapt = (ArrayAdapter<String>) binding.editorsList.getAdapter();
            adapt.clear();
            adapt.addAll(viewModel.getEditors());
            adapt.notifyDataSetChanged();
            adapter.setList(viewModel.getReports());
        }
    }

    @Override
    public void initDataBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_app);
        viewModel = new DetailAppViewModel(this);
        binding.setViewModel(viewModel);
    }
}
