package com.tool.unit;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by chenjianjun on 15-1-19.
 * <p/>
 * Beyond their own, let the future
 */
public class OnDoubleClickListenerImpl {

    public interface OnDoubleClickListener {
        // 单击事件
        public void OnSingleClick(View v);
        // 双击事件
        public void OnDoubleClick(View v);
    }

    public static void registerDoubleClickListener(View view, final OnDoubleClickListener listener) {

        if(listener==null) return;

        view.setOnClickListener(new View.OnClickListener() {
            private static final int DOUBLE_CLICK_TIME = 350;//双击间隔时间350毫秒
            private boolean waitDouble = true;

            private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    listener.OnSingleClick((View)msg.obj);
                }

            };

            //等待双击
            public void onClick(final View v) {
                if(waitDouble){
                    waitDouble = false;        //与执行双击事件
                    new Thread(){

                        public void run() {
                            try {
                                Thread.sleep(DOUBLE_CLICK_TIME);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }    //等待双击时间，否则执行单击事件
                            if(!waitDouble){
                                //如果过了等待事件还是预执行双击状态，则视为单击
                                waitDouble = true;
                                Message msg = handler.obtainMessage();
                                msg.obj = v;
                                handler.sendMessage(msg);
                            }
                        }

                    }.start();
                }else{
                    waitDouble = true;
                    listener.OnDoubleClick(v);    //执行双击
                }
            }
        });
    }
}
