package bcc.sipas.seeder;

import bcc.sipas.app.ortu.repository.OrangtuaRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Slf4j
@Configuration
public class Seeder {

    @Autowired
    private OrangtuaRepository orangtuaRepository;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public ConnectionFactoryInitializer connectionFactoryInitializer(){
        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        log.info("Executing init.sql");
        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(
                        new ClassPathResource("init.sql")
                )
        );
        orangtuaRepository.count()
                .subscribe(
                        (v) -> {
                            if(v == 0){
                                log.info("Orang tua database is empty");
                            } else {
                                log.info("Orang tua database table already exist");
                            }
                        },
                        (e) -> {
                            log.error("Error on executing init.sql with message {}", e.getMessage());
                        }
                );
        return initializer;
    }
}
