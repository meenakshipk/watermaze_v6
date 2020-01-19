/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wmsoftware;

/**
 *
 * @author Meenakshi 03092019
 */
public class Mouse {

    private int ID; //mouse ID;
    private int groupID;      // undefined = 0, probe day 3 = 1, probe day 5 = 2, probe day 7 = 3.
    private int geneticBackground;      // undefined = 0, wild type = 1, transgenic = 2.
    
    public Mouse() {
    }

    public int getID() {
        return ID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getGenBg() {
        return geneticBackground;
    }

    public void setID(int num) {
        ID = num;
    }

    public void setGroupID(int num) {
        groupID = num;
    }

    public void setGenBg(int num) {
        geneticBackground = num;
    }
}
