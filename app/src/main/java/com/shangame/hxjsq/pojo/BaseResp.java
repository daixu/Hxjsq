package com.shangame.hxjsq.pojo;

public class BaseResp {

    public int code;
    public String msg;

    public boolean isOk() {
        return code == 1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseResp{");
        sb.append("code=").append(code);
        sb.append(",msg=").append(msg).append("\'");
        sb.append("}");
        return sb.toString();
    }
}