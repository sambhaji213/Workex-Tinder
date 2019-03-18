package com.sk.workextinder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String country;
    private String countryCode;
    private String state;
    private String city;
    private String street;
    private String zip;
    private Geo geo;

    protected Address(Parcel in) {
        country = in.readString();
        countryCode = in.readString();
        state = in.readString();
        city = in.readString();
        street = in.readString();
        zip = in.readString();
        geo = in.readParcelable(Geo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(countryCode);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(street);
        dest.writeString(zip);
        dest.writeParcelable(geo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }
}
