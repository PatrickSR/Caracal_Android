package com.patrick.caracal.event;

import android.os.Bundle;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 15920 on 2016/7/31.
 */
public class StartFragmentEvent implements BEvent {

    public SupportFragment targetFragment;

    public Bundle bundle;

    public StartFragmentEvent(SupportFragment targetFragment){
        this.targetFragment = targetFragment;
    }

    public StartFragmentEvent(SupportFragment targetFragment,Bundle b){
        this.targetFragment = targetFragment;
        this.bundle = b;
    }
}
