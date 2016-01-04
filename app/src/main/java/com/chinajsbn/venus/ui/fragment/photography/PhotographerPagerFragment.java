/*
 * Copyright (C) 2014 Pixplicity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chinajsbn.venus.ui.fragment.photography;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

/**
 * 选摄影师头部
 */
@FragmentFeature(layout = R.layout.item_daily_custom)
public class PhotographerPagerFragment extends BaseFragment {

    @ViewInject(R.id.pager_img)
    private ImageView headImg;

    @ViewInject(R.id.pager_name_txt)
    private TextView nameTxt;

    @ViewInject(R.id.pager_place_txt)
    private TextView placeTxt;

    //    @OnClick(R.id.pager_schedule_btn)
    public void click(View view) {
        T.s(getActivity(), "Hello");
    }

    @Override
    public void initialize() {

        Simple simple = (Simple) getArguments().getSerializable("data");

        if (simple != null) {
            if (!TextUtils.isEmpty(simple.getContentUrl())) {
                Picasso.with(getActivity()).load(simple.getContentUrl()).into(headImg);
            }
            nameTxt.setText(simple.getActorNameFemale() + "&" + simple.getActorNameFemale());
            placeTxt.setText("拍摄地点:" + simple.getCreateDate() + " 点击量:12305");

        }
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
