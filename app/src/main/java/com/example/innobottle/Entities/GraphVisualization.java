package com.example.innobottle.Entities;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphVisualization {


    // Graph = entire coordination system
    // series = line of points

    private static final int SCROLLABLE_BOUNDARY = 100;
    private static final int MAX_X_VALUE = 50;

    //graph view
    LineGraphSeries<DataPoint> series1;
    GraphView graph;
    ArrayList<Float> latestValues;
    int counter;

    public GraphVisualization(GraphView graph, LineGraphSeries<DataPoint> series1, ArrayList<Float> latestValues, int counter){
        this.graph = graph;
        this.series1 = series1;
        this.latestValues = latestValues;
        this.counter = counter;
    }

    public void initGraph(){
        styleGrid();
        styleGraph();
    }

    public void removeGraph(){
        graph.removeAllSeries();
        latestValues.clear();
        counter = 1;
    }

    // styles the coordination system itself
    private void styleGrid(){
        graph.getGridLabelRenderer().setVerticalAxisTitle("Maximum force / cell row [N]");
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.CENTER);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setNumHorizontalLabels(10);
        graph.getGridLabelRenderer().setPadding(25);
        graph.getGridLabelRenderer().setLabelsSpace(10);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);
        graph.getViewport().isScalable();
        //graph.getViewport().setYAxisBoundsManual(true);
        //graph.getViewport().setMinY(0);
    }

    // styles online the line
    private void styleGraph() {
        series1.setColor(Color.parseColor("#1F2936"));
        series1.setThickness(3);
    }

    public void createGraph(ArrayList<Float> values) {
        DataPoint point;
        if(values.size() > 0){
            try{
                Log.d("debug1", String.valueOf(latestValues.size()));
                point = createDataPoint(values);
                series1.appendData(point, false, 50);
                graph.addSeries(series1);
            } catch (IllegalArgumentException e) {
                Log.d("debug2", String.valueOf(latestValues.size()));
                Log.d("error 123", e.getMessage());
                // clear the latestValues list to remove remaining values from previous series
                latestValues.clear();
                Log.d("debug3", String.valueOf(latestValues.size()));
                // draw the point
                point = createDataPoint(values);
                // reset the graph by removing existing series
                graph.removeAllSeries();
                // overload the constructor of the the object by creating a new instance of LineGraphSeries
                series1 = new LineGraphSeries<>();
                series1.appendData(point, false, 50);
                // add the new object to the graph, then proceed
                graph.addSeries(series1);
            }
            counter++;
            scaleGraph();
        }

    }

    private DataPoint createDataPoint(ArrayList<Float> listValues){
        Log.d("inside create", String.valueOf(latestValues.size()));
        DataPoint point;
        latestValues.add(listValues.get(0));
        Float xVal = Float.valueOf(latestValues.size());
        Float yVal = latestValues.get(latestValues.size()-1);
        return point = new DataPoint(xVal, yVal);
    }

    private void scaleGraph() {
        // make graph start at 0 but scroll to end
        if(counter > MAX_X_VALUE) {
            graph.getViewport().scrollToEnd();
        }
        // make y axis scale in an appropriate way
        Float currentMax = latestValues.get(latestValues.size()-1);
        if(currentMax >= 20 && currentMax < 50) {
            graph.getViewport().setMaxY(50);
        } else if(currentMax >= 50 && currentMax < 100) {
            graph.getViewport().setMaxY(100);
        } else if(currentMax >= 100 && currentMax < 200) {
            graph.getViewport().setMaxY(200);
        }
    }
}