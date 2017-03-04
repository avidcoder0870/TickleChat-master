package com.techpro.chat.ticklechat.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.techpro.chat.ticklechat.R;

import lombok.Getter;
import lombok.Setter;

public class TickleFriend {

    private String id;
    private String name;
    private String number;
    private Bitmap image;

    public  TickleFriend(String id, String name, String number,Bitmap image) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public  TickleFriend(String id, String name, String number) {
        new TickleFriend(id,name,number,null);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
