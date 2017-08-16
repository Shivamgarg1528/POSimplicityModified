package com.posimplicity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;

import com.Beans.ReportsModel;
import com.posimplicity.database.local.ReportsTable;
import com.utils.MyStringFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ChartViewActivity extends BaseActivity {

    private GraphicalView mChart;
    private String[] code;
    private float percentageCash;
    private float percentageCC;
    private float percentageCheck;
    private ReportsModel reports;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, false, this);
        setContentView(R.layout.activity_chartview);

        String timeTrans = new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString().trim();
        reports = new ReportsTable(mContext).getReportModel(timeTrans, ReportsTable.DAILY_REPORT);

        float netAmount = Float.parseFloat(reports.getTotalWithTaxAmount());
        float netCash = Float.parseFloat(reports.getCashAmount());

        percentageCash = (netCash / netAmount) * 100;
        percentageCash = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(percentageCash));

        float netCc = Float.parseFloat(reports.getCreditAmount());
        percentageCC = (netCc / netAmount) * 100;
        percentageCC = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(percentageCC));

        float netChe = Float.parseFloat(reports.getCreditAmount());
        percentageCheck = (netChe / netAmount) * 100;
        percentageCheck = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(percentageCheck));

        openChart();
    }

    private void openChart() {

        // Pie Chart Slice Names
        code = new String[]{
                "Cash", "Credit Card", "Check"
        };

        // Pie Chart Slice Values
        float[] distribution = {percentageCash, percentageCC, percentageCheck};

        // Color of each Pie Chart Slices
        int[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");

        for (int i = 0; i < distribution.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {

            // Instantiating a render for the slice
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);

            // Adding the renderer of a slice to the renderer of the pie chart
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Chart View");
        defaultRenderer.setChartTitleTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);

        // Getting a reference to view group linear layout chart_container
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);


        // Getting PieChartView to add to the custom layout
        mChart = ChartFactory.getPieChartView(getBaseContext(), distributionSeries, defaultRenderer);

        defaultRenderer.setClickEnabled(true);//
        defaultRenderer.setSelectableBuffer(10);
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {

                }
            }
        });

        chartContainer.addView(mChart);
    }

    @Override
    public void onInitViews() {
    }

    @Override
    public void onListenerRegister() {
    }

    @Override
    public void onDataRecieved(JSONArray arry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSocketStateChanged(int state) {
        // TODO Auto-generated method stub

    }

}
