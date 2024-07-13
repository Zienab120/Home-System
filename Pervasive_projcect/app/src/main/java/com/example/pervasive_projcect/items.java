package com.example.pervasive_projcect;

public class items {

    String title="";

    public items(String title, int image) {
        this.title = title;
        this.image = image;
    }

    int image=0;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
