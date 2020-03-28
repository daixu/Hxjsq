package com.shangame.hxjsq.pojo;

import java.util.List;

public class ResultBean {

    public String date;
    public List<ResultDataBean> resultData;

    public static class ResultDataBean {
        public Long id;
        public String name; // 名称
        public String name2; // 名称
        public String name3; // 名称
        public int duplicate; // 打坨
        public int duplicate2; // 打坨
        public int duplicate3; // 打坨
        public double accumulative; // 累计
        public double accumulative2; // 累计
        public double accumulative3; // 累计
        public double score; // 得分
        public double score2; // 得分
        public double score3; // 得分
        public long datetime; // 时间
        public long number;
    }
}
