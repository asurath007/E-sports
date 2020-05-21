package com.art.myknot_gaming.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapList {
    private String name,map,mode,date,time,type,id;
    private String prizeMoney,moneyBreakUp,entryFee;
    private int progress;
    private List<String> players;

    public MapList(String newName, String newMap, String newMode, String newDate, String newTime, String newPrizeMoney, String newEntryFee) {
        this.name = newName;
        this.map = newMap;
        this.mode = newMode;
        this.date = newDate;
        this.time = newTime;
        this.prizeMoney = newPrizeMoney;
//        this.moneyBreakUp = moneyBreakUp;
        this.entryFee = newEntryFee;
//        this.progress = newProgress;
        this.id= id;
    }
    public MapList(String newName, String newMap, String newMode, String newDate, String newTime, String newPrizeMoney, String newEntryFee, String newMoneyBreakUp, String newType, List<String>players) {
        this.name = newName;
        this.map = newMap;
        this.mode = newMode;
        this.date = newDate;
        this.time = newTime;
        this.prizeMoney = newPrizeMoney;
        this.moneyBreakUp = newMoneyBreakUp;
        this.entryFee = newEntryFee;
        this.type = newType;
//        this.progress = newProgress;
        this.id= id;
        this.players=players;
    }
    public MapList(String newName, String newMap, String newMode, String newDate, String newTime, String newPrizeMoney, String newEntryFee, String newMoneyBreakUp, String newType) {
        this.name = newName;
        this.map = newMap;
        this.mode = newMode;
        this.date = newDate;
        this.time = newTime;
        this.prizeMoney = newPrizeMoney;
        this.moneyBreakUp = newMoneyBreakUp;
        this.entryFee = newEntryFee;
        this.type = newType;
//        this.progress = newProgress;
        this.id= id;
    }
    public MapList(String newName, String newMap, String newMode, String newDate, String newTime, String newPrizeMoney, String newEntryFee, String newMoneyBreakUp, String newType, String id) {
        this.name = newName;
        this.map = newMap;
        this.mode = newMode;
        this.date = newDate;
        this.time = newTime;
        this.prizeMoney = newPrizeMoney;
        this.moneyBreakUp = newMoneyBreakUp;
        this.entryFee = newEntryFee;
        this.type = newType;
//        this.progress = newProgress;
        this.id= id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrizeMoney() {
        return prizeMoney;
    }

    public void setPrizeMoney(String prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public String getMoneyBreakUp() {
        return moneyBreakUp;
    }

    public void setMoneyBreakUp(String moneyBreakUp) {
        this.moneyBreakUp = moneyBreakUp;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public MapList(String name, String map, String mode, String date, String time, String prizeMoney, String entryFee, int progress, String id) {
//        this.name = name;
//        this.map = map;
//        this.mode = mode;
//        this.date = date;
//        this.time = time;
//        this.prizeMoney = prizeMoney;
////        this.moneyBreakUp = moneyBreakUp;
//        this.entryFee = entryFee;
//        this.progress = progress;
//        this.id= id;
//    }

    public MapList(){}

    public Map<String, Object> newMatch(){

        HashMap<String,Object> match =  new HashMap<>();
        match.put("name",this.name);
        match.put("map",this.map);
        match.put("mode",this.mode);
        match.put("date",this.date);
        match.put("time",this.time);
        match.put("prizeMoney",this.prizeMoney);
        match.put("moneyBreakUp",this.moneyBreakUp);
        match.put("entryFee",this.entryFee);
        match.put("type",this.type);
        match.put("progress",this.progress);
        match.put("id",this.id);
        match.put("players","");

        return match;
    }
}
