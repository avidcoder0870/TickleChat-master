package com.techpro.chat.ticklechat.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.TechProException;
import com.techpro.chat.ticklechat.models.CustomLocationBean;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static CustomLocationBean getDataFromLocation(Context context, LatLng latLng) throws TechProException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!ValidationUtils.validateObject(addressList)) {
                throw new TechProException(context, context.getString(R.string.invalid_location));
            }
            Address address = addressList.get(0);
            CustomLocationBean customLocationBean = new CustomLocationBean();
            customLocationBean.setVenue(address.getPremises());
            customLocationBean.setCity(address.getLocality());
            customLocationBean.setPinCode(address.getPostalCode());
            customLocationBean.setStreet(address.getSubLocality());
            return customLocationBean;
        } catch (IOException e) {
            throw new TechProException(context, context.getString(R.string.invalid_location));
        }
    }
}
