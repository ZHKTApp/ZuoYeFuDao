package com.zwyl.homeworkhelp.base;

/**
 * Created by zhubo on 2017/11/24.
 */

public class ComFlag {
    public static final String ASC = "asc";//列表升序
    public static final String DESC = "desc";//降序
    public static final String PACKAGE_NAME = "作业辅导";
    public static final String APP_ID= "zuoyefudao";
    public class NumFlag {
        public static final int INTENT_WEL = 0;//app从后台到前台或者锁屏后回到app，跳转WelcomActivit时的参数
        public static final int RB_TOP_WRITE = 0;//手写笔
        public static final int RB_TOP_EDIT = 1;//输入法
        public static final int RB_TOP_PHOTO = 2;//拍照
        // 题目类型
        public static final int EXERCISE_RADIO = 30101;//单选题
        public static final int EXERCISE_MULTIPLE = 30102;//多选题
        public static final int EXERCISE_JUDGE = 30103;//判断题
        public static final int EXERCISE_GAP = 30104;//填空题
        public static final int EXERCISE_COUNT = 30105;//计算题
        public static final int EXERCISE_SHORT = 30106;//简答题
        //作业类型
        public static final int WORKTYPE_GUIDE = 80101;//导学测试
        public static final int WORKTYPE_INTERNET = 501;//网络作业
        public static final int WORKTYPE_BANK = 50101;//题库作业
        public static final int WORKTYPE_ADJUNCT = 50102;//附件作业
        public static final int WORKTYPE_SIMPLE = 50103;//简易作业
        public static final int WORKTYPE_COACH = 901;//作业辅导
        //作业辅导--习题
        public static final int WORK_SUPPORT_EXERCISE = 90101;
        //作业辅导--微课
        public static final int WORK_SUPPORT_VEDIO = 90102;
    }


    public class StrFlag {
        public static final String TAG = "TAG";//常用字符串
    }

    /*课本详情界面popwindow*/
    public class PopFlag {
        public static final String TITLE = "title";//顶部button按钮
        public static final String NAME = "name";//下面的条目
    }
}
