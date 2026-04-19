package com.example.wildlife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Wildlife Application Tests")
class WildlifeApplicationTests {

    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
    }

    @Test
    @DisplayName("Application should start without exceptions")
    void applicationStartsSuccessfully() {
        assert true;
    }

}