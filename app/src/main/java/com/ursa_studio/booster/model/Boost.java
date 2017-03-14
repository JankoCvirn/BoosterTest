package com.ursa_studio.booster.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Booster
 * com.ursa_studio.booster.model
 * Created by janko on 13.03.17..
 * Description:
 * Usage:
 */

public class Boost implements Parcelable {

  private Double lat;
  private Double lon;
  private String deliverTime;
  private String fuelType;
  private String paymentType;

  public String getDeliverTime (){
    return deliverTime;
  }
  public void setDeliverTime (String deliverTime){
    this.deliverTime = deliverTime;
  }
  public String getFuelType (){
    return fuelType;
  }
  public void setFuelType (String fuelType){
    this.fuelType = fuelType;
  }
  public String getPaymentType (){
    return paymentType;
  }
  public void setPaymentType (String paymentType){
    this.paymentType = paymentType;
  }
  public Double getLat (){
    return lat;
  }
  public void setLat (Double lat){
    this.lat = lat;
  }
  public Double getLon (){
    return lon;
  }
  public void setLon (Double lon){
    this.lon = lon;
  }

  public Boost (){
  }

  @Override public int describeContents (){
    return 0;
  }
  @Override public void writeToParcel (Parcel dest, int flags){
    dest.writeValue(this.lat);
    dest.writeValue(this.lon);
    dest.writeString(this.deliverTime);
    dest.writeString(this.fuelType);
    dest.writeString(this.paymentType);
  }
  protected Boost (Parcel in){
    this.lat = (Double) in.readValue(Double.class.getClassLoader());
    this.lon = (Double) in.readValue(Double.class.getClassLoader());
    this.deliverTime = in.readString();
    this.fuelType = in.readString();
    this.paymentType = in.readString();
  }
  public static final Creator<Boost> CREATOR = new Creator<Boost>() {
    @Override public Boost createFromParcel (Parcel source){
      return new Boost(source);
    }
    @Override public Boost[] newArray (int size){
      return new Boost[size];
    }
  };
}
