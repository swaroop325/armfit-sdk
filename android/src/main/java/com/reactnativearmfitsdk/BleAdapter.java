package com.reactnativearmfitsdk;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BleAdapter extends BaseAdapter {
  Context context;
  public List<Bluetooth> deviceList;

//    private onBleChooseListener mListener;

  public BleAdapter(Context context, List<Bluetooth> deviceList) {
    this.context = context;
    this.deviceList = deviceList;
  }

  //返回要显示的数量
  @Override
  public int getCount() {
    return deviceList.size();
  }
  //返回当前Item显示的数据
  @Override
  public Bluetooth getItem(int position) {
    return deviceList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int p, View v, ViewGroup parent) {
    ViewHolder vh;

    if (v == null) {
      vh = new ViewHolder();
//            vh.layout = v.findViewById(R.id.layout_er1);
//            vh.layout.setOnClickListener(mListener);

      v.setTag(vh);
    } else {
      vh = (ViewHolder) v.getTag();
    }

    Bluetooth b = deviceList.get(p);
//        String name = deviceList.get(p).getName();
//        String sn = name.substring(name.length()-4);
    vh.name.setText(b.getName());
    vh.rssi.setText(b.getRssi() + " dbm");
//        vh.name.setText(b.getName() + " => " + b.getMacAddr());
//        vh.layout.setTag(b);

    return v;
  }


//    public static abstract class onBleChooseListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Log.d("SCAN_FRAGMENT", ((Bluetooth) v.getTag()).getName());
//            onMyClick((Bluetooth) v.getTag(), v);
//        }
//
//        public abstract void onMyClick(Bluetooth b, View v);
//    }
//
//    public void setOnBleChooseListener(onBleChooseListener listener) {
//        mListener = listener;
//    }

  class ViewHolder {
    //        private ConstraintLayout layout;
    private TextView name;
    private TextView rssi;
  }
}
