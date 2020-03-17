package com.anyou.yx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.FileProvider;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MapActivity extends com.tencent.tencentmap.mapsdk.map.MapActivity implements
        TencentLocationListener {

    String imageFileName;
    LatLng latLngLocation;
    TencentLocation tencentLocation;
    private static final int TAKE_PHONE = 20;
    private TencentMap tencentMap;
    private Marker mLocationMarker;
    private TencentLocationManager mLocationManager;


    EditText myEdt, nameEdt, markEdt, imageEdt;

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
        myEdt = findViewById(R.id.myEdt);
        nameEdt = findViewById(R.id.nameEdt);
        markEdt = findViewById(R.id.markEdt);
        imageEdt = findViewById(R.id.imageEdt);
        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myLoc = myEdt.getText().toString();
                String name = nameEdt.getText().toString();
                String mark = markEdt.getText().toString();
                String image = imageEdt.getText().toString();
                if (tencentLocation == null || latLngLocation == null || TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(myLoc)) {
                    System.out.println("数据不全");
                    return;
                }
                StoreLocation storeLocation = new StoreLocation();
                storeLocation.id = System.currentTimeMillis();
                storeLocation.lng = latLngLocation.getLongitude();
                storeLocation.lat = latLngLocation.getLatitude();
                storeLocation.address = myLoc;
                storeLocation.name = name;
                storeLocation.image = image;
                storeLocation.mark = mark;
                System.out.println(storeLocation.toString());

                App.getApplication().insertDB(storeLocation);
                System.out.println("记录成功");
            }
        });
        findViewById(R.id.imageClearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageEdt.setText("");
            }
        });
        findViewById(R.id.imageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        findViewById(R.id.markClearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markEdt.setText("");
            }
        });
        findViewById(R.id.nameClearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdt.setText("");
            }
        });
        findViewById(R.id.myClearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEdt.setText("");
            }
        });
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

            System.out.println("定位成功");

            tencentLocation = location;

            latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
            tencentMap.animateTo(latLngLocation);
            if (mLocationMarker == null) {
                mLocationMarker =
                        tencentMap.addMarker(new MarkerOptions().
                                position(latLngLocation).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_location)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
            }

            myEdt.setText(location.getAddress());

            stopLocation();
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

    private void takePhoto() {
        File outputImage = new File(getExternalCacheDir(), new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(MapActivity.this, "com.anyou.yx.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        imageFileName = imageUri.getPath();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHONE && resultCode == RESULT_OK) {
            imageEdt.setText(imageFileName);
        }
    }

}
