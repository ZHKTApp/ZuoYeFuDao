package com.zwyl.homeworkhelp.main.detaile;

import android.content.Intent;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mayigeek.frame.http.state.HttpSucess;
import com.mayigeek.frame.view.state.ViewControl;
import com.zwyl.homeworkhelp.App;
import com.zwyl.homeworkhelp.R;
import com.zwyl.homeworkhelp.base.BaseActivity;
import com.zwyl.homeworkhelp.base.ComFlag;
import com.zwyl.homeworkhelp.base.adapter.CommonAdapter;
import com.zwyl.homeworkhelp.base.adapter.MultiItemTypeAdapter;
import com.zwyl.homeworkhelp.base.adapter.ViewHolder;
import com.zwyl.homeworkhelp.dialog.bean.BeanAllYear;
import com.zwyl.homeworkhelp.dialog.popwindow.PopupWindowA;
import com.zwyl.homeworkhelp.dialog.popwindow.PopupWindowB;
import com.zwyl.homeworkhelp.dialog.popwindow.PopupWindowC;
import com.zwyl.homeworkhelp.http.ApiUtil;
import com.zwyl.homeworkhelp.main.BeanHomeGrid;
import com.zwyl.homeworkhelp.main.BeanTextBookGrid;
import com.zwyl.homeworkhelp.main.subject.SubjectActivity;
import com.zwyl.homeworkhelp.service.UserService;
import com.zwyl.homeworkhelp.util.DensityUtil;
import com.zwyl.homeworkhelp.viewstate.ViewControlUtil;
import com.zwyl.homeworkhelp.viewstate.treelist.FileBean;
import com.zwyl.homeworkhelp.viewstate.treelist.Node;
import com.zwyl.homeworkhelp.viewstate.treelist.SimpleTreeAdapter;
import com.zwyl.homeworkhelp.viewstate.treelist.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class TextDetaileActivity extends BaseActivity {

    @BindView(R.id.tv_textclass)
    TextView tvTextclass;

    @BindView(R.id.detaile_recyclerview)
    RecyclerView detaileRecyclerview;
    List mlist = new ArrayList<com.zwyl.homeworkhelp.main.detaile.BeanCatelog>();
    List<com.zwyl.homeworkhelp.main.detaile.BeanDetaile> mlistDetaile = new ArrayList<com.zwyl.homeworkhelp.main.detaile.BeanDetaile>();
    @BindView(R.id.iv_detaile_select)
    ImageView ivDetaileSelect;
    @BindView(R.id.ll_detaile_select)
    LinearLayout llDetaileSelect;
    @BindView(R.id.catalog_lisitview)
    ListView catalogLisitview;
    private CommonAdapter cutelogAdapter;
    private CommonAdapter detaileAdapter;
    private int postionTag = -1;
    private String textBookId;
    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private TreeListViewAdapter mAdapter;
    private UserService api;
    private List<BeanAllYear> years;
    private String timeasc = ComFlag.DESC;
    private int timeAscTag=0;//0代表降序,1代表升序
    private boolean catelogIsclick;
    private String clickId;//目录是否点击,有值点击.无值未点击,默认未点击(为了刷新详情时判断刷新默认说句还是选中后的数据 )
    private String schoolSubjectId;
    private String schoolTextBookName;
    private String token;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_textdetaile;
    }

    @Override
    protected void initView() {
        super.initView();
        ivDetaileSelect.setImageResource(R.mipmap.selecte);
        textBookId = getIntent().getStringExtra("textBookId");
        schoolSubjectId = getIntent().getStringExtra("schoolSubjectId");
        schoolTextBookName = getIntent().getStringExtra("schoolTextBookName");
        token = getIntent().getStringExtra("token");
        setHeadView();
        //目录列表
        try {
            mAdapter = new SimpleTreeAdapter<FileBean>(catalogLisitview, this, mDatas, 10);
            catalogLisitview.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //目录点击事件
        mAdapter.setOnTreeNodeClickListener((Node node, int position) -> {
//            if (node.isLeaf()) {
            clickId = node.getClickId();
            getLeftItemclickData(clickId);
            catelogIsclick = true;
//            }
        });
        //详情列表
        LinearLayoutManager linearLayoutManagerDetaile = new LinearLayoutManager(App.mContext, OrientationHelper.VERTICAL, false);
        detaileRecyclerview.setLayoutManager(linearLayoutManagerDetaile);
        detaileRecyclerview.setAdapter(detaileAdapter = new CommonAdapter<com.zwyl.homeworkhelp.main.detaile.BeanDetaile>(App.mContext, R.layout.item_detaile, mlistDetaile) {
            @Override
            protected void convert(ViewHolder holder, com.zwyl.homeworkhelp.main.detaile.BeanDetaile beanDetaile, int position) {
                holder.setText(R.id.tv_item_detaile_name, beanDetaile.workSupportName);
                holder.setText(R.id.tv_item_detaile_creatman, "创建老师:" + beanDetaile.teacherName);
                holder.setText(R.id.tv_item_detaile_starttime, "开始时间:" + beanDetaile.startTime);
                TextView tv_look = holder.getView(R.id.tv_look);
                tv_look.setOnClickListener((View v) -> {
                    Intent intent = createIntent(SubjectActivity.class);
                    intent.putExtra("workSupportId", beanDetaile.workSupportId);
                    intent.putExtra("workSupportName", beanDetaile.workSupportName);
                    intent.putExtra("cmTeacherId", beanDetaile.cmTeacherId);
                    intent.putExtra("textBookId", textBookId);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    ApiUtil.doDefaultApi(api.addlog(beanDetaile.workSupportId, ComFlag.NumFlag.WORKTYPE_COACH, beanDetaile.cmTeacherId, textBookId), new HttpSucess<String>() {
                        @Override
                        public void onSucess(String data) {
                            Log.e("http", "添加成功");
                        }
                    }, ViewControlUtil.create2Dialog(TextDetaileActivity.this));
                });
            }
        });
        detaileAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = createIntent(SubjectActivity.class);
                intent.putExtra("workSupportId", detaileLists.get(position).workSupportId);
                intent.putExtra("workSupportName", detaileLists.get(position).workSupportName);
                intent.putExtra("cmTeacherId", detaileLists.get(position).cmTeacherId);
                intent.putExtra("textBookId", textBookId);
                intent.putExtra("token", token);
                startActivity(intent);
                ApiUtil.doDefaultApi(api.addlog(detaileLists.get(position).workSupportId, ComFlag.NumFlag.WORKTYPE_COACH, detaileLists.get(position).cmTeacherId, textBookId), new HttpSucess<String>() {
                    @Override
                    public void onSucess(String data) {
                        Log.e("http", "添加成功");
                    }
                }, ViewControlUtil.create2Dialog(TextDetaileActivity.this));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        api = ApiUtil.createDefaultApi(UserService.class, token);
        detaileLists = new ArrayList<>();
        //左边目录列表
        getcatelogList(textBookId);
        getDefaltDetaileData();


        //获取所有学年列表
        ApiUtil.doDefaultApi(api.allYearsByStudentId(null), (List<BeanAllYear> data) -> {
            years = data;
        }, ViewControlUtil.create2Dialog(this));

    }

    private List<BeanDetaile> detaileLists;

    private void getDefaltDetaileData() {
        //默认请求详情列表
        ApiUtil.doDefaultApi(api.selectWorkSupportByTextBookId(textBookId, timeasc), new HttpSucess<List<com.zwyl.homeworkhelp.main.detaile.BeanDetaile>>() {
            @Override
            public void onSucess(List<BeanDetaile> data) {
                detaileAdapter.setDatas(data);
                detaileLists.clear();
                detaileLists.addAll(data);
                ivDetaileSelect.setImageResource(R.mipmap.selecte);
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    private void getcatelogList(String BookId) {
        //左边目录列表
        ApiUtil.doDefaultApi(api.selectTextBookChapter(textBookId), (List<ResultInfoBean> data) -> {
            mDatas.clear();
            traveTree(data);
            mAdapter.refreshData(mDatas, 1);
            mAdapter.notifyDataSetChanged();
        }, ViewControlUtil.create2Dialog(this));
    }

    private void traveTree(List<ResultInfoBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            ResultInfoBean resultInfoBean = dataList.get(i);
            FileBean fileBean = new FileBean(resultInfoBean.getTextBookChapterId(), resultInfoBean.getTextBookChapterParentId(), resultInfoBean.getTextBookChapterName(), resultInfoBean.getTextBookChapterId() + "");
            mDatas.add(fileBean);
            traveTree(dataList.get(i).getChildrenList());
        }
    }

    //点击事件
    @OnClick({R.id.ll_detaile_select, R.id.detaile_recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_detaile_select:
                showPopWindow(ComFlag.PopFlag.TITLE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (detaileAdapter != null)
            getDefaltDetaileData();
        if (mAdapter != null)
            getcatelogList(textBookId);
    }

    //设置顶部view显示及点击事件
    private void setHeadView() {
        setTitleCenter(schoolTextBookName);
        tvTextclass.setText(schoolTextBookName);
        setShowLeftHead(true);//左边顶部按钮
        setShowRightHead(true);//右边顶部按钮
        setShowFilter(false);//日历筛选
        setShowLogo(false);//logo筛选
        setShowRefresh(true);//刷新
        setRefreshClick((View v) -> {//刷新点击
            if (detaileAdapter != null)
                if (TextUtils.isEmpty(clickId)) {
                    getDefaltDetaileData();
                } else {
                    getLeftItemclickData(clickId);
                }
//            if (mAdapter != null)
//                getcatelogList(textBookId);
        });
        setTimeClick((View v) -> {//时间点击
            if (timeAscTag == 0) {
                head_time.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.asc), null, null, null);
                timeAscTag = 1;
                timeasc = ComFlag.ASC;
            } else {
                head_time.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.desc), null, null, null);
                timeAscTag = 0;
                timeasc = ComFlag.DESC;
            }
            if (clickId == null) {
                getDefaltDetaileData();
            } else {
                getLeftItemclickData(clickId);
            }
        });
        setDownloadClick((View v) -> {//全部下载
//            showToast("全部下载");
        });
    }

    private PopupWindowA mWindowA;
    private PopupWindowB mWindowB;
    private PopupWindowC mWindowC;

    //显示popwindow
    private void showPopWindow(String tag) {
        if (mWindowC != null && mWindowC.isShowing()) {
            mWindowC.dismiss();
        }
        if (mWindowB != null && mWindowB.isShowing()) {
            mWindowB.dismiss();
        }
        if (mWindowA != null && mWindowA.isShowing()) {
            mWindowA.dismiss();
            if (ComFlag.PopFlag.TITLE.equals(tag)) {
                ivDetaileSelect.setImageResource(R.mipmap.selecte);
                return;
            }
        }
        ivDetaileSelect.setImageResource(R.mipmap.selectleft);
        this.mWindowA = new PopupWindowA<BeanAllYear>(this, years, (int position1) -> {
            if (mWindowB != null && mWindowB.isShowing())
                mWindowB.dismiss();
            if (mWindowC != null && mWindowC.isShowing())
                mWindowC.dismiss();
            getSubject(years.get(position1).academicYearId);
        });
        //根据指定View定位
        PopupWindowCompat.showAsDropDown(this.mWindowA, llDetaileSelect, 0, DensityUtil.dip2px(-37), Gravity.RIGHT);
    }

    //获取所有课本目录的数据
    private void getSubject(String academicYearId) {
        ApiUtil.doDefaultApi(api.selectGradeSubejectList(academicYearId), (List<BeanHomeGrid> data) -> {
            if (data.size() > 0) {
                mWindowB = new PopupWindowB<BeanHomeGrid>(TextDetaileActivity.this, data, (int position2) -> {

                    if (mWindowC != null && mWindowC.isShowing())
                        mWindowC.dismiss();
                    if (data.size() > 0) {
                        schoolSubjectId = data.get(position2).schoolSubjectId;
                        getTextBook(academicYearId, schoolSubjectId, data.get(position2).schoolSubjectName);
                    } else {
                        showToast("没有下一级了");
                    }

                });
                //根据指定View定位
                PopupWindowCompat.showAsDropDown(mWindowB, llDetaileSelect, DensityUtil.dip2px(153), DensityUtil.dip2px(-37), Gravity.RIGHT);
            } else {
                showToast("没有下一级了");
            }
        }, ViewControlUtil.create2Dialog(this));

    }

    //获取上下册数据
    private void getTextBook(String academicYearId, String schoolSubjectId, String schoolSubjectName) {
        ApiUtil.doDefaultApi(api.selectTextBook(academicYearId, schoolSubjectId), (List<BeanTextBookGrid> data) -> {
            if (data.size() > 0) {
                mWindowC = new PopupWindowC<BeanTextBookGrid>(TextDetaileActivity.this, data, (int position3) -> {
                    getcatelogList(data.get(position3).schoolTextBookId);
                    tvTextclass.setText(schoolSubjectName);
                    mWindowC.dismiss();
                    mWindowB.dismiss();
                    mWindowA.dismiss();
                    //选择上下册时也刷新详情列表
                    textBookId = data.get(position3).schoolTextBookId;
                    getDefaltDetaileData();
                });
                //根据指定View定位
                PopupWindowCompat.showAsDropDown(mWindowC, llDetaileSelect, DensityUtil.dip2px(260), DensityUtil.dip2px(-37), Gravity.RIGHT);
            } else {
                showToast("没有下一级了");
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    //获取目录点击后的详情数据
    private void getLeftItemclickData(String clickId) {
        ApiUtil.doDefaultApi(api.selectWorkSupportByChapterId(clickId, timeasc), (List<BeanDetaile> data) -> {
            detaileAdapter.setDatas(data);
            detaileLists.clear();
            detaileLists.addAll(data);
        }, ViewControlUtil.create2Dialog(this));
    }

}
