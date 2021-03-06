package com.devforxkill.androidcrowdfunding.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String id;
    String email;
    String pseudo;

    /* ----- ID ----- */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /* ----- EMAIL ----- */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* ----- PSEUDO ----- */
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }


    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        pseudo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(pseudo);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
