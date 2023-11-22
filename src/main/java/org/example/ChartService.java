package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class ChartService {
    public void draw(List<String> date, List<Double> price) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < date.size(); i++) {
            dataset.addValue(price.get(i), "Function", date.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Close Price",
                "Date",
                "Price",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        JFrame frame = new JFrame("Function Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
