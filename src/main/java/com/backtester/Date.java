package com.backtester;

public class Date implements Comparable<Date> {
    public int day;
    public int month;
    public int year;

    public Date(String date) {
        // expects format "MM/DD/YYYY"
        this.month = Integer.parseInt(date.substring(0, 2));
        this.day   = Integer.parseInt(date.substring(3, 5));
        this.year  = Integer.parseInt(date.substring(6));
    }

    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", month, day, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Date other)) return false;
        return this.day == other.day &&
                this.month == other.month &&
                this.year == other.year;
    }

    @Override
    public int hashCode() {
        return day + 31 * month + 372 * year;
    }
}
