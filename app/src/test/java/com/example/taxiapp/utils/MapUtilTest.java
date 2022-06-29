package com.example.taxiapp.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.taxiapp.MapsActivity;
import com.example.taxiapp.utils.MapUtil;
import com.google.android.gms.maps.model.LatLng;

import net.bytebuddy.asm.Advice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapUtilTest {

    public static final LatLng coordinates = new LatLng(59.973154, 30.300667);
    public static final String addressString = "Petrogradsky District, St Petersburg, 197022";
    public List<Address> addressList = new ArrayList<>();


    @Test
    public void getAddress() throws IOException {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getAddressLine(0)).thenReturn(addressString);
        addressList.add(address);
        Geocoder geocoder = Mockito.spy(new Geocoder(null));
        doReturn(addressList).when(geocoder).getFromLocation(coordinates.latitude, coordinates.longitude, 1);
        System.out.println(geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1));
        MapUtil mapUtil = new MapUtil();
        mapUtil.setGeocoder(geocoder);
        assert mapUtil.getAddress(coordinates).getAddressLine(0).equals(addressString);
    }

}