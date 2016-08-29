package com.patrick.caracal.event;

import com.wilddog.client.WilddogError;

/**
 * Created by Patrick on 16/8/23.
 */
public class AuthEvent {

    public enum STATE{
        REG_SUCCESS,
        REG_FAIL,
        LOGIN_SUCCESS,
        LOGIN_FAIL
    }

    public STATE state;

    public WilddogError wilddogError;

    public AuthEvent(STATE state){
        this.state = state;
    }

    public AuthEvent(STATE state, WilddogError wilddogError) {
        this.state = state;
        this.wilddogError = wilddogError;
    }
}
