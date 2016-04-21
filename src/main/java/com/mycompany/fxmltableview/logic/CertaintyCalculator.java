/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fxmltableview.logic;

import com.mycompany.fxmltableview.datamodel.Entry;
import com.mycompany.fxmltableview.datamodel.RawDataFile;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author stefankoch calculates certainty of fittedshift the penalty value that
 * has to be added to all fittedshifts for the fittedshift of interest to change
 */
public class CertaintyCalculator {

    private Session session;

    public CertaintyCalculator(Session session) {
        this.session = session;

    }

    public void calculate() {
        
        double cert = 0.05; //value that get's subtracted from original/iteration (from theoretical 1)
        //penalty for change in position
        double distpen = 1.0/session.getResolution();
        
        for (int i = 0; i < session.getListofOGroups().size(); i++) {
                            //delete old certainties
                            session.getListofOGroups().get(i).setCertainties(new HashMap<RawDataFile,Double>());                
        }
        
        for (int d = 0; d < session.getListofDatasets().size(); d++) {
            if (session.getListofDatasets().get(d).getActive()) {
                for (int f = 0; f < session.getListofDatasets().get(d).getListofFiles().size(); f++) {
                    RawDataFile currentfile = session.getListofDatasets().get(d).getListofFiles().get(f);
                    if (currentfile.getActive().booleanValue()) {

                        //build original weight matrix
                        Collections.sort(session.getListofOGroups(), new Entry.orderbyRT());
                        double[][] matrix = new double[session.getListofOGroups().size()][session.getResolution()];

                        for (int i = 0; i < session.getListofOGroups().size(); i++) {   
                            //add best possible certainty 
                            session.getListofOGroups().get(i).getCertainties().put(currentfile, 1.0);
                            
                            double[] PropArray = session.getListofOGroups().get(i).getOGroupPropArraySmooth(currentfile);
                            for (int j = 0; j < session.getResolution(); j++) {
                                matrix[i][j] = PropArray[j];
                            }
                        }

                        //iterate
                        for (int p = 0; p < 20; p++) {
                            

                            //add penalty to fittedpeaks
                            //subtracts p*cert*value from value in original matrix, linear penalty
                            for (int i = 0; i < session.getListofOGroups().size(); i++) {
                                for (int t = session.getListofOGroups().get(i).getOGroupFittedShift(currentfile)-session.getIntPeakRTTol(); t<=session.getListofOGroups().get(i).getOGroupFittedShift(currentfile)+session.getIntPeakRTTol(); t++) {
                                    matrix[i][t] = matrix[i][t]*(1.0/(1.0-p*cert))*(1-(p+1)*cert) ;
                                }
                            }
                            
                            

                            //build new weight matrix
                            double[][] weights = new double[session.getListofOGroups().size()][session.getResolution()];
                            //fill first row
                            for (int j = 0; j < session.getResolution(); j++) {
                                weights[0][j] = matrix[0][j];

                            }
                            //fill rest of weights matrix
                            double penalty = session.getListofDatasets().get(0).getPenalty();
                            for (int i = 1; i < session.getListofOGroups().size(); i++) {
                                for (int j = 0; j < session.getResolution(); j++) {
                                    double max = 0;
                                    if (weights[i - 1][j] > max) {
                                        max = weights[i - 1][j] + matrix[i][j];
                                    }
                                    if ((j - 1) > 0 && weights[i - 1][j - 1] + matrix[i][j] - penalty > max) {
                                        max = weights[i - 1][j - 1] + matrix[i][j] - penalty;
                                    }
                                    if ((j + 1) < session.getResolution() && weights[i - 1][j + 1] + matrix[i][j] - penalty > max) {
                                        max = weights[i - 1][j + 1] + matrix[i][j] - penalty;
                                    }
                                    weights[i][j] = max;

                                }

                            }
                            
                            //calculate path
                            //get max in last row
                                double max = 0;
                                int maxint = 0;
                                for (int j = 0; j < session.getResolution(); j++) {
                                    if (weights[session.getListofOGroups().size() - 1][j] > max) {
                                        maxint = j;
                                        max = weights[session.getListofOGroups().size() - 1][j];
                                    }
                                }

                                //TODO: calculate range as function of time
                                for (int i = session.getListofOGroups().size() - 2; i > -1; i--) {
                                    max = 0;

                                    int j = maxint;
                                    if ((j - 1) > 0 && weights[i][j - 1] > max) {
                                        max = weights[i][j - 1];
                                        maxint = j - 1;
                                    }
                                    if (weights[i][j] > max) {
                                        max = weights[i][j];
                                        maxint = j;
                                    }
                                    if ((j + 1) < session.getResolution() && weights[i][j + 1] > max) {
                                        //max = weights[i][j+1];
                                        maxint = j + 1;
                                    }
                            
                                    //check if changed
                                    int dist = Math.abs(maxint-session.getListofOGroups().get(i).getOGroupFittedShift(currentfile));
                                    if (dist>session.getIntPeakRTTol()) {
                                        //calculate certainty
                                        
                                        //first part is what fraction of the original value has been subtracted. High value = high certainty
                                        //second part is the distance from the original fittedshift, high distance = low certainty
                                        double fp = (p+1)*cert;
                                        double sp = 1-(dist*distpen);
                                        
                                        if (session.getListofOGroups().get(i).getCertainties().get(currentfile)>fp) {
                                           session.getListofOGroups().get(i).getCertainties().put(currentfile,fp);
                                        }
                                    }
                                        
                                    }
                            

                        }

                    }

                }
            }
        }

    }

}
