package com.example.wildlife.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    private SmsService smsService = new SmsService();

    @Test
    public void testSendSms_Success() {
        ReflectionTestUtils.setField(smsService, "fromPhoneNumber", "+1234567890");
        ReflectionTestUtils.setField(smsService, "toPhoneNumber", "+0987654321");
    }
}