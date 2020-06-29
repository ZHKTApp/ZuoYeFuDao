package com.zwyl.homeworkhelp.main.subject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mayigeek.frame.http.state.HttpSucess;
import com.zwyl.homeworkhelp.App;
import com.zwyl.homeworkhelp.R;
import com.zwyl.homeworkhelp.base.BaseActivity;
import com.zwyl.homeworkhelp.base.ComFlag;
import com.zwyl.homeworkhelp.base.adapter.CommonAdapter;
import com.zwyl.homeworkhelp.base.adapter.ViewHolder;
import com.zwyl.homeworkhelp.customveiw.CustomLinearLayoutManager;
import com.zwyl.homeworkhelp.dialog.AddNoteDialog;
import com.zwyl.homeworkhelp.dialog.AnalysisDialog;
import com.zwyl.homeworkhelp.dialog.TitleDialog;
import com.zwyl.homeworkhelp.http.ApiUtil;
import com.zwyl.homeworkhelp.main.PlayActivity;
import com.zwyl.homeworkhelp.service.UserService;
import com.zwyl.homeworkhelp.util.DownloadManager;
import com.zwyl.homeworkhelp.util.FileUtils;
import com.zwyl.homeworkhelp.util.MediaUtils;
import com.zwyl.homeworkhelp.util.MyProgress;
import com.zwyl.homeworkhelp.util.Utils;
import com.zwyl.homeworkhelp.viewstate.ViewControlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.appsdream.nestrefresh.util.L;

public class SubjectActivity extends BaseActivity {
    @BindView(R.id.rl_subject)
    RecyclerView rlSubject;
    List mlist = new ArrayList<BeanSubject>();
    List mRadio = new ArrayList<BeanAbc>();
    int[] mJudge = {R.mipmap.right, R.mipmap.close};//判断
    //    @BindView(R.id.tv_sunbject_save)
//    TextView tvSunbjectSave;
    private CustomLinearLayoutManager layoutManager;
    private String workSupportId;
    private String workSupportName;
    private String cmTeacherId;
    private String textBookId;
    private CommonAdapter mAdapter;
    private UserService api;
    List<BeanInfo> infoList = new ArrayList<BeanInfo>();
    List<String> picturePathList = new ArrayList<String>();
    private int subjectSize;
    protected static final int REQUEST_CODE = 1;
    private String token;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_subject;
    }

    @Override
    protected void initView() {
        super.initView();
        workSupportId = getIntent().getStringExtra("workSupportId");
        workSupportName = getIntent().getStringExtra("workSupportName");
        cmTeacherId = getIntent().getStringExtra("cmTeacherId");
        textBookId = getIntent().getStringExtra("textBookId");
        token = getIntent().getStringExtra("token");
        setShowFilter(false);
        setTitleCenter(workSupportName);
        //  webViewParticipationDetail.loadDataWithBaseURL(null, "<p>asdfasdfas<img src=\"http://qkc-oss.oss-cn-beijing.aliyuncs.com/1547541255125.jpg\" title=\"Tulips.jpg\" alt=\"Tulips.jpg\"></p>", "text/html", "UTF-8", null);
        setRefreshClick(v -> {
            getdata();
        });

        layoutManager = new CustomLinearLayoutManager(App.mContext);
        rlSubject.setLayoutManager(layoutManager);
//        rlSubject.setNestedScrollingEnabled(false);//禁止滑动
        rlSubject.setAdapter(mAdapter = new CommonAdapter<BeanSubject>(App.mContext, R.layout.item_subject, mlist) {
            @Override
            protected void convert(ViewHolder holder, BeanSubject beanSubject, int position) {
//                holder.setText(R.id.tv_item_subject_type, Utils.getsubjectType(beanSubject.exerciseTypeCode));
//                holder.setText(R.id.tv_item_subject_name, beanSubject.exercisesTitle);
//                holder.setText(R.id.tv_item_subject_name, beanSubject.createTime);
                holder.setText(R.id.tv_item_subject_type, "第" + (position + 1) + "题");
                holder.setText(R.id.tv_item_teacher_name, "[" + beanSubject.teacherName + "]");
                holder.setText(R.id.tv_item_subject_time, beanSubject.createTime + " 创建");
                LinearLayout ll_item_subject_listen = holder.getView(R.id.ll_item_subject_listen);
                LinearLayout ll_item_subject_look = holder.getView(R.id.ll_item_subject_look);
                LinearLayout ll_item_subject_addFalse = holder.getView(R.id.ll_item_subject_addFalse);
                LinearLayout ll_item_subject_down = holder.getView(R.id.ll_item_subject_down);
                TextView tv_item_subject_down = holder.getView(R.id.tv_item_subject_down);
                WebView webView_detail = holder.getView(R.id.webView_detail);
                VideoView video_item_subject = holder.getView(R.id.video_item_subject);
                ImageView iv_item_subject = holder.getView(R.id.iv_item_subject);
                ImageView iv_item_button = holder.getView(R.id.iv_item_button);
                MyProgress progressBar = holder.getView(R.id.progressBar);
                String fileName = ComFlag.PACKAGE_NAME + beanSubject.exerciseExplainFileUri.substring(beanSubject.exerciseExplainFileUri.lastIndexOf("/") + 1);
                boolean isDownload = FileUtils.isFilesCreate(ComFlag.PACKAGE_NAME, fileName);
                int workType = beanSubject.sourceTypeCode;
                //是否有听讲解内容

                if(workType == ComFlag.NumFlag.WORK_SUPPORT_VEDIO){
                    if (TextUtils.isEmpty(beanSubject.exerciseExplainFileUri)) {
                        beanSubject.exerciseExplainFileUri = beanSubject.microCourseUri;
                    }
                }

                if (TextUtils.isEmpty(beanSubject.exerciseExplainFileUri)) {
                    ll_item_subject_listen.setSelected(false);
                    ll_item_subject_listen.setClickable(false);
                    ll_item_subject_down.setBackground(getResources().getDrawable(R.drawable.drawable_gray_bg_efef));
                    tv_item_subject_down.setText("下载");
                } else {
                    ll_item_subject_down.setBackground(getResources().getDrawable(R.drawable.drawable_green_bg));
                    tv_item_subject_down.setText("下载");
                    ll_item_subject_listen.setClickable(true);
                    ll_item_subject_listen.setSelected(true);
                    //听讲解
                    ll_item_subject_listen.setOnClickListener(v -> {
                        Intent intent = new Intent(App.getContext(), PlayActivity.class);
                        intent.putExtra("resourceUri", beanSubject.exerciseExplainFileUri);
                        startActivity(intent);
                    });
                    //是否已下载
                    if (isDownload) {
                        ll_item_subject_down.setClickable(false);
                        tv_item_subject_down.setText("已下载");
                        ll_item_subject_down.setBackground(getResources().getDrawable(R.drawable.drawable_gray_bg));
                        ll_item_subject_down.setSelected(false);
                    } else {
                        ll_item_subject_down.setBackground(getResources().getDrawable(R.drawable.drawable_green_bg));
                        ll_item_subject_down.setClickable(true);
                        ll_item_subject_down.setSelected(true);
                        tv_item_subject_down.setText("下载");
                        //下载
                        ll_item_subject_down.setOnClickListener(v -> {
                            progressBar.setVisibility(View.VISIBLE);
                            startDownload(beanSubject.exerciseExplainFileUri, fileName, progressBar, tv_item_subject_down);
                        });
                    }
                }
                ImageView iv_item_subject_false = holder.getView(R.id.iv_item_subject_false);
                //是否加入错题集
                if (beanSubject.isMistakesCollection) {
                    ll_item_subject_addFalse.setSelected(true);
                    iv_item_subject_false.setBackgroundResource(R.mipmap.deletefalse);
                } else {
                    ll_item_subject_addFalse.setSelected(true);
                    iv_item_subject_false.setBackgroundResource(R.mipmap.addfalse);
                }


                if (!TextUtils.isEmpty(beanSubject.exerciseAnalysis)) {
                    ll_item_subject_look.setClickable(true);
                    ll_item_subject_look.setSelected(true);
                } else {
                    ll_item_subject_look.setSelected(false);
                    ll_item_subject_look.setClickable(false);
                }

                if (workType == ComFlag.NumFlag.WORK_SUPPORT_VEDIO) {
                    iv_item_subject.setVisibility(View.VISIBLE);
                    iv_item_button.setVisibility(View.VISIBLE);
                    webView_detail.setVisibility(View.GONE);
                    MediaUtils.getImageForVideo(beanSubject.microCourseUri, new MediaUtils.OnLoadVideoImageListener() {
                        @Override
                        public void onLoadImage(File file) {
                            iv_item_subject.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                        }
                    });
                    iv_item_subject.setOnClickListener(v -> {
                        Intent intent = new Intent(SubjectActivity.this, PlayActivity.class);
                        intent.putExtra("resourceUri", beanSubject.microCourseUri);
                        startActivity(intent);
                        ApiUtil.doDefaultApi(api.addlog(beanSubject.exercisesId, ComFlag.NumFlag.WORK_SUPPORT_VEDIO, cmTeacherId, textBookId), new HttpSucess<String>() {
                            @Override
                            public void onSucess(String data) {
                                Log.e("http", "添加成功");
                            }
                        }, ViewControlUtil.create2Dialog(SubjectActivity.this));
                    });
                } else {
                    iv_item_subject.setVisibility(View.GONE);
                    iv_item_button.setVisibility(View.GONE);
                    webView_detail.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(beanSubject.exercisesTitle))
                        webView_detail.loadDataWithBaseURL(null, beanSubject.exercisesTitle, "text/html", "UTF-8", null);
                }

                //看解析
                ll_item_subject_look.setOnClickListener(v -> {
                    if (workType == ComFlag.NumFlag.WORK_SUPPORT_VEDIO) {

                    } else {
                        if (!TextUtils.isEmpty(beanSubject.exerciseAnalysis))
                            new AnalysisDialog(SubjectActivity.this, "解析", beanSubject.exerciseAnalysis).show();
                        else
                            showToast("暂无解析");
                    }
                });
                //错题集
                ll_item_subject_addFalse.setOnClickListener(v -> {
                    //是否加入过错题集
                    if (beanSubject.isMistakesCollection) {
                        new TitleDialog(SubjectActivity.this, getResources().getString(R.string.clearMsg), new TitleDialog.OnclickListener() {
                            @Override
                            public void OnSure() {
                                ApiUtil.doDefaultApi(api.deleteMistakesCollection(workSupportId, beanSubject.exercisesId), data -> {
                                    getdata();
                                });
                            }

                            @Override
                            public void OnCancle() {

                            }
                        }).show();


                    } else {
                        //加入错题集
                        new AddNoteDialog(SubjectActivity.this, new AddNoteDialog.OnClickListener() {
                            @Override
                            public void onClick(String etString) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("workId", workSupportId);
                                map.put("exerciseId", beanSubject.exercisesId);
                                map.put("reason", etString);
                                map.put("workType", ComFlag.NumFlag.WORKTYPE_COACH + "");
                                ApiUtil.doDefaultApi(api.addMistakesCollection(map), data -> {
                                    getdata();

                                });
                            }
                        }).show();

                    }
                });

            }
        });
    }

    /**
     * @param exercisesId 题目id
     * @param optionId    题目答案
     */
    //去重单选题
    private void deWeightRadio(String exercisesId, String optionId) {
        BeanInfo beanInfo = new BeanInfo(exercisesId, optionId);
        if (infoList.size() > 0) {
            for (int i = 0; i < infoList.size(); i++) {
                if (exercisesId.equals(infoList.get(i).exerciseId)) {
                    infoList.remove(infoList.get(i));
                }
            }
            infoList.add(beanInfo);
        } else {
            infoList.add(beanInfo);
        }
    }

    /**
     *
     */
    @Override
    protected void initData() {
        super.initData();
        api = ApiUtil.createDefaultApi(UserService.class, token);
        getdata();
    }

    private void getdata() {
        ApiUtil.doDefaultApi(api.selectWorkSupportInfo(workSupportId), data -> {
            if (data.size() > 0 && !data.equals("")) {
                subjectSize = data.size();
                mAdapter.setDatas(data);
            }
        }, ViewControlUtil.create2Dialog(this));
    }

    private void startDownload(String url, String name, ProgressBar progressBar, TextView tv_item_down) {
        DownloadManager downloadManager = DownloadManager.getInstance();
        String localUrl = FileUtils.getFilePath(ComFlag.PACKAGE_NAME, name).getAbsolutePath();
        downloadManager.download(localUrl, url, new DownloadManager.DownloadListener() {
            @Override
            public void preDownload() {

            }

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        tv_item_down.setSelected(true);
                        tv_item_down.setText("已下载");
                        mAdapter.notifyDataSetChanged();

                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("下载失败");
                    }
                });

            }

            @Override
            public void onUpdate(int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                    }
                });
            }

            @Override
            public void onCacheUpdate() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }


}
