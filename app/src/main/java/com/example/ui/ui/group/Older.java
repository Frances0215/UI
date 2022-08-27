package com.example.ui.ui.group;

import android.graphics.Bitmap;

public class Older {
    private String id;
    private String name;
    private String age;
    private String telephone;
    private String sex;
    private String birthtime;
    private Bitmap photo;
    public Older(){}

    public Older(String id, String name, String age, String telephone, String sex, String birthtime,Bitmap photo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthtime = birthtime;
        this.photo = photo;
        this.sex = sex;
        this.telephone = telephone;
    }

    public String getId(){return id;}

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
    public String getTelephone() {
        return telephone;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public Bitmap getPhoto() {
        return photo;
    }
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
    @Override
    public String toString() {
        return "Older{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", telephone=" + telephone +
                ", sex='" + sex + '\'' +
                ", birthtime='" + birthtime + '\'' +
                '}';
    }
}
