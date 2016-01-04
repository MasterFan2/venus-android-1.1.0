package com.chinajsbn.venus.net.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.T;
import com.chinajsbn.venus.utils.entity.NetInfo;

/**
 * Created by 13510 on 2015/10/26.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetInfo netInfo = NetworkUtil.getNetworkInfo(context);
            if(netInfo.getType() == NetInfo.NetType.T_NO_CONN) {
                T.s(context, "当前无网络连接! 请检查您的网络");
            }else if(netInfo.getType() == NetInfo.NetType.T_2G) {
                T.s(context, "当前网络切换为2G,请注意您的流量");
            }else if(netInfo.getType() == NetInfo.NetType.T_3G) {
                T.s(context, "当前网络切换为3G,请注意您的流量");
            }else if(netInfo.getType() == NetInfo.NetType.T_4G) {
                T.s(context, "当前网络切换为4G,请注意您的流量");
            }else if(netInfo.getType() == NetInfo.NetType.T_WIFI) {
                T.s(context, "当前网络切换为WIFI!");
            }else if(netInfo.getType() == NetInfo.NetType.T_WAP) {
                T.s(context, "当前网络切换为wap!");
            }

        }
    }
}
