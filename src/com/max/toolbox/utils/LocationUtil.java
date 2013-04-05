
package com.max.toolbox.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * @���� LocationUtil
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class LocationUtil {

    /**
     * ��ȡ��γ��
     * 
     * @return ��γ�����飬[0]���ȣ�[1]γ��
     */
    public double[] getLocation(Context mContext) {
        double[] loc = {
                0.0, 0.0
        };
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                loc[0] = location.getLatitude();// ����
                loc[1] = location.getLongitude();// γ��
            }
        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider��״̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider��enableʱ�����˺���������GPS����
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider��disableʱ�����˺���������GPS���ر�
                @Override
                public void onProviderDisabled(String provider) {

                }

                // ������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Location changed", "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                loc[0] = location.getLatitude();// ����
                loc[1] = location.getLongitude();// γ��
            }
        }
        return loc;
    }

}
