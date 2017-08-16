
package com.posimplicity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.utils.ToastUtils;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;

import java.util.Set;

public class DeviceListActivity extends Activity {

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothService mService = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private Button scanBtn;
    private ListView pariedListView, newAvailableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list_as_dialog);


        scanBtn = (Button) findViewById(R.id.Activity_Device_List_Btn_Scan);
        pariedListView = (ListView) findViewById(R.id.Activity_Device_List_LV_Paired_Devices);
        newAvailableListView = (ListView) findViewById(R.id.Activity_Device_List_LV_Available_Devices);

        scanBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        pariedListView.setAdapter(mPairedDevicesArrayAdapter);
        pariedListView.setOnItemClickListener(mDeviceClickListener);

        newAvailableListView.setAdapter(mNewDevicesArrayAdapter);
        newAvailableListView.setOnItemClickListener(mDeviceClickListener);


        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mService = new BluetoothService(this, null);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.Activity_Device_List_TV_Paired_Devices_Title).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            mPairedDevicesArrayAdapter.add("No devices have been paired");
        }
    }

    private void doDiscovery() {

        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        findViewById(R.id.Activity_Device_List_TV_Available_Devices_Title).setVisibility(View.VISIBLE);
        if (mService.isDiscovering()) {
            mService.cancelDiscovery();
        }
        mService.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {   //����б�������豸
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mService.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View


            String info = ((TextView) v).getText().toString();
            if (info == null || info.isEmpty() || info.equalsIgnoreCase("No devices have been paired") || info.equalsIgnoreCase("No devices found")) {
                ToastUtils.showOwnToast(DeviceListActivity.this, "No Device Found ! Please Try Again Later.");
            } else {
                unRegisterAll();
                String address = info.substring(info.length() - 17);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        unRegisterAll();
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    private void unRegisterAll() {
        if (mService != null) {
            mService.cancelDiscovery();
        }
        mService = null;
        this.unregisterReceiver(mReceiver);
    }
}
