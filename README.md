#  version 0.0.2

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