package com.anyou.yx;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;


public class MapActivity extends com.tencent.tencentmap.mapsdk.map.MapActivity implements
        TencentLocationListener {

    private TextView textView;
    private TencentMap tencentMap;
    private Marker mLocationMarker;
    private TencentLocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION, //target为Q时，动态请求后台定位权限
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE //target为Q时，可以移除该权限申请
            };
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissions, 0);
            }
        }
        textView = findViewById(R.id.myTv);

        findViewById(R.id.myBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocation();
            }
        });

        MapView mMapView = findViewById(R.id.mapviewOverlay);
        tencentMap = mMapView.getMap();
        tencentMap.setZoom(16);

        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        if (error == TencentLocation.ERROR_OK) {
            stopLocation();

            System.out.println("定位成功");

            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
            tencentMap.animateTo(latLngLocation);
            if (mLocationMarker == null) {
                mLocationMarker =
                        tencentMap.addMarker(new MarkerOptions().
                                position(latLngLocation).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_location)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
            }

            textView.setText(location.getAddress());

            StoreLocation storeLocation = new StoreLocation();
            storeLocation.id = System.currentTimeMillis();
            storeLocation.name = "";
            storeLocation.address = location.getProvince() + location.getCity() + location.getDistrict() + location.getAddress();
            storeLocation.image = "";
            storeLocation.mark = "";
            storeLocation.lng = latLngLocation.getLongitude();
            storeLocation.lat = latLngLocation.getLatitude();
            System.out.println(storeLocation.toString());
            App.getApplication().insertDB(storeLocation);

        }
    }


    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
    }

    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        mLocationManager.requestLocationUpdates(request, this);
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

}
