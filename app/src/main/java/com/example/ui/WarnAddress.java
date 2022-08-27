package com.example.ui;

import com.example.ui.WarnActivity;

public class WarnAddress {
    private String name;
    private float address_x,address_y;
    private float radius;
    private int grade;
    private boolean use;
    private int id;

    public WarnAddress(int id,String name,float address_x,float address_y,float radius,int grade,boolean use) {
        this.id = id;
        this.name = name;
        this.address_x = address_x;
        this.address_y = address_y;
        this.radius = radius;
        this.grade = grade;
        this.use = use;
    }

    public WarnAddress(String name,float address_x,float address_y,float radius,int grade,boolean use){
        this.name = name;
        this.address_x = address_x;
        this.address_y = address_y;
        this.radius = radius;
        this.grade = grade;
        this.use = use;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getAddress_x() {
        return address_x;
    }

    public float getAddress_y() {
        return address_y;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isUse() {
        return use;
    }

    public int getGrade() {
        return grade;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public void setAddress_x(float address_x) {
        this.address_x = address_x;
    }

    public void setAddress_y(float address_y) {
        this.address_y = address_y;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
