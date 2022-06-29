package com.example.taxiapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapUtil {

    private Geocoder geocoder = null;

    public Address getAddress(LatLng position) {
        Address address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

}
