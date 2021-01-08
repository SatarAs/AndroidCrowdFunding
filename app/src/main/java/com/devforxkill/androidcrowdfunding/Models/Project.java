package com.devforxkill.androidcrowdfunding.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {

    String id;
    String title;
    String picture;
    String description;
    String montant;
    String endDate;
    String idUser;

    /* ----- ID ----- */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /* ----- TITLE ----- */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /* ----- PICTURE ----- */
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    /* ----- DESCRIPTION ----- */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* ----- MONTANT ----- */
    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    /* ----- DATE ----- */
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /* ----- ID_USER ----- */
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    /* ----- CREATOR ----- */
    public static Creator<Project> getCREATOR() {
        return CREATOR;
    }

    protected Project(Parcel in) {
        id = in.readString();
    }


    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeString(montant);
        dest.writeString(endDate);
        dest.writeString(idUser);
    }
}
