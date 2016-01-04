//package com.chinajsbn.library.share;
//
//import android.app.Activity;
//import android.view.View;
//import android.widget.Button;
//
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.media.UMImage;
//
///**
// * Created by CQYIEN on 2015/4/20.
// */
//public class UMengShare {
//
//    // 首先在您的Activity中添加如下成员变量
//    static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//
//    private static final String appID = "wx967daebe835fbeac";
//    private static final String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";
//
//    public static void share(final Activity activity, String url, Button button){
//        // 设置分享内容
//        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
//        // 设置分享图片, 参数2为图片的url地址
//        mController.setShareMedia(new UMImage(activity,
//                "http://www.umeng.com/images/pic/yinyt.png"));
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, "100424468",
//                "c7394704798a158208a74ab60104f0ba");
//        qZoneSsoHandler.addToSocialSDK();
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(activity,appID,appSecret);
//        wxHandler.addToSocialSDK();
//        // 添加微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(activity,appID,appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//        //设置新浪SSO handler
//        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
//        sinaSsoHandler.addToSocialSDK();
//        mController.getConfig().setSsoHandler(sinaSsoHandler);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 是否只有已登录用户才能打开分享选择页
//                mController.openShare(activity, false);
//            }
//        });
//
//    }
//}
