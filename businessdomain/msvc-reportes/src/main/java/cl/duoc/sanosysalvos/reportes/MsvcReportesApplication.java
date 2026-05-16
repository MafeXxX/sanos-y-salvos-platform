package cl.duoc.sanosysalvos.reportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcReportesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsvcReportesApplication.class, args);
    }
}
