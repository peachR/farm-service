package com.yiyi.farm.tool;

import java.io.Serializable;

public class Pair implements Serializable {
    int first;
    int all;

    public Pair(int first, int all) {
        this.first = first;
        this.all = all;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
