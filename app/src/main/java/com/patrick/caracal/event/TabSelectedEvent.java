package com.patrick.caracal.event;

/**
 * Created by 15920 on 2016/7/31.
 */
public class TabSelectedEvent implements BEvent {
    public int position;

    public TabSelectedEvent(int position) {
        this.position = position;
    }
}
