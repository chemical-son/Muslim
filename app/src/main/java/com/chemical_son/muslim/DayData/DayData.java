package com.chemical_son.muslim.DayData;

public class DayData {
    public String readable;
    public String timestamp;
    public Hijri hijri;
    public Gregorian gregorian;

    public String getReadable() {
        return readable;
    }

    public void setReadable(String readable) {
        this.readable = readable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Hijri getHijri() {
        return hijri;
    }

    public void setHijri(Hijri hijri) {
        this.hijri = hijri;
    }

    public Gregorian getGregorian() {
        return gregorian;
    }

    public void setGregorian(Gregorian gregorian) {
        this.gregorian = gregorian;
    }
}
