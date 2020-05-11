### Student Registration System (SRS)
### - Jordan Beth

This SRS project allows students to register in a two-step process. The first step requires a student to enter personal information about themselves. The second step will have the student enter personal residential information. During each step of the registration process, the fields will be validated. If there are errors, the student will be notified and allowed to modified the fields. Only valid registrations will be processed by the system.

The build automation is managed by Maven. 

To build and package the project run:

``` 
mvn package
```

To deploy the project to a running instance of Wildfly, run:

```
mvn clean package wildfly:deploy -DskipTests
```

To run the tests run:

```
mvn test
```

Once the project is built and deployed to Wildfly, it can be accessed via a web browser at http://127.0.0.1:8080/StudentRegistrationSystem/index.jsp

### JMS Configuration

A permanently running MessageDrivenBean receives all of the messages published to the
RegCourseTopic and prints it to standard output (console).

Add the following JMS topic configuration to the standalone-full.xml:

```
<jms-topic name="RegCourseTopic" entries="topic/RegCourseTopic java:jboss/exported/jms/topic/RegCourseTopic"/>
```

Start the wildfly server with the full configuration:

```
 ~/servers/wildfly/bin/standalone.sh -c standalone-full.xml &
```

#### Test Users
1.
- user: testtest
- password: password

2. 
- user: testtest1
- password: password

#### Data Source

data-source add --jndi-name=java:jboss/datasources/H2_784_JNDI --name=H2_784_DS --connection-url=jdbc:h2:~${PATH_TO_DB}/H2_784_DB;DB_CLOSE_DELAY=-1 --driver-name=h2 

#### Test Cases
Run the JPQL test with:

```
mvn clean package -Dtest=JPQLTest test
```

Run the Criteria API test with:

```
mvn clean package -Dtest=CriteriaAPITest test
```

#### Registrar Course Bean Client (SHORT | LONG)
```
mvn exec:java -Dexec.mainClass="edu.jhu.jbeth.jta.RegistrarCourseBeanClient" -Dexec.args="SHORT" -Dexec.classpathScope="test"
```