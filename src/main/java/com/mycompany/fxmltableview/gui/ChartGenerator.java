/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fxmltableview.gui;

import com.mycompany.fxmltableview.datamodel.Entry;
import com.mycompany.fxmltableview.datamodel.Peak;
import com.mycompany.fxmltableview.datamodel.RawDataFile;
import com.mycompany.fxmltableview.datamodel.Slice;
import com.mycompany.fxmltableview.logic.Session;
import java.util.Collections;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author stefankoch TODO: Just read every slice once and generate all the
 * different graphs TODO: Performance:
 * http://stackoverflow.com/questions/28850211/performance-issue-with-javafx-linechart-with-65000-data-points
 *
 */
public class ChartGenerator {
    
    private Fxml_adductviewController adductcontroller;
    private Fxml_shiftviewController shiftcontroller;
    

    private Session session;
    

    public ChartGenerator(Fxml_adductviewController controller, Fxml_shiftviewController shiftcontroller) {
        this.session = session;
        this.adductcontroller = controller;
        this.shiftcontroller = shiftcontroller;

       
        
       
    }

    public LineChart generateEIC(Entry adduct) {

        //Basic Chart attributes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Intensity");
        LineChart<Number, Number> linechart = new LineChart(xAxis, yAxis);
        
        
        if (adductcontroller.getAdducttochart().containsKey(adduct)) {
            adductcontroller.getAdducttochart().get(adduct).add(linechart);
        } else {
            ArrayList list = new ArrayList();
            list.add(linechart);
            adductcontroller.getAdducttochart().put(adduct, list);
        }
        
        

        //double startouter = System.currentTimeMillis();
        // for all slices (= for all files)
        for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                Slice currentSlice = adduct.getListofSlices().get(currentfile);
                XYChart.Series newSeries = new XYChart.Series();
                adductcontroller.getSeriestochart().put(newSeries, linechart);
                
                //add Series to HashMaps
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                if (adductcontroller.getFiletoseries().containsKey(currentfile)){
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList list = new ArrayList();
                list.add(newSeries);
                adductcontroller.getFiletoseries().put(currentfile, list);
                        }

                //while the next RT is the same as the one before, add Intensities
                //double startinner = System.currentTimeMillis();
                for (int j = 0; j < currentSlice.getIntensityArray().length; j++) {
                    double intensity = currentSlice.getIntensityArray()[j];
                    double currentRT = currentSlice.getRTArray()[j];
                    XYChart.Data data = new XYChart.Data(currentRT, intensity);

                    newSeries.getData().add(data);

                }

                // add new Series
                linechart.getData().add(newSeries);
                //apply Css to create nodes
                linechart.applyCss();
                //cast to path to be able to set stroke
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else {
                ((Path) newSeries.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                
                ((Path) newSeries.getNode()).setStrokeWidth(currentSlice.getFile().getWidth());


            }
        }}}

        //don't draw symbols
        linechart.setCreateSymbols(false);
        //set size of chart
        linechart.setMaxSize(450, 300);

        double lower = adduct.getMinRT();
        double upper = adduct.getMaxRT();
        //set Range
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit((upper - lower) / 6);
        xAxis.setLowerBound(lower);
        xAxis.setUpperBound(upper);

        linechart.setAnimated(false);
//        linechart.setCache(true);
//        linechart.setCacheHint(CacheHint.SPEED);
        linechart.setLegendVisible(false);
        return linechart;
    }

    public LineChart generateNormalizedEIC(Entry adduct) {

        //Basic Chart attributes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Intensity (normalized)");
        LineChart<Number, Number> linechart = new LineChart(xAxis, yAxis);
        
        if (adductcontroller.getAdducttochart().containsKey(adduct)) {
            adductcontroller.getAdducttochart().get(adduct).add(linechart);
        } else {
            ArrayList list = new ArrayList();
            list.add(linechart);
            adductcontroller.getAdducttochart().put(adduct, list);
        }

        // for all slices (= for all files)
        //double startouter = System.currentTimeMillis();
         for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                Slice currentSlice = adduct.getListofSlices().get(currentfile);

                XYChart.Series newSeries = new XYChart.Series();
                adductcontroller.getSeriestochart().put(newSeries, linechart);
 
                //add Series to HashMaps
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                if (adductcontroller.getFiletoseries().containsKey(currentfile)){
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList list = new ArrayList();
                list.add(newSeries);
                adductcontroller.getFiletoseries().put(currentfile, list);
                        }
                
                double maxIntensity = Arrays.stream(currentSlice.getIntensityArray()).max().getAsDouble();
                //double startinner = System.currentTimeMillis();
                for (int j = 0; j < currentSlice.getIntensityArray().length; j++) {
                    double intensity = currentSlice.getIntensityArray()[j];
                    double currentRT = currentSlice.getRTArray()[j];
                    newSeries.getData().add(new XYChart.Data(currentRT, intensity / maxIntensity));

                }
                //double endinner = System.currentTimeMillis();
                //System.out.println("Inner loop norm: " + (endinner-startinner));
                linechart.getData().add(newSeries);
                linechart.applyCss();
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else {
                ((Path) newSeries.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                ((Path) newSeries.getNode()).setStrokeWidth(currentSlice.getFile().getWidth());
                linechart.setCreateSymbols(false);
                linechart.setMaxSize(450, 300);

                //double endouter = System.currentTimeMillis();
                //System.out.println("Outer loop norm: " + (endouter-startouter));
//set Range
             generateShiftmarker(adduct,currentfile,linechart);
             generatePeakmarker(adduct, currentfile, linechart);
            }
            
        }}}
        double lower = adduct.getMinRT();
        double upper = adduct.getMaxRT();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit((upper - lower) / 6);
        xAxis.setLowerBound(lower);
        xAxis.setUpperBound(upper);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        linechart.setAnimated(false);
//        linechart.setCache(true);
//        linechart.setCacheHint(CacheHint.SPEED);
        linechart.setLegendVisible(false);
        return linechart;
    }
    
    public LineChart generateNormalizedEICwithPeak(Entry adduct) {

        //Basic Chart attributes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Intensity (normalized)");
        LineChart<Number, Number> linechart = new LineChart(xAxis, yAxis);

        // for all slices (= for all files)
        //double startouter = System.currentTimeMillis();
         for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                Slice currentSlice = adduct.getListofSlices().get(currentfile);

                XYChart.Series newSeries = new XYChart.Series();
 
                //add Series to HashMaps
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                if (adductcontroller.getFiletoseries().containsKey(currentfile)){
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList list = new ArrayList();
                list.add(newSeries);
                adductcontroller.getFiletoseries().put(currentfile, list);
                        }
                
                double maxIntensity = Arrays.stream(currentSlice.getIntensityArray()).max().getAsDouble();
                //double startinner = System.currentTimeMillis();
                for (int j = 0; j < currentSlice.getIntensityArray().length; j++) {
                    double intensity = currentSlice.getIntensityArray()[j];
                    double currentRT = currentSlice.getRTArray()[j];
                    newSeries.getData().add(new XYChart.Data(currentRT, intensity / maxIntensity));

                }
                //double endinner = System.currentTimeMillis();
                //System.out.println("Inner loop norm: " + (endinner-startinner));
                linechart.getData().add(newSeries);
                linechart.applyCss();
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else {
                ((Path) newSeries.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                ((Path) newSeries.getNode()).setStrokeWidth(currentSlice.getFile().getWidth());
                linechart.setCreateSymbols(false);
                linechart.setMaxSize(300, 200);

                //double endouter = System.currentTimeMillis();
                //System.out.println("Outer loop norm: " + (endouter-startouter));
//set Range
                if (adduct.getAdductFittedShift(currentfile) > 0) {
                    XYChart.Series newSeries2 = new XYChart.Series();
                    double[] RTArray = adduct.getRTArray();
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getAdductFittedShift(currentfile)], 0));
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getAdductFittedShift(currentfile)], 1));
                    linechart.getData().add(newSeries2);
                    linechart.applyCss();
                    if (currentfile.isselected()) {
                    paintselectedLine(newSeries2.getNode());
                }else {
                ((Path) newSeries2.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                    ((Path) newSeries2.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries2.getNode()).getStrokeDashArray().setAll(4d, 4d, 4d, 4d, 4d);
                    adductcontroller.getSeriestofile().put(newSeries2, currentfile);
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries2);
                    
                    XYChart.Series newSeries3 = new XYChart.Series();
                    double[] RTArray3 = adduct.getRTArray();
                    newSeries3.getData().add(new XYChart.Data(RTArray[adduct.getListofSlices().get(currentfile).getfittedPeakStart()], 1));
                    newSeries3.getData().add(new XYChart.Data(RTArray[adduct.getListofSlices().get(currentfile).getfittedPeakStart()], 0));
                     newSeries3.getData().add(new XYChart.Data(RTArray[adduct.getListofSlices().get(currentfile).getfittedPeakEnd()], 0));
                    newSeries3.getData().add(new XYChart.Data(RTArray[adduct.getListofSlices().get(currentfile).getfittedPeakEnd()], 1));
                    linechart.getData().add(newSeries3);
                    linechart.applyCss();
                    if (currentfile.isselected()) {
                    paintselectedLine(newSeries3.getNode());
                }else {
                ((Path) newSeries3.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                    ((Path) newSeries3.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries3.getNode()).getStrokeDashArray().setAll(0.5d, 6d, 0.5d, 6d);
                    adductcontroller.getSeriestofile().put(newSeries3, currentfile);
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries3);

                } else {
                    XYChart.Series newSeries2 = new XYChart.Series();
                    double[] RTArray = adduct.getRTArray();
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], 0));
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], 1));
                    linechart.getData().add(newSeries2);
                    linechart.applyCss();
                    if (currentfile.isselected()) {
                    paintselectedLine(newSeries2.getNode());
                }else {
                ((Path) newSeries2.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                    ((Path) newSeries2.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries2.getNode()).getStrokeDashArray().setAll(1d,15d,1d,15d);
                    adductcontroller.getSeriestofile().put(newSeries2, currentfile);
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries2);

                    
                }
            }
        }}}
        double lower = adduct.getMinRT();
        double upper = adduct.getMaxRT();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit((upper - lower) / 6);
        xAxis.setLowerBound(lower);
        xAxis.setUpperBound(upper);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        linechart.setAnimated(false);
//        linechart.setCache(true);
//        linechart.setCacheHint(CacheHint.SPEED);
        linechart.setLegendVisible(false);
        return linechart;
    }


    public LineChart generateNormalizedEICwithProp(Entry adduct) {

        //Basic Chart attributes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Intensity (normalized)");
        LineChart<Number, Number> linechart = new LineChart(xAxis, yAxis);

        // for all slices (= for all files)
        //double startouter = System.currentTimeMillis();
         for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                Slice currentSlice = adduct.getListofSlices().get(currentfile);

                XYChart.Series newSeries = new XYChart.Series();
                
                 //add Series to HashMaps
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                if (adductcontroller.getFiletoseries().containsKey(currentfile)){
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList list = new ArrayList();
                list.add(newSeries);
                adductcontroller.getFiletoseries().put(currentfile, list);
                        }

                double maxIntensity = Arrays.stream(currentSlice.getIntensityArray()).max().getAsDouble();
                //double startinner = System.currentTimeMillis();
                for (int j = 0; j < currentSlice.getIntensityArray().length; j++) {
                    double intensity = currentSlice.getIntensityArray()[j];
                    double currentRT = currentSlice.getRTArray()[j];
                    newSeries.getData().add(new XYChart.Data(currentRT, intensity / maxIntensity));

                }
                //double endinner = System.currentTimeMillis();
                //System.out.println("Inner loop norm: " + (endinner-startinner));
                linechart.getData().add(newSeries);
                linechart.applyCss();
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else {
                ((Path) newSeries.getNode()).setStroke(currentSlice.getFile().getColor()); 
                }
                ((Path) newSeries.getNode()).setStrokeWidth(currentSlice.getFile().getWidth());
                linechart.setCreateSymbols(false);
                linechart.setMaxSize(300, 200);

            }
        }}}

        //double endouter = System.currentTimeMillis();
        //System.out.println("Outer loop norm: " + (endouter-startouter));
//set Range
        double lower = adduct.getMinRT();
        double upper = adduct.getMaxRT();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit((upper - lower) / 6);
        xAxis.setLowerBound(lower);
        xAxis.setUpperBound(upper);

        linechart.setAnimated(false);
//        linechart.setCache(true);
//        linechart.setCacheHint(CacheHint.SPEED);
        linechart.setLegendVisible(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(PropArray(adduct, linechart));
        return linechart;
    }

    public ScatterChart generateMassChart(Entry adduct) {

        //Basic Chart attributes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("ppm M/Z deviation");
        ScatterChart<Number, Number> scatterchart = new ScatterChart(xAxis, yAxis);
        
        if (adductcontroller.getAdducttochart().containsKey(adduct)) {
            adductcontroller.getAdducttochart().get(adduct).add(scatterchart);
        } else {
            ArrayList list = new ArrayList();
            list.add(scatterchart);
            adductcontroller.getAdducttochart().put(adduct, list);
        }

        // for all slices (= for all files)
        //double startouter = System.currentTimeMillis();
         for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                Slice currentSlice = adduct.getListofSlices().get(currentfile);

                XYChart.Series newSeries = new XYChart.Series();
                adductcontroller.getSeriestochart().put(newSeries, scatterchart);
                
                
                 //add Series to HashMaps
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                if (adductcontroller.getFiletoseries().containsKey(currentfile)){
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList list = new ArrayList();
                list.add(newSeries);
                adductcontroller.getFiletoseries().put(currentfile, list);
                        }
                
                double maxIntensity = Arrays.stream(currentSlice.getIntensityArray()).max().getAsDouble();
                double width = currentSlice.getFile().getWidth() + 1.5;
//double startinner = System.currentTimeMillis();
                for (int j = 0; j < currentSlice.getMZArray().length; j++) {
                    XYChart.Data data = new XYChart.Data(currentSlice.getRTArray()[j], getppm(adduct.getMZ(), currentSlice.getMZArray()[j]));

                    //rect is the node of the plot
                    Rectangle rect1 = new Rectangle(width, width);
                    
                    
                    if (currentfile.isselected()) {
                    paintselectedScatter(rect1);
                }else {
                rect1.setFill(currentSlice.getFile().getColor());
                }
                    

                    //set rect as node
                    data.setNode(rect1);

                    //set opacity
                    data.getNode().setOpacity(currentSlice.getIntensityArray()[j] / maxIntensity);

                    //set Tooltip
                    //Tooltip tooltip = new Tooltip();
                    //tooltip.setText(currentSlice.getIntensityList().get(j).toString());
                    //Tooltip.install(data.getNode(), tooltip);
                    newSeries.getData().add(data);
                }

                //double endinner = System.currentTimeMillis();
//System.out.println("Inner loop mass: " + (endinner-startinner));
                scatterchart.getData().add(newSeries);
                scatterchart.setMaxSize(450, 300);

                //set Range
            }
        }}}
        //double endouter = System.currentTimeMillis();
        //System.out.println("Outer loop mass: " + (endouter-startouter));
        double lower = adduct.getMinRT();
        double upper = adduct.getMaxRT();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit((upper - lower) / 7);
        xAxis.setLowerBound(lower);
        xAxis.setUpperBound(upper);

        yAxis.setAutoRanging(false);
        yAxis.setTickUnit((adduct.getSession().getMZTolerance() * 2 / 5));
        yAxis.setLowerBound(-1 * adduct.getSession().getMZTolerance());
        yAxis.setUpperBound(adduct.getSession().getMZTolerance());
        scatterchart.setAnimated(false);
//        scatterchart.setCache(true);
//        scatterchart.setCacheHint(CacheHint.SPEED);
        scatterchart.setLegendVisible(false);
        scatterchart.setHorizontalZeroLineVisible(false);

        return scatterchart;

    }

    public double PropArray(Entry adduct, LineChart<Number, Number> linechart) {

        double maxProp = 1;
         for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < adduct.getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = adduct.getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {

                if (adduct.getOGroupObject().getOGroupPropArraySmooth(currentfile) == null) {
                    adduct.getOGroupObject().peakpickOGroup(currentfile);
                }

                double[] PropArray = adduct.getOGroupObject().getOGroupPropArraySmooth(currentfile);
//        
                List asList = Arrays.asList(ArrayUtils.toObject(PropArray));
                double max = (double) Collections.max(asList);
                double[] RTArray = adduct.getRTArray();
                XYChart.Series newSeries = new XYChart.Series();

                for (int i = 0; i < PropArray.length; i++) {
                    newSeries.getData().add(new XYChart.Data(RTArray[i], PropArray[i]));
                    if (maxProp < PropArray[i]) {
                        maxProp = PropArray[i];
                    }

                }
                linechart.getData().add(newSeries);
                linechart.applyCss();
                ((Path) newSeries.getNode()).setStroke(Color.ORANGE);
                ((Path) newSeries.getNode()).setStrokeWidth(1.5);

                double[] PropArray2 = adduct.getAdductPropArray(currentfile);

                List asList2 = Arrays.asList(ArrayUtils.toObject(PropArray2));
                double max2 = (double) Collections.max(asList2);
                double[] RTArray2 = adduct.getRTArray();
                XYChart.Series newSeries3 = new XYChart.Series();

                for (int i = 0; i < PropArray2.length; i++) {
                    newSeries3.getData().add(new XYChart.Data(RTArray2[i], PropArray2[i]));

                }
                linechart.getData().add(newSeries3);
                linechart.applyCss();
                ((Path) newSeries3.getNode()).setStroke(Color.RED);
                ((Path) newSeries3.getNode()).setStrokeWidth(1.5);

                if (adduct.getOGroupObject().getOGroupFittedShift(currentfile) > 0) {
                    XYChart.Series newSeries2 = new XYChart.Series();
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], 0));
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], maxProp));
                    linechart.getData().add(newSeries2);
                    linechart.applyCss();
                    ((Path) newSeries2.getNode()).setStroke(currentfile.getColor());
                    ((Path) newSeries2.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries2.getNode()).getStrokeDashArray().setAll(4d, 4d, 4d, 4d, 4d);
                }

            }
        }}}
        return maxProp;
    }

    public LineChart generateShiftChart(ObservableList<Entry> list) {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Shift [seconds]");
        LineChart<Number, Number> linechart = new LineChart(xAxis, yAxis);

        double upper = 0;
        double lower = 0;
 for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < list.get(0).getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = list.get(0).getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                XYChart.Series newSeries = new XYChart.Series();
                
                shiftcontroller.getSeriestofile().put(newSeries, currentfile);
                if (shiftcontroller.getFiletoseries().containsKey(currentfile)){
                    shiftcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList array = new ArrayList();
                array.add(newSeries);
                shiftcontroller.getFiletoseries().put(currentfile, array);
                        }
                
                double shiftiter = (list.get(0).getSession().getRTTolerance() * 2) / list.get(0).getSession().getResolution();
                int middleint = (list.get(0).getSession().getResolution() / 2) - 1;

                for (int i = 0; i < list.size(); i++) {
                    double shift = (list.get(i).getOGroupFittedShift(currentfile) - middleint) * shiftiter * 60;
                    XYChart.Data data = new XYChart.Data(list.get(i).getRT(), shift);
                    newSeries.getData().add(data);
                    if (shift > upper) {
                        upper = shift;
                    } else if (shift < lower) {
                        lower = shift;
                    }
                }
                linechart.getData().add(newSeries);
                linechart.applyCss();
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else{
                ((Path) newSeries.getNode()).setStroke(currentfile.getColor());
                }
                
                ((Path) newSeries.getNode()).setStrokeWidth(currentfile.getWidth());

//TODO: calulate number of active files
                System.out.println("Charts " + (f + 1) + "of " + list.get(0).getSession().getListofDatasets().get(d).getListofFiles().size() + " drawn");
            }
        }}}
        linechart.setCreateSymbols(false);
        linechart.setMaxSize(2000, 500);
        linechart.setLegendVisible(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(lower - 20);
        yAxis.setUpperBound(upper + 20);
        return linechart;
    }

    public double getppm(double massref, double mass) {
        double dif = massref - mass;

        return dif / (massref / 1000000);
    }
    
    

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    public void paintselectedLine(Node node) {
        node.setCursor(Cursor.HAND);
        ((Path) node).setStroke(Color.RED);
        
    }
    
    public void paintselectedScatter(Rectangle rect) {
        rect.setFill(Color.RED);
        
    }
    
    public ScatterChart generateScatterShiftChart(ObservableList<Entry> list) {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Shift [seconds]");
        ScatterChart<Number, Number> scatterchart = new ScatterChart(xAxis, yAxis);

        double upper = 0;
        double lower = 0;
        double shiftiter = (list.get(0).getSession().getRTTolerance() * 2) / list.get(0).getSession().getResolution();
        int middleint = (list.get(0).getSession().getResolution() / 2) - 1;
        
        
        //draw penalty Area
        XYChart.Series backSeries = new XYChart.Series();
        
        
        List<RawDataFile> sellist =  session.getSelectedFiles();
        List<XYChart.Data> points = new ArrayList<XYChart.Data>();
        //draw background
        for (int i = 0;i<list.size(); i=i+3) {
            for (int j = 0; j< session.getResolution(); j++) {
                boolean penalty = false;
               for (int s = 0; s<sellist.size(); s++) {
                   if (sellist.get(s).getActive()) {
                       if (list.get(i).getPenArray()!=null){
                           if (list.get(i).getPenArray().containsKey(sellist.get(s))) {
                       if (list.get(i).getPenArray().get(sellist.get(s))[j]<0) {
                           penalty = true;
                           break;
                           
                       }
                       }
                       }
                   }
               }
                if (penalty) {
                    XYChart.Data data = new XYChart.Data(list.get(i).getRT(), (j-middleint)*60*shiftiter);
                    Rectangle rect = new Rectangle(5,5);
                    rect.setFill(Color.PINK);
                    data.setNode(rect);
                    points.add(data);
                }
               
            }
        }
        backSeries.getData().addAll(points);
        scatterchart.getData().add(backSeries);
            
            
            
 
        
 for (int d = 0; d<session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
        for (int f = 0; f < list.get(0).getSession().getListofDatasets().get(d).getListofFiles().size(); f++) {
            RawDataFile currentfile = list.get(0).getSession().getListofDatasets().get(d).getListofFiles().get(f);
            if (currentfile.getActive().booleanValue()) {
                XYChart.Series newSeries = new XYChart.Series();
                
                shiftcontroller.getSeriestofile().put(newSeries, currentfile);
                if (shiftcontroller.getFiletoseries().containsKey(currentfile)){
                    shiftcontroller.getFiletoseries().get(currentfile).add(newSeries);
                } else {
                ArrayList array = new ArrayList();
                array.add(newSeries);
                shiftcontroller.getFiletoseries().put(currentfile, array);
                        }
                
                
                
                

                double oshift = (list.get(0).getOGroupFittedShift(currentfile) - middleint) * shiftiter * 60;
                double oRT = list.get(0).getRT();
                double nshift;
                double nRT;
                
                for (int i = 1; i < list.size()-1; i++) {
                    double shift = (list.get(i).getOGroupFittedShift(currentfile) - middleint) * shiftiter * 60;
                    XYChart.Data data = new XYChart.Data(list.get(i).getRT(), shift);
                    
                    Ellipse cir = new Ellipse(1.5,4);
                    
                    TreeItem<Entry> item = null;
                    for (int e = 0; e<shiftcontroller.getSupercontroller().getMetTable().getRoot().getChildren().size(); e++) {
                        if (shiftcontroller.getSupercontroller().getMetTable().getRoot().getChildren().get(e).getValue().equals(list.get(i)))  {
                            item = shiftcontroller.getSupercontroller().getMetTable().getRoot().getChildren().get(e);
                            break;
                        }
                    }
                    shiftcontroller.getNodetoogroup().put(cir, item);
                    if (currentfile.isselected()) {
                    cir.setFill(Color.RED);
                }else {
                cir.setFill(currentfile.getColor());
                }
                    data.setNode(cir);
                    if (shiftcontroller.getOpacityMode().equals("Peak found")) {
                        data.getNode().setOpacity(list.get(i).getmaxScorepeakfound(currentfile)+0.02); 
                    } else if (shiftcontroller.getOpacityMode().equals("Peak close")) {
                        data.getNode().setOpacity(list.get(i).getminScorepeakclose(currentfile)+0.02); 
                    }
                    
                    
                    
                    
                    newSeries.getData().add(data);
                    if (shift > upper) {
                        upper = shift;
                    } else if (shift < lower) {
                        lower = shift;
                    }
                }
                scatterchart.getData().add(newSeries);
              
                

//TODO: number
                System.out.println("Charts " + (f + 1) + "of " + list.get(0).getSession().getListofDatasets().get(d).getListofFiles().size() + " drawn");
            }
        }}}

        scatterchart.setMaxSize(2000, 2000);
        scatterchart.setLegendVisible(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(lower - 30);
        yAxis.setUpperBound(upper + 30);
        return scatterchart;
    }
    
    //Peak Chart for peak view (context menu in Shiftview)
    public ScatterChart generateScatterPeakChart(ObservableList<Entry> list) {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("RT [minutes]");
        yAxis.setLabel("Shift [seconds]");
        ScatterChart<Number, Number> scatterchart = new ScatterChart(xAxis, yAxis);

     
        double shiftiter = (list.get(0).getSession().getRTTolerance() * 2) / list.get(0).getSession().getResolution();
        int middleint = (list.get(0).getSession().getResolution() / 2) - 1;
          List<XYChart.Data> points = new ArrayList<XYChart.Data>();
          List<RawDataFile> sellist =  session.getSelectedFiles();
        
        
            
   
        
        
        for (int i = 0; i< sellist.size(); i++) {
            for (int k = 1; k < list.size()-1; k++) {
            double shift = (list.get(k).getOGroupFittedShift(sellist.get(i)) - middleint) * shiftiter * 60;
                   XYChart.Data data = new XYChart.Data(list.get(k).getRT(), shift);
                    
                    Ellipse cir = new Ellipse(1.5,4);
                    cir.setFill(Color.ANTIQUEWHITE);
                    data.setNode(cir);
                    points.add(data);
            }
        }
        
        for (int i = 0; i< sellist.size(); i++) {
           
            List<Slice> slices = sellist.get(i).getListofSlices();
            for (int j = 0; j< slices.size(); j++) {
                for (int p = 0; p<slices.get(j).getListofPeaks().size(); p++) {
                    XYChart.Data data = new XYChart.Data(slices.get(j).getRT(), (slices.get(j).getListofPeaks().get(p).getIndex()-middleint)*60*shiftiter);
                    Rectangle rect = new Rectangle(1,1);
                    rect.setFill(Color.BLACK);
                    data.setNode(rect);
                    points.add(data);
                    
                }
                
                   
                    
            }
            

        }
        XYChart.Series peakSeries = new XYChart.Series();
        peakSeries.getData().addAll(points);
        scatterchart.getData().add(peakSeries);
        
        

        scatterchart.setMaxSize(2000, 2000);
        scatterchart.setLegendVisible(false);
//        yAxis.setAutoRanging(false);
//        yAxis.setLowerBound(lower - 90);
//        yAxis.setUpperBound(upper + 90);
        return scatterchart;
    }
    
    void generateShiftmarker(Entry adduct, RawDataFile currentfile, LineChart linechart) {
        
          if (adduct.getAdductFittedShift(currentfile) > 0) {
                    XYChart.Series newSeries2 = new XYChart.Series();
                    adductcontroller.getSeriestochart().put(newSeries2, linechart);
                    double[] RTArray = adduct.getRTArray();
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getAdductFittedShift(currentfile)], 0));
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getAdductFittedShift(currentfile)], 1));
                    linechart.getData().add(newSeries2);
                    linechart.applyCss();
                    if (currentfile.isselected()) {
                    paintselectedLine(newSeries2.getNode());
                }else {
                ((Path) newSeries2.getNode()).setStroke(currentfile.getColor()); 
                }
                    ((Path) newSeries2.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries2.getNode()).getStrokeDashArray().setAll(4d, 4d, 4d, 4d, 4d);
                    adductcontroller.getSeriestofile().put(newSeries2, currentfile);
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries2);

                } else {
                    XYChart.Series newSeries2 = new XYChart.Series();
                    adductcontroller.getSeriestochart().put(newSeries2, linechart);
                    double[] RTArray = adduct.getRTArray();
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], 0));
                    newSeries2.getData().add(new XYChart.Data(RTArray[adduct.getOGroupObject().getOGroupFittedShift(currentfile)], 1));
                    linechart.getData().add(newSeries2);
                    linechart.applyCss();
                    if (currentfile.isselected()) {
                    paintselectedLine(newSeries2.getNode());
                }else {
                ((Path) newSeries2.getNode()).setStroke(currentfile.getColor()); 
                }
                    ((Path) newSeries2.getNode()).setStrokeWidth(currentfile.getWidth());
                    ((Path) newSeries2.getNode()).getStrokeDashArray().setAll(1d,15d,1d,15d);
                    adductcontroller.getSeriestofile().put(newSeries2, currentfile);
                    adductcontroller.getFiletoseries().get(currentfile).add(newSeries2);

                    
                }
          
          
        
    }
    
    void generatePeakmarker(Entry adduct, RawDataFile currentfile, LineChart linechart) {
        
        List<Peak> list = adduct.getListofSlices().get(currentfile).getListofPeaks();
        if (list!= null) {
            for (int i = 0; i< list.size(); i++) {
                XYChart.Series newSeries = new XYChart.Series();
                adductcontroller.getSeriestochart().put(newSeries, linechart);
                double[] RTArray = adduct.getRTArray();
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getStart()], 1.13));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getStart()], 1.19));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getIndex()], 1.19));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getIndex()], 1.05));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getIndex()], 1.19));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getEnd()], 1.19));
                newSeries.getData().add(new XYChart.Data(RTArray[list.get(i).getEnd()], 1.13));
                linechart.getData().add(newSeries);
                adductcontroller.getPeakseries().add(newSeries);
                adductcontroller.getSeriestofile().put(newSeries, currentfile);
                adductcontroller.getFiletoseries().get(currentfile).add(newSeries);
                linechart.applyCss();
                if (currentfile.isselected()) {
                    paintselectedLine(newSeries.getNode());
                }else {
                ((Path) newSeries.getNode()).setStroke(currentfile.getColor()); 
                }
                ((Path) newSeries.getNode()).setStrokeWidth(currentfile.getWidth());
                //((Path) newSeries.getNode()).getStrokeDashArray().setAll(0.5d, 4d);
            } 
             
                   
                    
                    
            
        }
       
        
        
        
    }
    
}
