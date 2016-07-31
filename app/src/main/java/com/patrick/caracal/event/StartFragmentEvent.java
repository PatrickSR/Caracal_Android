package com.patrick.caracal.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 15920 on 2016/7/31.
 */
public class StartFragmentEvent implements BEvent {

    public SupportFragment targetFragment;

    public StartFragmentEvent(SupportFragment targetFragment){
        this.targetFragment = targetFragment;
    }
}
