package cc.sika.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cc.sika.ai.mapper"})
public class SikaAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SikaAiApplication.class, args);
    }

}
