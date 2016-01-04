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

import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

@FragmentFeature(layout = R.layout.fragment_page)
public class PageFragment extends BaseFragment {

    @ViewInject(R.id.page_img)
    private ImageView pageImg;

    @ViewInject(R.id.page_txt)
    private TextView pageTxt;

    @Override
    public void initialize() {
        String  url = getArguments().getString("url");
        String text = getArguments().getString("text");
        pageTxt.setText(text);
        Picasso.with(getActivity()).load(url).error(getResources().getDrawable(R.mipmap.ic_launcher)).placeholder(R.drawable.loading).into(pageImg);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
