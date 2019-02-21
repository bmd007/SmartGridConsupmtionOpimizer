package ir.tiroon.schedulingApp.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orm.query.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.tiroon.schedulingApp.Adapter.DeviceListAdapter;
import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.Model.Device;
import ir.tiroon.schedulingApp.Model.UsageRecord;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import schedulingApp.tiroon.ir.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {

    public DeviceListFragment() {
        // Required empty public constructor
    }

    public static boolean needRefresh = false;

    public static DeviceListFragment newInstance(int param) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putInt("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView deviceListRecycleView;
    DeviceListAdapter deviceListAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list_fragment_layout, container, false);

        deviceListRecycleView = (RecyclerView) view.findViewById(R.id.device_list_recycler_view);

        Integer spanCount = getActivity().getResources().getInteger(R.integer.numberOfColumnsInTableReserveRecyclerView);

        deviceListRecycleView.setLayoutManager(
                new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));

        deviceListAdapter = new DeviceListAdapter(Select.from(Device.class).list(), getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.deviceListRefreshView);

        if (needRefresh)refreshDeviceList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDeviceList();
            }
        });

        deviceListRecycleView.setAdapter(deviceListAdapter);

        return view;
    }



    public void dataSetChanged(){
        deviceListAdapter.devices = Select.from(Device.class).list();
        deviceListAdapter.notifyDataSetChanged();
    }


    public void refreshDeviceList() {
        new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    return LocalSchedulerWebServicesCallUtil.deviceList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                System.out.println("BMD::>"+response);

                ObjectMapper om = new ObjectMapper();
                List<UsageRecord> usageRecords = null;
                try {
                    usageRecords = om.readValue(response, om.getTypeFactory().constructCollectionType(List.class, UsageRecord.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //
                UsageRecord.deleteAll(UsageRecord.class);
                Device.deleteAll(Device.class);
                //

                for (UsageRecord ur : usageRecords) {
                    System.out.println("BMD saved:"+ur);
                    ur.getDevice().save();
                    ur.save();
                }
                dataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }


}
