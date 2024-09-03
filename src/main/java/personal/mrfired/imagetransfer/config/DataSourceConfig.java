package personal.mrfired.imagetransfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource(@Value("${spring.datasource.url}") String url,
                                    @Value("${spring.datasource.username}") String username,
                                    @Value("${SPRING_DATASOURCE_PASSWORD_FILE}") String datasourcePasswordFile)
            throws IOException {
        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(Files.readString(Path.of(datasourcePasswordFile)))
                .build();
    }
}
