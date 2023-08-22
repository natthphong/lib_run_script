#  version 0.0.2
# mvn install

artifactId=flyway<br>
groupId=com.tar<br>
version=0.0.1<br>

<h3> value can custome in yml </h3>
tar:<br>
&nbsp;&nbsp;migration-location: "db/migrations"<br>
&nbsp;&nbsp;database-type: MYSQL<br>
&nbsp;&nbsp;migration-enable: true<br>
&nbsp;&nbsp;migration-show: false<br>
&nbsp;&nbsp;start-with-version: 1 <br>
&nbsp;&nbsp;stop-with-version: 12 <br>
&nbsp;&nbsp;ignore-version: [1,3,11] <br>
