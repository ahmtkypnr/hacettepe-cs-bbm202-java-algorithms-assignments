import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;

class Main {
    public static void main(String args[]) throws IOException {

        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        int[] input = Functions.takeInput(args[0], 251282);

        double[][] yAxis = new double[3][inputAxis.length];
        System.out.println("INSERTION SORT WITH RANDOM");
        yAxis[0] = Functions.test(inputAxis, input, "Insertion", true);
        System.out.println("MERGE SORT WITH RANDOM");
        yAxis[1] = Functions.test(inputAxis, input, "Merge", true);
        System.out.println("COUNTING SORT WITH RANDOM");
        yAxis[2] = Functions.test(inputAxis, input, "Counting", true);

        showAndSaveChart("Tests on Random Data", inputAxis, yAxis, true);

        int[] sorted = input.clone();
        Functions.mergeSort(sorted);

        System.out.println("INSERTION SORT WITH SORTED");
        yAxis[0] = Functions.test(inputAxis, sorted, "Insertion", false);
        System.out.println("MERGE SORT WITH SORTED");
        yAxis[1] = Functions.test(inputAxis, sorted, "Merge", false);
        System.out.println("COUNTING SORT WITH SORTED");
        yAxis[2] = Functions.test(inputAxis, sorted, "Counting", false);

        showAndSaveChart("Tests on the Sorted Data", inputAxis, yAxis, true);

        int[] reverseSorted = Functions.reverse(sorted);

        System.out.println("INSERTION SORT WITH REVERSELY SORTED");
        yAxis[0] = Functions.test(inputAxis, reverseSorted, "Insertion", false);
        System.out.println("MERGE SORT WITH REVERSELY SORTED");
        yAxis[1] = Functions.test(inputAxis, reverseSorted, "Merge", false);
        System.out.println("COUNTING SORT WITH REVERSELY SORTED");
        yAxis[2] = Functions.test(inputAxis, reverseSorted, "Counting", false);

        showAndSaveChart("Tests on the Reversely Sorted Data", inputAxis, yAxis, true);

        System.out.println("LINEAR SEARCH WITH RANDOM");
        yAxis[0] = Functions.test(inputAxis, input, "Linear", true);
        System.out.println("LINEAR SEARCH WITH SORTED");
        yAxis[1] = Functions.test(inputAxis, sorted, "Linear", false);
        System.out.println("BINARY SEARCH WITH SORTED");
        yAxis[2] = Functions.test(inputAxis, sorted, "Binary", false);

        showAndSaveChart("Tests with Search Algorithms", inputAxis, yAxis, false);
    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis, boolean sort) throws IOException {
        String series1, series2, series3, yTitle;
        if (sort) {
            series1 = "Insertion Sort";
            series2 = "Merge Sort";
            series3 = "Counting Sort";
            yTitle = "Time in Milliseconds";
        }
        else {
            series1 = "Linear Search (Random Data)";
            series2 = "Linear Search (Sorted Data)";
            series3 = "Binary Search (Sorted Data)";
            yTitle = "Time in Nanoseconds";
        }
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle(yTitle).xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries(series1, doubleX, yAxis[0]);
        chart.addSeries(series2, doubleX, yAxis[1]);
        chart.addSeries(series3, doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
}
