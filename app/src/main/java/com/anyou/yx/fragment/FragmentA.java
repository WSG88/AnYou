package com.anyou.yx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyou.yx.R;

import androidx.fragment.app.Fragment;

public class FragmentA extends Fragment {
  private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2";
  private String mParam1 = "", mParam2 = "";

  public FragmentA() {}

  public static FragmentA newInstance(String param1, String param2) {
    FragmentA fragment = new FragmentA();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
      System.out.println("mParam1 " + mParam1);
      System.out.println("mParam2 " + mParam2);
    }
  }

  TextView tv;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_a, container, false);
    tv = view.findViewById(R.id.tv);
    tv.setText(mParam2 + " ");
    return view;
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    System.out.println(mParam2 + " " + hidden);
  }
}
