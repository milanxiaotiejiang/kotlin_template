package com.thirtydays.kotlin.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.ArraySet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/8
 * desc   :
 *
 * </pre>
 */
public final class BluetoothUtils {

    public static void enableBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static boolean isSupportBle(Context context) {
        if (context == null || !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            return manager.getAdapter() != null;
        }
        return false;
    }

    public static boolean isBleEnable(Context context) {
        if (!isSupportBle(context)) {
            return false;
        }
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            return manager.getAdapter().isEnabled();
        }
        return false;
    }

    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        BluetoothManager manager = context == null ? null :
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (manager == null) {
            return BluetoothAdapter.getDefaultAdapter();
        } else {
            return manager.getAdapter();
        }
    }

    @Nullable
    public static BluetoothDevice findBluetoothDevice(@NonNull BluetoothAdapter adapter,
                                                      @NonNull String address) {
        if (address.length() == 0) {
            return null;
        }

        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return null;
        }

        return adapter.getRemoteDevice(address);
    }

    /**
     * 获取已连接的蓝牙设备
     */
    public static Set<BluetoothDevice> getBluetoothDeviceConnected(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> bluetoothDevices = new ArraySet<>();
        //获取BluetoothAdapter的Class对象
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
        try {
            //反射获取蓝牙连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开使用这个方法的权限
            method.setAccessible(true);
            int state = (int) method.invoke(bluetoothAdapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                //获取在系统蓝牙的配对列表中的设备，已连接设备包含在其中
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    isConnectedMethod.setAccessible(true);

                    //电量反射
//                    Method batteryMethod = BluetoothDevice.class.getDeclaredMethod("getBatteryLevel", (Class[]) null);
//                    batteryMethod.setAccessible(true);
//                    int level = (int) batteryMethod.invoke(device, (Object[]) null);

                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (device != null && isConnected) {
                        bluetoothDevices.add(device);
                    }
                }
            } else {
                XLog.e("no find");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bluetoothDevices;
    }

    /**
     * 获取已连接的蓝牙设备的电量
     */
    public static int getBluetoothDeviceBattery(BluetoothDevice bluetoothDevice) {
        int level = 0;
        try {
            Method batteryMethod = BluetoothDevice.class.getDeclaredMethod("getBatteryLevel", (Class[]) null);

            batteryMethod.setAccessible(true);
            level = (int) batteryMethod.invoke(bluetoothDevice, (Object[]) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return level;
    }

    public static void getBluetoothDeviceConnected2(Context context, BluetoothAdapter bluetoothAdapter) {

        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        int flag = -1;
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }

        if (flag != -1) {
            bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {

                @Override
                public void onServiceDisconnected(int profile) {

                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        for (BluetoothDevice device : mDevices) {
                            XLog.e(" name : " + device.getName() + " type : " + device.getType() + "  uuid : " + device.getAddress());
                        }
                    } else {
                        XLog.e("no find");
                    }
                }
            }, flag);
        }
    }
}
