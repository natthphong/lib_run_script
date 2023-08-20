package com.tar.flyway.configuration;

import com.tar.flyway.MigrationRunner;
import com.tar.flyway.model.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "tar")
public class MigrationAutoConfiguration {

    private String migrationLocation = "db/migrations";
    private DatabaseType databaseType = DatabaseType.MYSQL;
    private Boolean migrationEnable = true;
    private Boolean migrationShow = true;

    public String getMigrationLocation() {
        return migrationLocation;
    }

    public void setMigrationLocation(String migrationLocation) {
        this.migrationLocation = migrationLocation;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public Boolean getMigrationEnable() {
        return migrationEnable;
    }

    public void setMigrationEnable(Boolean migrationEnable) {
        this.migrationEnable = migrationEnable;
    }

    public Boolean getMigrationShow() {
        return migrationShow;
    }

    public void setMigrationShow(Boolean migrationShow) {
        this.migrationShow = migrationShow;
    }

    @Bean
    @ConditionalOnMissingBean
    public MigrationRunner migrationRunner(DataSource dataSource) {
        if (migrationLocation.charAt(0) == '/') migrationLocation = migrationLocation.substring(1);
        return new MigrationRunner(dataSource, migrationLocation, databaseType, migrationShow, migrationEnable);
    }
}