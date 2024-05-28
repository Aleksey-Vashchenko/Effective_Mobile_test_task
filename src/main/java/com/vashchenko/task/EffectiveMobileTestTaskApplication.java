package com.vashchenko.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class EffectiveMobileTestTaskApplication implements CommandLineRunner{

   @Autowired
   private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(EffectiveMobileTestTaskApplication.class, args);
    }

        @Override
        public void run(String... args) throws Exception {
            try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/start_script.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)
                )){
                String sql = reader.lines().collect(Collectors.joining("\n"));
                dataSource.getConnection().prepareStatement(sql).executeUpdate();
            }
            catch (Exception e){
                log.error(e.getMessage());
                throw new Exception(e);
            }
        }
}
