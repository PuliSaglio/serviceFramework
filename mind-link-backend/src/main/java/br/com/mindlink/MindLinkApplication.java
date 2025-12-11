package br.com.mindlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.mindlink",      // Scaneia esse projeto (ServiceLink)
        "br.com.serviceframework"  // Scaneia o Framework
})
@EntityScan({
        "br.com.mindlink.domain.entity",
        "br.com.serviceframework.domain.entity"
})
@EnableJpaRepositories({
        "br.com.mindlink.repository",
        "br.com.serviceframework.repository"
})
public class MindLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindLinkApplication.class, args);
    }
}