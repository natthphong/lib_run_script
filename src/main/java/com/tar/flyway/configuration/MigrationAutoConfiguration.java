package com.tar.flyway.configuration;

import com.tar.flyway.MigrationRunner;
import com.tar.flyway.model.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MigrationAutoConfiguration {
    @Value("${tar.migration.scripts-location:db/migrations/}")
    private String migrationScriptsLocation;
    @Value("${tar.migration.database-type}")
    private DatabaseType databaseType;
    @Bean
    @ConditionalOnMissingBean
    public MigrationRunner migrationRunner(DataSource dataSource) {
        if (migrationScriptsLocation==null || migrationScriptsLocation.length()==0)
            migrationScriptsLocation= "/db/migrations";
        if (databaseType==null ||  databaseType.name().length()==0)
            databaseType=DatabaseType.MYSQL;
        System.out.println(" migrationRunner ");
         return new MigrationRunner(dataSource, migrationScriptsLocation, databaseType);
    }
}