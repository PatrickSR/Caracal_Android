package com.patrick.caracal.event;

import com.wilddog.client.WilddogError;

/**
 * Created by Patrick on 16/8/23.
 */
public class WRegisterEvent {

    public enum STATE{
        SUCCESS,
        FAIL
    }

    public STATE state;

    public WilddogError wilddogError;

    public WRegisterEvent(STATE state){
        this.state = state;
    }

    public WRegisterEvent(STATE state, WilddogError wilddogError) {
        this.state = state;
        this.wilddogError = wilddogError;
    }
}
