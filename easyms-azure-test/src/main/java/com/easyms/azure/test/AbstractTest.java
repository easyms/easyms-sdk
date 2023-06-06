package com.easyms.azure.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@ComponentScan(basePackages = "com.easyms.azure.test")
public class AbstractTest {
}

