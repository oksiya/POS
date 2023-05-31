package com.example.pos;

import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pos.databinding.FragmentAnalysisBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Analysis extends Fragment {


    private FragmentAnalysisBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnTotalSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.barChart.setVisibility(View.VISIBLE);
                binding.pieChart.setVisibility(View.GONE);
                binding.productChart.setVisibility(View.GONE);
                List<SSRecords> salesList = salesList();
                double totalSales = calculateTotalSales(salesList);
                Map<String, Double> totalPricePerSale = calculateTotalPricePerSale(salesList);
                showTotalSalesLineChart(totalPricePerSale);


            }
        });

        binding.btnSalesByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.pieChart.setVisibility(View.VISIBLE);
                binding.barChart.setVisibility(View.GONE);
                binding.productChart.setVisibility(View.GONE);
                List<SSRecords> salesList = salesList();
                Map<String, Double> salesByCategory = calculateSalesByCategory(salesList);
                showSalesByCategoryPieChart(salesByCategory);

            }
        });

        binding.btnTopSellingProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.pieChart.setVisibility(View.GONE);
                binding.barChart.setVisibility(View.GONE);
                binding.productChart.setVisibility(View.VISIBLE);
                List<SSRecords> salesList = salesList();
                Map<String, Integer> topItems = getTopItems(salesList, 5);
                showTopSellingItemsBarChart(topItems);


            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(FirstFragment.this)
                //       .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private List<SSRecords> salesList(){
        ArrayList<SSRecords> salesList = new ArrayList<>();
        try (SQLHelper database = new SQLHelper(this.getContext())) {
            salesList = database.getSalesData();
        }catch(SQLException e){

            e.printStackTrace();
        }
        return salesList;
    }

    public double calculateTotalSales(List<SSRecords> salesList) {
        double totalSales = 0.0;
        for (SSRecords record : salesList) {
            if(record.getAccount().equals("Sale")){
                totalSales += record.getDebit();
            }

        }
        return totalSales;
    }

    public Map<String, Double> calculateSalesByCategory(List<SSRecords> salesList) {
        Map<String, Double> salesByCategory = new HashMap<>();
        for (SSRecords record : salesList) {
            if(record.getAccount().equals("Sale")){
                String category = record.getCategory();
                double sales = salesByCategory.getOrDefault(category, 0.0);
                sales += record.getDebit();
                salesByCategory.put(category, sales);
            }

        }
        return salesByCategory;
    }


    private void showSalesByCategoryPieChart(Map<String, Double> salesByCategory) {
        // Create a list of colors for the chart slices
        List<Integer> sliceColors = new ArrayList<>();
        sliceColors.add(Color.parseColor("#A8DADC"));
        sliceColors.add(Color.parseColor("#457B9D"));
        sliceColors.add(Color.parseColor("#F7A278"));
        sliceColors.add(Color.parseColor("#A13D63"));
        sliceColors.add(Color.parseColor("#6DD3CE"));
        sliceColors.add(Color.parseColor("#C8E9A0"));
        sliceColors.add(Color.parseColor("#E1F4CB"));
        sliceColors.add(Color.parseColor("#BACBA9"));
        sliceColors.add(Color.parseColor("#717568"));
        sliceColors.add(Color.parseColor("#FFE5D4"));
        sliceColors.add(Color.parseColor("#FFE5D4"));
        sliceColors.add(Color.parseColor("#BFD3C1"));

        // Create a PieDataSet using the sales data
        List<PieEntry> entries = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, Double> entry : salesByCategory.entrySet()) {
            String category = entry.getKey();
            double sales = entry.getValue();
            entries.add(new PieEntry((float) sales, category));
            colorIndex++;
        }


        // Configure the PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(sliceColors);

        Description description = new Description();
        description.setText("Top Selling Items");
        description.setTextSize(14f);
        description.setTextAlign(Paint.Align.CENTER);

        // Create the PieData object
        PieData data = new PieData(dataSet);

        // Customize the chart and display it
        PieChart chart = binding.pieChart;
        chart.setNoDataText("");
        chart.setDescription(description);
        description.setPosition(chart.getWidth() / 2f, chart.getHeight() / 2f);
        chart.getDescription().setTextColor(Color.BLACK);
        Legend legend = chart.getLegend();
        legend.setTextColor(Color.WHITE);
        chart.setData(data);
        chart.invalidate();
    }




    public Map<String, Double> calculateTotalPricePerSale(List<SSRecords> salesRecords) {
        Map<String, Double> totalPricePerSale = new HashMap<>();

        for (SSRecords record : salesRecords) {
            if(record.getAccount().equals("Sale")){
                String timeStamp = record.getDate();
                double price = record.getDebit();

                if (totalPricePerSale.containsKey(timeStamp)) {
                    double total = totalPricePerSale.get(timeStamp);
                    total += price;
                    totalPricePerSale.put(timeStamp, total);
                } else {
                    totalPricePerSale.put(timeStamp, price);
                }

            }

        }

        return totalPricePerSale;
    }

    public void showTotalSalesLineChart(Map<String, Double> totalPricePerSale) {
        LineChart lineChart = binding.barChart;

        List<Entry> entries = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Double> entry : totalPricePerSale.entrySet()) {
            String timeStamp = entry.getKey();
            double totalPrice = entry.getValue();

            // Add an entry to the chart
            entries.add(new Entry(index, (float) totalPrice));

            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Total Sales");
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);

        Description description = new Description();
        description.setText("Total per sale");
        description.setTextSize(14f);
        lineChart.setDescription(description);
        lineChart.setNoDataText("");
        lineChart.getDescription().setTextColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false); // Disable right Y-axis

        // Enable value selection
        lineChart.setTouchEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);

        // Setup value selected listener
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Display the coordinates when a data point is selected
                float x = e.getX() + 1;
                float y = e.getY();

                Toast.makeText(getContext(), "Customer: " + (int) x + ", Total Price: R" + HelperFunctions.roundToTwoDecimalPlaces(y), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                // Do nothing when no data point is selected
            }
        });

        lineChart.invalidate();
    }

    public Map<String, Integer> getTopItems(List<SSRecords> salesList, int numTopItems) {
        // Create a map to store item names and their total quantities
        Map<String, Integer> itemQuantities = new HashMap<>();

        // Calculate the quantity for each item based on occurrence count
        for (SSRecords record : salesList) {
            if(record.getAccount().equals("Sale")){
                String itemName = record.getName();

                // Update the item's quantity in the map
                itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + 1);

            }

        }

        // Sort the items by their quantities in descending order
        List<Map.Entry<String, Integer>> sortedItems = itemQuantities.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());

        // Get the top selling items based on the specified number
        List<Map.Entry<String, Integer>> topItems = sortedItems.stream()
                .limit(numTopItems)
                .collect(Collectors.toList());

        // Create a map to store the top selling items
        Map<String, Integer> topSellingItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : topItems) {
            topSellingItems.put(entry.getKey(), entry.getValue());
        }

        return topSellingItems;
    }

    public void showTopSellingItemsBarChart(Map<String, Integer> topSellingItems) {
        BarChart barChart = binding.productChart;

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : topSellingItems.entrySet()) {
            String itemName = entry.getKey();
            int itemCount = entry.getValue();

            // Add a bar entry to the chart
            entries.add(new BarEntry(index, itemCount));
            labels.add(itemName);

            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Top Selling Items");
        dataSet.setColors(getCustomColors());
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position to bottom
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f); // Set the granularity to 1 to display all labels
        barChart.getXAxis().setLabelRotationAngle(45f); // Rotate the labels by 45 degrees
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getLegend().setYOffset(10f);
        barChart.setNoDataText("");

        Description description = new Description();
        description.setText("Top Selling Items");
        description.setTextSize(12f);
        description.setPosition(barChart.getWidth() - description.getTextSize(), barChart.getTop() + description.getTextSize());
        barChart.setDescription(description);
        barChart.getDescription().setTextColor(Color.WHITE);

        barChart.invalidate();
    }

    private int[] getCustomColors() {
        int[] colors = new int[] {

                Color.parseColor("#A8DADC"),
                Color.parseColor("#457B9D"),
                Color.parseColor("#F7A278"),
                Color.parseColor("#A13D63"),
                Color.parseColor("#E1F4CB"),
                Color.parseColor("#BACBA9"),
                Color.parseColor("#717568"),
                Color.parseColor("#FFE5D4"),
                Color.parseColor("#FFE5D4"),
                Color.parseColor("#BFD3C1"),
        };

        return colors;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}