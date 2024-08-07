package com.tar.flyway;

import com.tar.flyway.exception.TarException;
import com.tar.flyway.model.DatabaseType;
import com.tar.flyway.model.Migration;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MigrationRunner {
    private DataSource dataSource;
    private String migrationScriptsLocation;

    private DatabaseType databaseType;

    private Boolean showScript;

    private Integer start;

    private Integer stop;
    private List<Integer> ignoreVersion ;

    public MigrationRunner(DataSource dataSource, String migrationScriptsLocation,
                           DatabaseType databaseType, Boolean showScript,
                           Boolean enable, Integer start , Integer stop,
                           List<Integer> ignoreVersion) {
        Assert.notNull(dataSource, "DataSource Must be not null");
        Assert.notNull(dataSource, "migrationScriptsLocation Must be not null");
        Assert.notNull(dataSource, "databaseType Must be not null");
        this.dataSource = dataSource;
        this.migrationScriptsLocation = migrationScriptsLocation;
        this.databaseType = databaseType;
        this.showScript = showScript;
        this.start = start;
        this.stop = stop;
        this.ignoreVersion= ignoreVersion;
        if (enable) this.runMigrations();
    }


    public void setMigrationScriptsLocation(String migrationScriptsLocation) {
        this.migrationScriptsLocation = migrationScriptsLocation;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public void runMigrations() {
        try (Connection connection = dataSource.getConnection()) {
            List<Migration> migrations = loadMigrations();
            migrations = migrations.stream().sorted(Comparator.comparing(Migration::getVersionInt)).toList();
            stop=stop==-1?migrations.size():stop;
            for (Migration migration : migrations) {
                if (start <= migration.getVersionInt() && migration.getVersionInt()<=stop && !ignoreVersion.contains(migration.getVersionInt())) executeMigration(connection, migration);
            }
            if (stop>0) System.out.println("=".repeat(100) + "COMPLETE MIGRATIONS BY TAR" + "=".repeat(100));
        } catch (SQLException | IOException e) {
            throw new TarException(e.getMessage());
        }
    }

    private List<Migration> loadMigrations() throws IOException {
        List<Migration> migrations = new ArrayList<>();
        Enumeration<URL> resources = getClass().getClassLoader().getResources(migrationScriptsLocation);
        try {
            if (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String[] filenames = readScriptFromFile(resource.openStream()).split("\n");
                for (var filename : filenames) {
                    if (filename.endsWith(".sql")) {
                        Enumeration<URL> temp = getClass().getClassLoader().getResources(migrationScriptsLocation + "/" + filename);
                        URL tempResource = temp.nextElement();
                        String script = readScriptFromFile(tempResource.openStream());
                        Migration migration = parseMigrationFileName(filename);
                        migration.setScript(script);
                        migrations.add(migration);
                    }
                }
            }
        } finally {
            if (resources instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) resources).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return migrations;
    }

    private Migration parseMigrationFileName(String filename) {
        Pattern pattern = Pattern.compile("V(\\d+)_(.+)\\.sql");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.matches()) {
            String version = matcher.group(1);
            String description = matcher.group(2).replace("_", " ");
            return new Migration(version, description, null);
        }
        return new Migration(filename, filename, null);
    }


    private String readScriptFromFile(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }


    private void executeMigration(Connection connection, Migration migration) throws SQLException {
        if (showScript) System.out.println("Migration with : V" + migration.getVersion() + "_" + migration.getDescription());
        try (Statement statement = connection.createStatement()) {
            if (databaseType != DatabaseType.MYSQL) {
                statement.execute(migration.getScript());
                if (showScript)
                    System.out.println("Executed migration: " + migration.getScript());
            } else {
                String[] statements = migration.getScript().split(";");
                for (String sql : statements) {
                    if (!sql.trim().isEmpty()) {
                        statement.execute(sql);
                        if (showScript)
                            System.out.println("Executed MYSQL statement: " + sql.trim());
                    }
                }
            }
        }
    }


}
