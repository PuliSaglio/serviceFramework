package br.com.musiclink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.musiclink",
        "br.com.serviceframework"
})
@EntityScan({
        "br.com.servicelink.domain.entity",
        "br.com.serviceframework.domain.entity"
})
@EnableJpaRepositories({
        "br.com.musiclink.repository",
        "br.com.serviceframework.repository"
})
public class MusicLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicLinkApplication.class, args);
    }
}