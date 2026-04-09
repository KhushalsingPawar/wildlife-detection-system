package com.example.wildlife.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    @Value("${twilio.phone.number}")
    private String fromNumber;

    @Value("${user.phone.number}")
    private String toNumber;

    public boolean sendSms(String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            System.out.println("📩 SMS SID: " + message.getSid());
            return true;

        } catch (Exception e) {
            System.out.println("❌ Twilio SMS Failed: " + e.getMessage());
            return false;
        }
    }
}