package com.yiyi.farm.tool;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/11.
 */
public class PosterityStatistics implements Serializable {
    int total = 0;
    int valid = 0;
    public PosterityStatistics(int total,int valid){
        this.total = total;
        this.valid = valid;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
