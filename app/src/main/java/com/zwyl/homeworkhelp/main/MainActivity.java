package com.zwyl.homeworkhelp.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mayigeek.frame.http.state.HttpSucess;
import com.orhanobut.logger.Settings;
import com.zwyl.homeworkhelp.App;
import com.zwyl.homeworkhelp.R;
import com.zwyl.homeworkhelp.base.BaseActivity;
import com.zwyl.homeworkhelp.base.ComFlag;
import com.zwyl.homeworkhelp.base.adapter.CommonAdapter;
import com.zwyl.homeworkhelp.base.adapter.MultiItemTypeAdapter;
import com.zwyl.homeworkhelp.base.adapter.ViewHolder;
import com.zwyl.homeworkhelp.dialog.RightDrawerDialog;
import com.zwyl.homeworkhelp.dialog.bean.BeanAllYear;
import com.zwyl.homeworkhelp.http.ApiUtil;
import com.zwyl.homeworkhelp.main.detaile.UpdataManager;
import com.zwyl.homeworkhelp.service.UserService;
import com.zwyl.homeworkhelp.util.UtilPermission;
import com.zwyl.homeworkhelp.viewstate.SpaceItemDecoration;
import com.zwyl.homeworkhelp.viewstate.ViewControlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerview;
    private List mlist = new ArrayList<com.zwyl.homeworkhelp.main.BeanHomeGrid>();
    private CommonAdapter mAdapter;
    private List<BeanAllYear> years = new ArrayList<>();
    private UserService api;
    private String academicYearId;
    private String token;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        //设置顶部按钮事件,日历筛选
        setHeadView();
//        token = "ee507cfa4a4148b09f9f7fe2166a59e3";
        token = getIntent().getStringExtra("token");
        Log.e("http", "token : " + token);
        //版本更新
        if (!TextUtils.isEmpty(token)) {
            UpdataManager updataManager = new UpdataManager(this, token, ComFlag.APP_ID);
            updataManager.getVersion();
        }
        //Gridview内容
        GridLayoutManager layoutManager = new GridLayoutManager(App.mContext, 5, OrientationHelper.VERTICAL, false);
        homeRecyclerview.setLayoutManager(layoutManager);
        int spacingInPixels = 60;
        homeRecyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        homeRecyclerview.setAdapter(mAdapter = new CommonAdapter<BeanHomeGrid>(App.mContext, R.layout.item_home_grid, mlist) {
            @Override
            protected void convert(ViewHolder holder, com.zwyl.homeworkhelp.main.BeanHomeGrid beanHomeGrid, int position) {
                holder.setText(R.id.tv_item_grid_name, beanHomeGrid.schoolSubjectName);
                ImageView iv_item_grid_subjects = holder.getView(R.id.iv_item_grid_subjects);
                String schoolSubjectName = beanHomeGrid.schoolSubjectName;
                switch (schoolSubjectName) {
                    case "语文":
                        iv_item_grid_subjects.setImageResource(R.mipmap.yuwen);
                        break;
                    case "数学":
                        iv_item_grid_subjects.setImageResource(R.mipmap.shuxue);
                        break;
                    case "英语":
                        iv_item_grid_subjects.setImageResource(R.mipmap.english);
                        break;
                    case "物理":
                        iv_item_grid_subjects.setImageResource(R.mipmap.wuli);
                        break;
                    case "化学":
                        iv_item_grid_subjects.setImageResource(R.mipmap.huaxue);
                        break;
                    case "生物":
                        iv_item_grid_subjects.setImageResource(R.mipmap.shengwu);
                        break;
                    case "政治":
                        iv_item_grid_subjects.setImageResource(R.mipmap.zhengzhi);
                        break;
                    case "历史":
                        iv_item_grid_subjects.setImageResource(R.mipmap.lishi);
                        break;
                    case "地理":
                        iv_item_grid_subjects.setImageResource(R.mipmap.dili);
                        break;
                    default:
                        iv_item_grid_subjects.setImageResource(R.mipmap.huaxue);
                        break;
                }
            }
        });
    }

    @Override
    protected void initControl() {
        super.initControl();
        //课本点击事件
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = createIntent(TextbookActivity.class);
                BeanHomeGrid item = (BeanHomeGrid) mlist.get(position);
                intent.putExtra("academicYearId", academicYearId);
                intent.putExtra("schoolSubjectId", item.schoolSubjectId);
                intent.putExtra("subjectName", item.schoolSubjectName);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        api = ApiUtil.createDefaultApi(UserService.class, token);
        //获取当前学年
        ApiUtil.doDefaultApi(api.currentYearByStudentId(null), data -> {
            head_filter.setText(data.academicYear);
            academicYearId = data.academicYearId;
            getSubject(academicYearId);
        }, ViewControlUtil.create2Dialog(this));

        //获取所有学年列表
        ApiUtil.doDefaultApi(api.allYearsByStudentId(null), data -> {
            years = data;
        }, ViewControlUtil.create2Dialog(MainActivity.this));
    }

    //获取课本列表数据
    private void getSubject(String academicYearId) {
        ApiUtil.doDefaultApi(api.selectGradeSubejectList(academicYearId), new HttpSucess<List<BeanHomeGrid>>() {
            @Override
            public void onSucess(List<BeanHomeGrid> data) {
                mAdapter.setDatas(data);
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    /**
     * 设置顶部点击事件
     */
    private void setHeadView() {
        setTitleCenter(R.string.app_name);
        setShowLeftHead(false);//左边顶部按钮
        setShowRightHead(false);//右边顶部按钮
        setShowFilter(true);//日历筛选
        setShowLogo(true);//logo筛选
        setShowRefresh(false);//刷新
        setFilterClick(v -> {
            RightDrawerDialog dialog = new RightDrawerDialog(MainActivity.this, years, position -> {
                head_filter.setText(years.get(position).academicYear);
                academicYearId = years.get(position).academicYearId;
                getSubject(academicYearId);
            });
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }


        });
        setLogoClick(v -> {
            finish();
        });
    }

    /**
     * 检查权限
     */
    private boolean needCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (needCheck) {
                UtilPermission.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                needCheck = false;
                showMissingPermissionDialog();
            }

        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle).setMessage(R.string.notifyMsg).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
                finish();
            }
        }).setCancelable(false).show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);
    }
}

