package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Team;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.MajordomoDetailActivity;
import com.chinajsbn.venus.ui.photography.WorksDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.recyclerviewdiviver.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 团队
 *
 * Created by 13510 on 2015/12/2.
 */
@FragmentFeature(layout = R.layout.fragment_mt_sel_photographer)
public class TeamFragment extends BaseFragment implements OnRecyclerItemClickListener {

    private List<Team> dataList = new ArrayList<>();

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    public void initialize() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.gray_300)).build());

        adapter = new MyAdapter(TeamFragment.this);
        recyclerView.setAdapter(adapter);

        Team team = new Team();
        team.setTag("majordomo_tag");
        dataList.add(team);

        HttpClient.getInstance().teamList(1, majordomoCallback);
    }

    private Callback<Base<ArrayList<Team>>> majordomoCallback = new Callback<Base<ArrayList<Team>>>() {
        @Override
        public void success(Base<ArrayList<Team>> resp, Response response) {
            if(resp.getCode() == 200){
                for (Team team : resp.getData()) {
                    team.setTag("majordomo");
                    dataList.add(team);
                }
                adapter.notifyDataSetChanged();

                Team team = new Team();
                team.setTag("senior_tag");
                dataList.add(team);
                HttpClient.getInstance().teamList(2, seniorCallback);
            }else{

            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private Callback<Base<ArrayList<Team>>> seniorCallback = new Callback<Base<ArrayList<Team>>>() {
        @Override
        public void success(Base<ArrayList<Team>> resp, Response response) {
            if(resp.getCode() == 200){
                for (Team team : resp.getData()) {
                    team.setTag("senior");
                    dataList.add(team);
                }
                adapter.notifyDataSetChanged();
            }else{

            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private static final int TYPE_TAG_MAJORDOMO = 0;
        private static final int TYPE_TAG_SENIOR = 1;
        private static final int TYPE_MAJORDOMO = 2;
        private static final int TYPE_SENIOR = 3;

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener listener) {
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataList.get(position).getTag().equals("majordomo_tag")) {
                return TYPE_TAG_MAJORDOMO;
            } else if (dataList.get(position).getTag().equals("senior_tag")) {
                return TYPE_TAG_SENIOR;
            } else if (dataList.get(position).getTag().equals("majordomo")) {
                return TYPE_MAJORDOMO;
            } else if (dataList.get(position).getTag().equals("senior")) {
                return TYPE_SENIOR;
            }
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_TAG_MAJORDOMO) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singel_textview, parent, false);
                return new MajordomoTagViewHolder(itemView);
            } else if (viewType == TYPE_TAG_SENIOR) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singel_textview, parent, false);
                return new SeniorTagViewHolder(view);
            } else if (viewType == TYPE_MAJORDOMO) {
                View view = View.inflate(parent.getContext(), R.layout.item_team_majordomo, null);
                return new MajordomoViewHolder(view);
            } else if (viewType == TYPE_SENIOR) {
                View view = View.inflate(parent.getContext(), R.layout.item_team_senior, null);
                return new SeniorViewHolder(view, null);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof MajordomoTagViewHolder) {
                MajordomoTagViewHolder tagHolder = (MajordomoTagViewHolder) holder;
                tagHolder.tagTxt.setText("总监级摄影团队");
            } else if (holder instanceof SeniorTagViewHolder) {
                SeniorTagViewHolder tagHolder = (SeniorTagViewHolder) holder;
                tagHolder.tagTxt.setText("资深级摄影团队");
            } else if (holder instanceof MajordomoViewHolder) {

                MajordomoViewHolder majorHolder = (MajordomoViewHolder) holder;
                final Team team = dataList.get(position);
                majorHolder.photoNameTxt.setText(team.getPhotographerDetail().getPersonName());
                majorHolder.stylistNameTxt.setText(team.getStylistDetail().getPersonName());
                majorHolder.teamNameTxt.setText(team.getTeamName());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                majorHolder.rootView.setLayoutParams(params);

                if(!TextUtils.isEmpty(team.getPhotographerDetail().getHead())) {
                    Picasso.with(getActivity()).load(team.getPhotographerDetail().getHead()).into(majorHolder.photoHeadImg);
                }
                if(!TextUtils.isEmpty(team.getStylistDetail().getHead())) {
                    Picasso.with(getActivity()).load(team.getStylistDetail().getHead()).into(majorHolder.stylistHeadImg);
                }
                majorHolder.detailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                        intent.putExtra("isMajordomo", true);
                        intent.putExtra("data", team);
                        animStart(intent);
                    }
                });

            } else if (holder instanceof SeniorViewHolder) {
                SeniorViewHolder seniorHolder = (SeniorViewHolder) holder;
                final Team team = dataList.get(position);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimenUtil.screenWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                seniorHolder.leftLayout.setLayoutParams(params);

                seniorHolder.photoNameTxt.setText(team.getPhotographerDetail().getPersonName());
                seniorHolder.stylistNameTxt.setText(team.getStylistDetail().getPersonName());
                seniorHolder.teamNameTxt.setText(team.getTeamName());

                if(!TextUtils.isEmpty(team.getPhotographerDetail().getHead())) {
                    Picasso.with(getActivity()).load(team.getPhotographerDetail().getHead()).into(seniorHolder.photoHeadImg);
                }
                if(!TextUtils.isEmpty(team.getStylistDetail().getHead())) {
                    Picasso.with(getActivity()).load(team.getStylistDetail().getHead()).into(seniorHolder.stylistHeadImg);
                }

                seniorHolder.detailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                        intent.putExtra("isMajordomo", false);
                        intent.putExtra("data", team);
                        animStart(intent);
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            String p_i[] = view.getTag().toString().split("@");
            int position = Integer.parseInt(p_i[0]);
            int i = Integer.parseInt(p_i[1]) - 1;

            final Team team = dataList.get(position);
            Intent intent = new Intent(getActivity(), WorksDetailActivity.class);
//            intent.putExtra("personId", stylist.getPersonId() + "");
//            intent.putExtra("worksId", stylist.getList().get(i).getContentId());
            animStart(intent);
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        /**
         * majordomo tag vew holder
         */
        public class MajordomoTagViewHolder extends RecyclerView.ViewHolder {

            TextView tagTxt;

            public MajordomoTagViewHolder(View itemView) {
                super(itemView);
                tagTxt = (TextView) itemView.findViewById(R.id.single_text);
            }
        }

        /**
         * senior tag view holder
         */
        public class SeniorTagViewHolder extends RecyclerView.ViewHolder {

            TextView tagTxt;

            public SeniorTagViewHolder(View itemView) {
                super(itemView);
                tagTxt = (TextView) itemView.findViewById(R.id.single_text);
            }
        }

        /**
         * majordomo vew holder
         */
        public class MajordomoViewHolder extends RecyclerView.ViewHolder {

            ImageView photoHeadImg;
            ImageView stylistHeadImg;
            TextView photoNameTxt;
            TextView stylistNameTxt;

            TextView teamNameTxt;
            TextView detailTxt;
            View rootView;

            public MajordomoViewHolder(View itemView) {
                super(itemView);
                rootView = itemView;
                photoHeadImg = (ImageView) itemView.findViewById(R.id.team_majordomo_photo_head_img);
                stylistHeadImg = (ImageView) itemView.findViewById(R.id.team_majordomo_stylist_head_img);

                photoNameTxt = (TextView) itemView.findViewById(R.id.team_majordomo_photo_name_txt);
                stylistNameTxt = (TextView) itemView.findViewById(R.id.team_majordomo_stylist_name_txt);

                teamNameTxt = (TextView) itemView.findViewById(R.id.team_name_txt);
                detailTxt = (TextView) itemView.findViewById(R.id.team_detail_txt);
            }
        }

        /**
         *
         */
        public final class SeniorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView photoHeadImg;
            ImageView stylistHeadImg;
            TextView photoNameTxt;
            TextView stylistNameTxt;

            TextView teamNameTxt;
            TextView detailTxt;

            LinearLayout leftLayout, rightLayout;

            public SeniorViewHolder(View view, OnRecyclerItemClickListener listener) {
                super(view);

                photoHeadImg = (ImageView) itemView.findViewById(R.id.team_majordomo_photo_head_img);
                stylistHeadImg = (ImageView) itemView.findViewById(R.id.team_majordomo_stylist_head_img);

                photoNameTxt = (TextView) itemView.findViewById(R.id.team_majordomo_photo_name_txt);
                stylistNameTxt = (TextView) itemView.findViewById(R.id.team_majordomo_stylist_name_txt);

                teamNameTxt = (TextView) itemView.findViewById(R.id.team_name_txt);
                detailTxt = (TextView) itemView.findViewById(R.id.team_detail_txt);
                leftLayout = (LinearLayout) itemView.findViewById(R.id.left_layout);
                rightLayout= (LinearLayout) itemView.findViewById(R.id.right_layout);
            }

            @Override
            public void onClick(View v) {
                //onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
