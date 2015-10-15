package com.itlao.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.R.string;

public class Clazz{

    public int id;
    public String  name;
    //public String[]  tyles=new String[100];
    public static ArrayList<String> tyleslist=new ArrayList<String>();
    public static Map<String,Integer> tylesmap=new HashMap();//储存name,id

    public Clazz() {
    }

    public Clazz(int id, String  name) {

        this.id = id;
        this.name = name;

    }
}
