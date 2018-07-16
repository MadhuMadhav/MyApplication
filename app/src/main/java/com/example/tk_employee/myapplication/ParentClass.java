package com.example.tk_employee.myapplication;

import java.util.ArrayList;

class ParentClass {

    public String pName="";
    public String pImage="";
    public String pRating="";

    private ArrayList<ChildClass> ChildItems;

    public ArrayList<ChildClass> getChildItems() {
        return ChildItems;
    }

    public void setChildItems(ArrayList<ChildClass> childItems) {
        ChildItems = childItems;
    }
}
