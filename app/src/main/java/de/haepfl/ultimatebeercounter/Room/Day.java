package de.haepfl.ultimatebeercounter.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "day_table")
public class Day {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int second;

    public Day(int year, int month, int day, int hour, int min, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.second = second;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSecond() {
        return second;
    }
}
