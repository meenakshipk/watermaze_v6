/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wmsoftware;

import ij.process.ImageProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meenakshi 10092019
 */
public class DataStore {

    private int totalTrials = 0;
    private int totalMice = 0;
    private ArrayList<HashMap<String, Object>>[] dataInput = null;
    HashMap<String, HashMap<String, Object>> data = null;

    /**
     *
     * @param totalTrialNo
     * @param totalMiceNo
     * @param trial Total number of trials
     * @param mice Total number of mice per trial
     */
    public DataStore(int totalTrialNo, int totalMiceNo) {
        totalTrials = totalTrialNo;
        totalMice = totalMiceNo;
        dataInput = (ArrayList<HashMap<String, Object>>[]) new ArrayList[totalTrials];
        for (int t = 0; t < totalTrials; t++) {
            ArrayList<HashMap<String, Object>> trial = new ArrayList<>();
            for (int m = 0; m < totalMice; m++) {
                HashMap<String, Object> mouse = new HashMap<>();
                mouse.put("ID", "M" + String.valueOf(m));
                trial.add(mouse);
            }
            HashMap<String, Object> mouse = new HashMap<>();
            mouse.put("ID", "AveM");
            trial.add(mouse);
            dataInput[t] = trial;
        }
    }

    public ArrayList<HashMap<String, Object>>[] getInputData() {
        return dataInput;
    }

    public int getTotalMice() {
        return totalMice;
    }

    public int getTotalTrials() {
        return totalTrials;
    }

    /**
     * Reads an array of file
     *
     */
    public void readFile() {
        for (int trial = 0; trial < totalTrials; trial++) {
            ArrayList<HashMap<String, Object>> trialData = dataInput[trial];
            for (int mouse = 0; mouse < totalMice; mouse++) {
                HashMap<String, Object> mouseData = trialData.get(mouse);
                File curFile = (File) mouseData.get("File");

                //reading the file and saving it to DataTrace
                String dataString = "";
                int c = 0;
                double xData = 0;
                double yData = 0;
                FileReader fReader = null;                      //Reader class : Java class for reading text files (ASCII)
                DataTrace_ver1 series = new DataTrace_ver1();

                if (curFile.exists()) {
                    try {
                        fReader = new FileReader(curFile);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        while ((c = fReader.read()) != -1) {
                            switch (c) {
                                case '\t':
                                    xData = Double.parseDouble(dataString);
                                    dataString = "";
                                    break;
                                case '\n':
                                    yData = Double.parseDouble(dataString);
                                    series.addData(xData, yData);
                                    dataString = "";
                                    break;
                                default:
                                    dataString += (char) c;
                            }
                        }
                        mouseData.put("Position", series);
                    } catch (IOException ex) {
                        Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public File writeFile(String resultName, String output, String s, File out) {
        String measure = resultName + "_" + output + s;
        //resultName + "_" + output + "_maxRm"
        //resultName + "_" + output + "_minRm"
        //resultName + "_" + output + "_Q" + q
        //resultName + "_" + output + "_P" + q
        //resultName + "_RmPlot"
        try {
            //header
            FileWriter outStream = new FileWriter(out, true);
            String toWrite = "";
            //file contents
            for (int trial = 0; trial < totalTrials; trial++) {
                ArrayList<HashMap<String, Object>> trialData = dataInput[trial];
                toWrite = resultName + "\t" + output + "\t" + "T" + trial + "\t" + s + "\t";
                HashMap<String, Object> mouseData = new HashMap<>();
                for (int mouse = 0; mouse < totalMice; mouse++) {
                    mouseData = trialData.get(mouse);
                    float measureValue = (float) mouseData.get(measure);
                    toWrite += measureValue + "\t";
                }
                HashMap<String, Object> aveMouse = trialData.get(totalMice);
                if (aveMouse.get(measure) != null) {
                    toWrite += (float) aveMouse.get(measure);
                }
                toWrite += "\n";
                outStream.write(toWrite);

            }
            outStream.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
        return out;
    }
}
