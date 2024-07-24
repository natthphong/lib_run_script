#  version 0.0.2

# setup
```xml
<!-- https://mvnrepository.com/artifact/io.github.natthphong/tarway -->
<dependency>
    <groupId>io.github.natthphong</groupId>
    <artifactId>tarway</artifactId>
    <version>0.0.2</version>
</dependency>
```
```groovy
// https://mvnrepository.com/artifact/io.github.natthphong/tarway
implementation group: 'io.github.natthphong', name: 'tarway', version: '0.0.2'
```

## Value can be customized in 
```yaml
tar:
  migration-location: "db/migrations"
  database-type: MYSQL
  migration-enable: true
  migration-show: false
  start-with-version: 1
  stop-with-version: 12
  ignore-version: [1, 3, 11]