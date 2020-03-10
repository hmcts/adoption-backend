# Adoption Backend

Backend springboot application for Adoption project.

### Contents:
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)

## Prerequisites:

- [Java 11](https://openjdk.java.net/projects/jdk/11/)

## Getting Started

The project uses [Gradle](https://gradle.org) as a build tool. It already contains `./gradlew` wrapper script, so there's no need to install Gradle.

To build the project execute the following command:

```shell script
  ./gradlew build
```
---
### Running the application from IDE
 
Ensure the Spring Boot is started with local profile

```
spring.profiles.active=local
```

### Running the application in Docker

Run the following command to start the application in docker

```bash
  ./bin/run-in-docker.sh
```

The above command assumes the following are set as variables `DB_PASSWORD=${DB_PASSWORD}`, `S2S_URL=${S2S_URL}`, `S2S_SECRET=${S2S_SECRET}`

To bring the application down and clear volumes run:

```shell script
docker-compose down -v
```

---

Either of the above commands will start the API on `localhost:4550`.

In order to test if the application is up, you can call its health endpoint:

```shell script
  curl http://localhost:4550/health
```

You should get a response similar to this:

```json
  {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
