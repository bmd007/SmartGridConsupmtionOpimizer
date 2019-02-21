package ir.tiroon.schedulingApp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.tiroon.schedulingApp.DeviceShowActivity;
import ir.tiroon.schedulingApp.Model.Device;
import ir.tiroon.schedulingApp.Util.MyUtil;
import schedulingApp.tiroon.ir.R;

/**
 * Created by Lenovo on 03/08/2016.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {

    public List<Device> devices;
    Activity context;

    public DeviceListAdapter(List<Device> devices, Activity c) {
        this.devices = devices;
        context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item_layout, parent, false);
        return new MyViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Device device = devices.get(position);

        holder.deviceName.setText(device.getName());

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView deviceName;

        public MyViewHolder(View itemView, int type) {
            super(itemView);

            deviceName = (TextView) itemView.findViewById(R.id.device_name_is_list);

            deviceName.setTypeface(MyUtil.fonts[3]);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putLong("deviceId",devices.get(getAdapterPosition()).getId());
            Intent intent = new Intent(context,DeviceShowActivity.class);
            intent.putExtras(bundle);
            context.startActivityForResult(intent, 1001);
        }

    }
}
