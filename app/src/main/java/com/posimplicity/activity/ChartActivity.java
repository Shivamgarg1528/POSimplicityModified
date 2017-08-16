package com.posimplicity.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.posimplicity.model.local.ReportParent;
import com.posimplicity.R;
import com.utils.Constants;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class ChartActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupToolBar("Chart View", false);

        ReportParent reportParent = (ReportParent) getIntent().getSerializableExtra(Constants.EXTRA_KEY);

        float total = reportParent.getTotalAmount();
        if (total <= 0) {
            return;
        }
        float[] amountArray = new float[]{
                (reportParent.getCashAmount() / (total * .01f)),
                (reportParent.getCheckAmount() / (total * .01f)),
                (reportParent.getCreditAmount() / (total * .01f)),
                (reportParent.getRewardAmount() / (total * .01f)),
                (reportParent.getGiftAmount() / (total * .01f)),
                (reportParent.getCustom1Amount() / (total * .01f)),
                (reportParent.getCustom2Amount() / (total * .01f)),
        };

        String[] tenderArray = new String[]{
                "Cash",
                "Check",
                "Credit",
                "Reward",
                "Gift",
                "Custom1",
                "Custom2"
        };

        int[] colorArray = new int[]{
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_light,
                android.R.color.white,
        };

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setLabelsTextSize(20);

        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");
        for (int i = 0; i < tenderArray.length; i++) {
            distributionSeries.add(tenderArray[i], amountArray[i]);

            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colorArray[i]);
            seriesRenderer.setDisplayChartValues(true);
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        GraphicalView mGraphicalView = ChartFactory.getPieChartView(this, distributionSeries, defaultRenderer);
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.activity_chart_ll);
        chartContainer.addView(mGraphicalView);
    }

    @Override
    protected void onBackTapped() {
        finish();
    }
}
