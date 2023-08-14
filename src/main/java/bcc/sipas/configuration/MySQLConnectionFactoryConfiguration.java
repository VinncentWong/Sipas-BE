package bcc.sipas.configuration;

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySQLConnectionFactoryConfiguration {

    @Value("${db.r2dbc.username}")
    private String username;
    @Value("${db.r2dbc.password}")
    private String password;
    @Value("${db.r2dbc.url}")
    private String url;

    @Value("${db.r2dbc.host}")
    private String host;

    @Value("${db.r2dbc.port}")
    private String port;

    @Value("${db.r2dbc.database}")
    private String db;

    @Bean
    public MySqlConnectionFactory mysqlConnectionFactory(){
        return MySqlConnectionFactory.from(
                MySqlConnectionConfiguration.builder()
                        .username(this.username)
                        .password(this.password)
                        .host(this.host)
                        .port(Integer.parseInt(this.port))
                        .database(db)
                        .build()
        );
    }
}
