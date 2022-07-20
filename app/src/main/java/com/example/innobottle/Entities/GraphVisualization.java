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

    private static final int SCROLLABLE_BOUNDARY = 50;

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

    public void populateGraph(){
        createGraph(latestValues);
        scaleGraph();
    }

    public void removeGraph(){
        latestValues.clear();
        counter = 0;
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
        graph.getViewport().isScalable();
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
    }

    // styles online the line
    private void styleGraph() {
        series1.setColor(Color.parseColor("#1F2936"));
        series1.setThickness(3);
    }

    private void createGraph(ArrayList<Float> values) {
        latestValues.add(values.get(0));
        Float xValue, yValue;
        Float lastElement = latestValues.get(latestValues.size()-1);
        // x = index counter = x axis of graph
        // y = the actual value coming from Presenter -> from UDP packet -> from load cells

        // 50 ?
        if(latestValues.size() <= SCROLLABLE_BOUNDARY){
            xValue = Float.valueOf(latestValues.size());
            yValue = lastElement;
            counter++;
        } else {
            ArrayList<Float> newTemporaryList = new ArrayList<>();
            newTemporaryList.add(lastElement);
            latestValues = newTemporaryList;
            xValue = Float.valueOf(counter);
            yValue = lastElement;
            counter++;
        }

        DataPoint point = new DataPoint(xValue, yValue);
        series1.appendData(point, false, 500);
        graph.addSeries(series1);
        scaleGraph();
    }

    private void scaleGraph() {
        // make graph start at 0 but scroll to end
        if(latestValues.size() > SCROLLABLE_BOUNDARY) {
            graph.getViewport().scrollToEnd();
        }
        // make y axis scale in an appropriate way
        float currentMax = latestValues.get(latestValues.size()-1);
        if(currentMax > 1 && currentMax < 5) {
            graph.getViewport().setMaxY(5);
        } else if(currentMax >= 5 && currentMax < 10) {
            graph.getViewport().setMaxY(10);
        } else if(currentMax >= 10 && currentMax < 20) {
            graph.getViewport().setMaxY(20);
        } else if(currentMax >= 20 && currentMax < 50) {
            graph.getViewport().setMaxY(50);
        } else if(currentMax >= 50 && currentMax < 100) {
            graph.getViewport().setMaxY(100);
        } else if(currentMax >= 100 && currentMax < 200) {
            graph.getViewport().setMaxY(200);
        }
    }
}