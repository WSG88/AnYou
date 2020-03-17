package com.anyou.yx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anyou.yx.fragment.FragmentA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
  FragmentA fa1, fa2, fa3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fa1 = FragmentA.newInstance("111", "11111");
    fa2 = FragmentA.newInstance("222", "22222");
    fa3 = FragmentA.newInstance("333", "33333");

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction
        .add(R.id.fragment_container1, fa1, "fa1")
        .add(R.id.fragment_container2, fa2, "fa2")
        .add(R.id.fragment_container3, fa3, "fa3")
        .hide(fa2)
        .hide(fa3)
        .commit();

    findViewById(R.id.addTv)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
              }
            });
    findViewById(R.id.seeTv)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SeeActivity.class));
              }
            });
  }

  public void btnClick1(View v) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(fa1).hide(fa2).hide(fa3).show(fa1).commit();
  }

  public void btnClick2(View v) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(fa1).hide(fa2).hide(fa3).show(fa2).commit();
  }

  public void btnClick3(View v) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(fa1).hide(fa2).hide(fa3).show(fa3).commit();
  }
}
