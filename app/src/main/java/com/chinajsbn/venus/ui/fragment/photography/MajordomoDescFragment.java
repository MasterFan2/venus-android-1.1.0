package com.chinajsbn.venus.ui.fragment.photography;

import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by 13510 on 2015/9/23.
 */
@FragmentFeature(layout = R.layout.fragment_majordomo_desc)
public class MajordomoDescFragment extends BaseFragment {

    @ViewInject(R.id.experience_txt)
    private TextView experienceTxt;

    @ViewInject(R.id.description_txt)
    private TextView descriptionTxt;

    @Override
    public void initialize() {
        Photographer photographer = (Photographer) getArguments().getSerializable("photographer");
        if(photographer != null){
            experienceTxt.setText(photographer.getExperience() + " 年从业经历");
            descriptionTxt.setText(photographer.getDescription());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
