package com.example.wildlife.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Value("${user.phone.number}")
    private String toPhoneNumber;

    public boolean sendSms(String message) {
        try {
            logger.info("Sending SMS to {}: {}", toPhoneNumber, message);
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();
            logger.info("SMS sent successfully to {}", toPhoneNumber);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", toPhoneNumber, e.getMessage(), e);
            return false;
        }
    }
}