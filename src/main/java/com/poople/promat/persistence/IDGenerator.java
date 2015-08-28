package com.poople.promat.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum IDGenerator{
    INSTANCE;
    private static final String pattern = "mssSSS";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    public long getUUID(){
        String UUID = formatter.format(new Date());
        long uuid;
        try{
            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
            }
            uuid = Long.parseLong(UUID);
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
            uuid = System.currentTimeMillis();
        }
        return uuid;
    }

}