package ru.lyuchkov.bank_course_work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BankCourseWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCourseWorkApplication.class, args);
    }

}
