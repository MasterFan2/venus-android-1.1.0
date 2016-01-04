package com.chinajsbn.venus.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.photography.PageFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.MasterTitleView;
import com.tool.widget.MultiViewPager;

@ActivityFeature(layout = R.layout.activity_view_pager, statusBarColor = R.color.gray)
public class ViewPagerActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @ViewInject(R.id.tv_info)
    private TextView textView;

    private final String [] lists = {"李白（701年2月8日—762年12月）[1]  ，字太白，号青莲居士，又号“谪仙人”。是唐代伟大的浪漫主义诗人，被后人誉为“诗仙”。与杜甫并称为“李杜”，为了与另两位诗人李商隐与杜牧即“小李杜”区别，杜甫与李白又合称“大李杜”。其人爽朗大方，爱饮酒作诗，喜交友。\n" +
            "李白有《李太白集》传世，诗作中多以醉时写的，代表作有《望庐山瀑布》、《行路难》、《蜀道难》、《将进酒》、《梁甫吟》、《早发白帝城》等多首。\n" +
            "李白所作词赋，宋人已有传记（如文莹《湘山野录》卷上），就其开创意义及艺术成就而言，“李白词”享有极为崇高的地位。", "杜甫（公元712年－公元770年），字子美，汉族，祖籍襄阳，生于河南巩县。[1-2]   自号少陵野老，唐代伟大的现实主义诗人，与李白合称“李杜”。为了与另两位诗人李商隐与杜牧即“小李杜”区别，杜甫与李白又合称“大李杜”，杜甫也常被称为“老杜”。\n" +
            "杜甫在中国古典诗歌中的影响非常深远，被后人称为“诗圣”，他的诗被称为“诗史”。后世称其杜拾遗、杜工部，也称他杜少陵、杜草堂。\n" +
            "杜甫创作了《春望》、《北征》、《三吏》、《三别》等名作。759年杜甫弃官入川，虽然躲避了战乱，生活相对安定，但仍然心系苍生，胸怀国事。虽然杜甫是个现实主义诗人，但他也有狂放不羁的一面，从其名作《饮中八仙歌》不难看出杜甫的豪气干云。", "辛弃疾（1140年5月28日－1207年10月3日），字幼安，号稼轩，山东东路济南府历城县（今济南市历城区遥墙镇四凤闸村）人，中国南宋豪放派词人，人称词中之龙，与苏轼合称“苏辛”，与李清照并称“济南二安”。辛弃疾生于金国，少年抗金归宋，曾任江西安抚使、福建安抚使等职。追赠少师，谥忠敏。有词集《稼轩长短句》，现存词600多首，强烈的爱国主义思想和战斗精神是他词的基本思想内容。著名词作《水调歌头》（带湖吾甚爱）、《摸鱼儿（更能消几番风雨）》、《满江红（家住江南）》、《沁园春》（杯汝来前）、《西江月·夜行黄沙道中》等。其词艺术风格多样，以豪放为主，风格沉雄豪迈又不乏细腻柔媚之处。其词题材广阔又善化用前人典故入词，抒写力图恢复国家统一的爱国热情，倾诉壮志难酬的悲愤，对当时执政者的屈辱求和颇多谴责；也有不少吟咏祖国河山的作品。著有《美芹十论》与《九议》，条陈战守之策。由于与当政的主和派政见不合，后被弹劾落职，退隐山居，公元1207年秋季，辛弃疾逝世，年68岁。", "苏轼（1037年1月8日—1101年8月24日），字子瞻，又字和仲，号东坡居士，自号道人，世称苏仙[1-3]    。宋代重要的文学家，宋代文学最高成就的代表。汉族，北宋眉州眉山（今属四川省眉山市）人。宋仁宗嘉祐（1056—1063）年间进士。其诗题材广阔，清新豪健，善用夸张比喻，独具风格，与黄庭坚并称“苏黄”。词开豪放一派，与辛弃疾同是豪放派代表，并称“苏辛”。 又工书画。有《东坡七集》、《东坡易传》、《东坡乐府》等。",
            "李清照（1084年3月13日—1155年5月12日），号易安居士，汉族，齐州（今山东济南章丘）人。宋代（两宋之交）女词人，婉约词派代表，有“千古第一才女”之称。\n" +
                    "李清照出生于书香门第，早期生活优裕，其父李格非藏书甚富，她小时候就在良好的家庭环境中打下文学基础。出嫁后与夫赵明诚共同致力于书画金石的搜集整理。金兵入据中原时，流寓南方，境遇孤苦。所作词，前期多写其悠闲生活，后期多悲叹身世，情调感伤。形式上善用白描手法，自辟途径，语言清丽。论词强调协律，崇尚典雅，提出词“别是一家”之说，反对以作诗文之法作词。能诗，留存不多，部分篇章感时咏史，情辞慷慨，与其词风不同。"
    };

    @ViewInject(R.id.viewPager)
    private MultiViewPager multiViewPager;

    @Override
    public boolean onKeydown() {

        animFinish();
        return true;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view){
        animFinish();
    }

    @Override
    public void initialize() {
        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Fragment getItem(int position) {
                return new PageFragment();
            }
        };
        multiViewPager.setAdapter(adapter);
        multiViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                textView.setText(lists[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
