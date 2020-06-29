package com.zwyl.homeworkhelp.main.subject;

import java.util.List;

public class BeanSubject {


    /**
     * isDownload : false
     * exercisesTitle : 下列说法中正确的是
     * sourceTypeCode : 90101
     * teacherName : 孔子老师
     * analysisFileUri : http://art.test.internet.zhiwangyilian.com/images/artImg/3464645
     * isMistakesCollection : true
     * exerciseOptionList : [{"optionId":"danxuan001","optionNo":"A","rankNo":1,"isRight":true,"createTime":"2017-06-03 15:59:59","createUser":"001","lastUpdateTime":"2017-06-03 15:59:59","lastUpdateUser":"001","isDeleted":false,"exerciseId":"8c330e9bb90e4904aeca29aa8e5c6997","optionContent":"北京"},{"optionId":"danxuan002","optionNo":"B","rankNo":2,"isRight":false,"createTime":"2017-06-03 15:59:59","createUser":"001","lastUpdateTime":"2017-06-03 15:59:59","lastUpdateUser":"001","isDeleted":false,"exerciseId":"8c330e9bb90e4904aeca29aa8e5c6997","optionContent":"上海"},{"optionId":"danxuan003","optionNo":"C","rankNo":3,"isRight":false,"createTime":"2017-06-03 15:59:59","createUser":"001","lastUpdateTime":"2017-06-03 15:59:59","lastUpdateUser":"001","isDeleted":false,"exerciseId":"8c330e9bb90e4904aeca29aa8e5c6997","optionContent":"广州"}]
     * exercisesId : 8c330e9bb90e4904aeca29aa8e5c6997
     * microCourseUri : http://art.test.internet.zhiwangyilian.com/images/artImg/null
     * createTime : 2017-06-03 15:59:59
     * exerciseExplainFileUri : http://art.test.internet.zhiwangyilian.com/images/artImg/75215
     * exerciseAnalysis : 解析这是解析
     * exerciseTypeCode : 30101
     */

    public boolean isDownload;
    public String exercisesTitle;
    public int sourceTypeCode;
    public String teacherName;
    public String analysisFileUri;
    public boolean isMistakesCollection;
    public String exercisesId;
    public String microCourseUri;
    public String createTime;
    public String exerciseExplainFileUri;
    public String exerciseAnalysis;
    public int exerciseTypeCode;
    public List<ExerciseOptionListBean> exerciseOptionList;

    public static class ExerciseOptionListBean {
        /**
         * optionId : danxuan001
         * optionNo : A
         * rankNo : 1
         * isRight : true
         * createTime : 2017-06-03 15:59:59
         * createUser : 001
         * lastUpdateTime : 2017-06-03 15:59:59
         * lastUpdateUser : 001
         * isDeleted : false
         * exerciseId : 8c330e9bb90e4904aeca29aa8e5c6997
         * optionContent : 北京
         */

        public String optionId;
        public String optionNo;
        public int rankNo;
        public boolean isRight;
        public String createTime;
        public String createUser;
        public String lastUpdateTime;
        public String lastUpdateUser;
        public boolean isDeleted;
        public String exerciseId;
        public String optionContent;
    }
}
