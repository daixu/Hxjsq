package com.shangame.hxjsq.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ScoreRecordEntity {

    @Id(autoincrement = true)
    private Long id;
    private String name; // 名称
    private String name2; // 名称
    private String name3; // 名称
    private double score; // 得分
    private double score2; // 得分
    private double score3; // 得分
    private long number;
    @Generated(hash = 401715259)
    public ScoreRecordEntity(Long id, String name, String name2, String name3,
            double score, double score2, double score3, long number) {
        this.id = id;
        this.name = name;
        this.name2 = name2;
        this.name3 = name3;
        this.score = score;
        this.score2 = score2;
        this.score3 = score3;
        this.number = number;
    }
    @Generated(hash = 2050842670)
    public ScoreRecordEntity() {
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
    public long getNumber() {
        return this.number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
}
