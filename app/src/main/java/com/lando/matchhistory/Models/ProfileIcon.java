package com.lando.matchhistory.Models;

import io.realm.RealmObject;

/**
 * Created by Lando on 1/28/2015.
 */
public class ProfileIcon extends RealmObject {

    private long id;
    private Image image;

    public ProfileIcon() {
    }

    public ProfileIcon(long id, Image image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
