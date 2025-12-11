package br.com.musiclink.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public GenerativeModel generativeModel() throws IOException {
        VertexAI vertexAI = new VertexAI("servicelink", "us-central1");

        return new GenerativeModel("gemini-1.0-pro", vertexAI);
    }

}
