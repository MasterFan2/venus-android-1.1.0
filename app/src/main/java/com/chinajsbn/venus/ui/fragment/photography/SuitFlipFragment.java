package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.WeddingSuit;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.photography.SuitDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;

/**
 */
@FragmentFeature(layout = R.layout.item_suit_layout)
public class SuitFlipFragment extends BaseFragment implements OnClickListener {

    @ViewInject(R.id.item_suit_name_txt)
    private TextView suitNameTxt;

    @ViewInject(R.id.item_suit_money_txt)
    private TextView moneyTxt;

    @ViewInject(R.id.item_suit_order_money_txt)
    private TextView orderTxt;

    @ViewInject(R.id.item_suit_img)
    private ImageView contentImg;

    @ViewInject(R.id.item_suit_rush_buy_btn)
    private Button rushBuyBtn;

    @ViewInject(R.id.item_suit_select_photographer_stylist_txt)
    private TextView photographerStylistTxt;

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    private WeddingSuit suit;
    private String suitID;

    @OnClick(R.id.item_suit_rush_buy_btn)
    public void itemClick(View view){
        if(NetworkUtil.hasConnection(getActivity())){
            Intent intent = new Intent(getActivity(), SuitDetailActivity.class);
            intent.putExtra("suitId", suitID);
            intent.putExtra("detailId", suit.getWeddingDressSuitId());
            intent.putExtra("url", suit.getImageUrl());
            animStart(intent);
        }else{
            handler.sendEmptyMessageDelayed(10, 100);
            handler.sendEmptyMessageDelayed(11, 4000);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10://show
                    networkDialog.show();
                    break;
                case 11://close
                    if (networkDialog != null && networkDialog.isShowing()) networkDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void initialize() {

        ///无网络提示///
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        networkDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(SuitFlipFragment.this)
                .create();

        suit = (WeddingSuit) getArguments().getSerializable("suit");
        suitID = (String) getArguments().getSerializable("suitId");
        moneyTxt.setText("￥ " + suit.getPrice());
        orderTxt.setText("定金 ￥" + suit.getDepositRate());
        suitNameTxt.setText(suit.getProductName());

        String str = "";
        if(suit.getIsOptionalStylist() == 1){
            str = "可自选造型师";
        }

        if(suit.getIsOptionalCameraman() == 1){
            if(TextUtils.isEmpty(str)){
                str = "可自选摄影师";
            }else {
                str += "/摄影师";
            }
        }
        if(TextUtils.isEmpty(str)){
            photographerStylistTxt.setText("(不可自选摄影师/造型师)");
        }else{
            photographerStylistTxt.setText("(" + str +")");
        }
        if(!TextUtils.isEmpty(suit.getImageUrl()))
            Picasso.with(getActivity()).load(suit.getImageUrl()+ "@" + DimenUtil.screenWidth +"w_" + (DimenUtil.screenWidth *2) +"h_60Q").into(contentImg);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        dialog.dismiss();
    }
}
