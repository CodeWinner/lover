package com.example.lovedays.main.units;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeLine implements Comparable<TimeLine>{
    private String id;
    private String date;
    private String subject;
    private String content;
    private String status;
    private String type;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String countDate;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }

    @Override
    public int compareTo(TimeLine timeLine) {
        try {
            return sdf.parse(this.getDate()).before(sdf.parse(timeLine.getDate())) ? -1 : 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
