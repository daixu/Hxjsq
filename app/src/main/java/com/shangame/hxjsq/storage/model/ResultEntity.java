package com.shangame.hxjsq.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ResultEntity {

    @Id(autoincrement = true)
    private Long id;
    private String name; // 名称
    private String name2; // 名称
    private String name3; // 名称
    private int duplicate; // 打坨
    private int duplicate2; // 打坨
    private int duplicate3; // 打坨
    private double accumulative; // 累计
    private double accumulative2; // 累计
    private double accumulative3; // 累计
    private double score; // 得分
    private double score2; // 得分
    private double score3; // 得分
    private long datetime; // 时间
    private String date;
    private long number;
    @Generated(hash = 1434462058)
    public ResultEntity(Long id, String name, String name2, String name3,
            int duplicate, int duplicate2, int duplicate3, double accumulative,
            double accumulative2, double accumulative3, double score, double score2,
            double score3, long datetime, String date, long number) {
        this.id = id;
        this.name = name;
        this.name2 = name2;
        this.name3 = name3;
        this.duplicate = duplicate;
        this.duplicate2 = duplicate2;
        this.duplicate3 = duplicate3;
        this.accumulative = accumulative;
        this.accumulative2 = accumulative2;
        this.accumulative3 = accumulative3;
        this.score = score;
        this.score2 = score2;
        this.score3 = score3;
        this.datetime = datetime;
        this.date = date;
        this.number = number;
    }
    @Generated(hash = 369396957)
    public ResultEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName2() {
        return this.name2;
    }
    public void setName2(String name2) {
        this.name2 = name2;
    }
    public String getName3() {
        return this.name3;
    }
    public void setName3(String name3) {
        this.name3 = name3;
    }
    public int getDuplicate() {
        return this.duplicate;
    }
    public void setDuplicate(int duplicate) {
        this.duplicate = duplicate;
    }
    public int getDuplicate2() {
        return this.duplicate2;
    }
    public void setDuplicate2(int duplicate2) {
        this.duplicate2 = duplicate2;
    }
    public int getDuplicate3() {
        return this.duplicate3;
    }
    public void setDuplicate3(int duplicate3) {
        this.duplicate3 = duplicate3;
    }
    public double getAccumulative() {
        return this.accumulative;
    }
    public void setAccumulative(double accumulative) {
        this.accumulative = accumulative;
    }
    public double getAccumulative2() {
        return this.accumulative2;
    }
    public void setAccumulative2(double accumulative2) {
        this.accumulative2 = accumulative2;
    }
    public double getAccumulative3() {
        return this.accumulative3;
    }
    public void setAccumulative3(double accumulative3) {
        this.accumulative3 = accumulative3;
    }
    public double getScore() {
        return this.score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public double getScore2() {
        return this.score2;
    }
    public void setScore2(double score2) {
        this.score2 = score2;
    }
    public double getScore3() {
        return this.score3;
    }
    public void setScore3(double score3) {
        this.score3 = score3;
    }
    public long getDatetime() {
        return this.datetime;
    }
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public long getNumber() {
        return this.number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
    
}
