package com.learn.srb.sms.server;

public interface SmsService {
    void sendMsg(String mobile, Integer type);
}
