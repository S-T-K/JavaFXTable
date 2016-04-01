/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fxmltableview.datamodel;

import com.mycompany.fxmltableview.logic.DomParser;
import com.mycompany.fxmltableview.logic.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 *
 * @author stefankoch
 * 
 * A File
 * Basically just a list of Slices, all other information is only needed when constructing a new file
 * 
 * TODO:
 * Implement Labels for Files (sick and healthy...)
 */
public class RawDataFile {

    private File file;
    private Dataset dataset;
    private List<Scan> listofScans;
    private List<Slice> listofSlices;
    private StringProperty name;
    private Session session;
    
    
    private final Property<Color> color;
    private DoubleProperty Width;
   
    //for M/Z cleaning
    private int[] mzbins;
    private DoubleProperty mzshift;

    //Constructor for new Raw Data file
    public RawDataFile(Dataset dataset, File file, Session session) {
        this.file=file;
        this.dataset=dataset;
        this.name = new SimpleStringProperty(file.getName());
        this.color= new SimpleObjectProperty(dataset.getColor());
        this.Width = new SimpleDoubleProperty(dataset.getWidth());
        this.session = session;
        mzbins = new int[100];
        mzshift = new SimpleDoubleProperty();
    }

    // parse Scans
    public void parseFile() {
        DomParser dpe = new DomParser(file.toString());
        this.listofScans = dpe.ParseFile();
        dpe=null;
    }

    //extract Slices, according to tolerances
    public void extractSlices(boolean isreference, List<Entry> data, float RTTolerance, float MZTolerance) {
        this.setListofSlices(new ArrayList<>());


        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getListofAdducts().size(); j++) {
                int Num = data.get(i).getListofAdducts().get(j).getNum();
                float MZ = (float) data.get(i).getListofAdducts().get(j).getMZ();
                float RT = (float) data.get(i).getListofAdducts().get(j).getOGroupRT();   //RT in Minutes
                Slice newSlice = new Slice(this, data.get(i).getListofAdducts().get(j)); 
                newSlice.extractSlicefromScans(listofScans);
                data.get(i).getListofAdducts().get(j).addSlice(newSlice);
                getListofSlices().add(newSlice);
                
                
            }
           
            
        }
 //get max bin
 int maxint = 0;
 int max = 0;
 for (int i =0; i<mzbins.length; i++) {
     if (mzbins[i]>max){
         max = mzbins[i];
         maxint = i;   
     }
 }
 
 //calculate "median" shift
double step = session.getMZTolerance()/(mzbins.length)*2;
mzshift = new SimpleDoubleProperty(session.getMZTolerance()-maxint*step);

//clean slices according to shift and tolerance
for (int i =0; i< listofSlices.size(); i++) {
    listofSlices.get(i).clean();
    listofSlices.get(i).generateInterpolatedEIC();
    
}
        
        
this.listofScans=null; //get rid of Scans, they are not needed any more

    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
       
    }

    /**
     * @param name the name to set
     */
    public void setName(StringProperty name) {
        this.name = name;
    }

    public final Color getColor() {
	return color.getValue();
    }

    public final void setColor(Color color) {
	this.color.setValue(color);
    }
    
    public Property<Color> colorProperty() {
	return color;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return Width.get();
    }

    /**
     * @param width the width to set
     */
    public void setWidth(DoubleProperty width) {
        this.Width = width;
    }

    /**
     * @return the listofSlices
     */
    public List<Slice> getListofSlices() {
        return listofSlices;
    }

    /**
     * @param listofSlices the listofSlices to set
     */
    public void setListofSlices(List<Slice> listofSlices) {
        this.listofSlices = listofSlices;
    }

    /**
     * @return the dataset
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    public void addtoBin(int bin){
        mzbins[bin]++;
        
    }

    /**
     * @return the mzbins
     */
    public int[] getMzbins() {
        return mzbins;
    }

    /**
     * @param mzbins the mzbins to set
     */
    public void setMzbins(int[] mzbins) {
        this.mzbins = mzbins;
    }

    /**
     * @return the mzshift
     */
    public double getMzshift() {
        return mzshift.get();
    }

    /**
     * @param mzshift the mzshift to set
     */
    public void setMzshift(DoubleProperty mzshift) {
        this.mzshift = mzshift;
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
    
    public void deleteFile() {
        dataset.getListofFiles().remove(this);
        
        
    }
    
}
