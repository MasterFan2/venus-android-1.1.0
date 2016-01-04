package com.chinajsbn.venus.ui.personalcenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.common.TextView;

@ActivityFeature(layout = R.layout.activity_personal_center)
public class PersonalCenterActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.listView)
    private ListView listView;

    @Override
    public void initialize() {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_center_header, null);
        listView.addHeaderView(view);
        listView.setAdapter(new PersonalCenterAdapter());
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view){
        animFinish();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    class PersonalCenterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 14;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 2 ? 2 : (position % 5 == 0 ) ? 0 : 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderPrefix holderPrefix = null;
            ViewHolderNormal holderNormal = null;
            ViewHolderPhoto  holderPhoto  = null;
            int type = getItemViewType(position);

            if(convertView == null){
                //initialize
                if(type == 0){
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_center_prefix, null, false);
                    holderPrefix = new ViewHolderPrefix();
//                    ViewUtils.inject(holderPrefix, convertView);
                    holderPrefix.gridView  = (GridView) convertView.findViewById(R.id.item_personal_prefix_gridView);
                    holderPrefix.prefixTxt = (TextView) convertView.findViewById(R.id.item_personal_prefix_tag_txt);
                    holderPrefix.titleTxt  = (TextView) convertView.findViewById(R.id.item_personal_prefix_title_txt);

                    convertView.setTag(holderPrefix);
                }else if(type == 1){
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_center_normal, null, false);
                    holderNormal = new ViewHolderNormal();
//                    ViewUtils.inject(holderNormal, convertView);
                    holderNormal.gridView = (GridView) convertView.findViewById(R.id.item_personal_normal_gridView);
//                    holderNormal.titleTxt = (TextView) convertView.findViewById(R.id.item_personal_normal_title_txt);
                    convertView.setTag(holderNormal);
                }else if(type == 2){
                    holderPhoto = new ViewHolderPhoto();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_photography, null, false);
                    convertView.setTag(holderPhoto);
                }
            }else {
                if(type == 0){
                    holderPrefix = (ViewHolderPrefix) convertView.getTag();
                }else if(type == 1){
                    holderNormal = (ViewHolderNormal) convertView.getTag();
                }else if(type == 2){
                    holderPhoto = (ViewHolderPhoto) convertView.getTag();
                }
            }


            // bind data
            if(type == 0){
                holderPrefix.gridView.setAdapter(new GridViewAdapter());
            }else if(type == 1){
                holderNormal.gridView.setAdapter(new GridViewAdapter());
            }else if(type == 2){
                //
                if(holderPhoto != null){

                }
            }

            //return
            return convertView;
        }

        class GridViewAdapter extends BaseAdapter {

//            private boolean isPrefix;
//
//            public GridViewAdapter(boolean isPre){
//                this.isPrefix = isPre;
//            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                final ViewHolder viewHolder;
                if(view == null){
                    view = LayoutInflater.from(context).inflate(R.layout.item_persional_gridview, viewGroup, false);
                    viewHolder = new ViewHolder();
                    //ViewUtils.inject(viewHolder, view);
                    view.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                return view;
            }

            class ViewHolder {
//                @ViewInject(R.id.item_personal_grid_img)
                ImageView img;

//                @ViewInject(R.id.item_personal_grid_txt)
                TextView  txt;
            }
        }

        class ViewHolderPrefix {
//            @ViewInject(R.id.item_personal_prefix_txt)
            TextView prefixTxt;

//            @ViewInject(R.id.item_personal_prefix_txt)
            TextView titleTxt;

//            @ViewInject(R.id.item_personal_prefix_txt)
            GridView gridView;
        }

        class ViewHolderNormal {
//            @ViewInject(R.id.item_personal_normal_title_txt)
            TextView titleTxt;

//            @ViewInject(R.id.item_personal_normal_gridView)
            GridView gridView;
        }

        class ViewHolderPhoto {

        }
    }
}
