package com.anyou.yx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        fileName = Environment.getExternalStorageDirectory().toString() + File.separator + "OCR/image/" + name + ".jpg";
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap b = BitmapFactory.decodeFile(fileName);
            File myCaptureFile = new File(fileName);
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (!myCaptureFile.getParentFile().exists()) {
                        myCaptureFile.getParentFile().mkdirs();
                    }
                    BufferedOutputStream bos;
                    bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    b.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                    bos.flush();
                    bos.close();
                } else {
                    Toast toast = Toast.makeText(this, "保存失败，SD卡无效", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }

}