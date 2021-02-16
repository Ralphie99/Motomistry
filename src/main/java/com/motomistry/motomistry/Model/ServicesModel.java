package com.motomistry.motomistry.Model;

/**
 * Created by Nikhil on 02-Apr-18.
 */

public class ServicesModel {
    private int ID;
    private String Text;
    private String DISCREPTION;

    public ServicesModel(int ID, String text, String DISCREPTION) {
        this.ID = ID;
        Text = text;
        this.DISCREPTION = DISCREPTION;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getDISCREPTION() {
        return DISCREPTION;
    }

    public void setDISCREPTION(String DISCREPTION) {
        this.DISCREPTION = DISCREPTION;
    }
}
