import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
public class FitnessChart  extends JFrame {
    public FitnessChart(String title, double[] meanBestFitness) {
        super(title);

        XYSeries series = new XYSeries("Mean Best Fitness");
        for (int i = 0; i < meanBestFitness.length; i++) {
            series.add(i + 1, meanBestFitness[i]); // Assuming iterations start from 1
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Mean Best Fitness Over Iterations",
                "Iterations",
                "Mean Best Fitness",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }
}
