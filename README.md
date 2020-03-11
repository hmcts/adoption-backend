# Adoption Backend

[![Build Status](https://travis-ci.com/hmcts/adoption-backend.svg?branch=master)](https://travis-ci.com/hmcts/adoption-backend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Aadoption-backend&metric=alert_status)](https://sonarcloud.io/dashboard?id=uk.gov.hmcts.reform%3Aadoption-backend)

### Contents:

- [Prerequisites](#prerequisites)
- [Running application](#running-the-application)
- [Upload CCD definition files to data store](#ccd-definition-files)

## Pre-requisites

 * Java 11 installed
 * Git installed
 * [Docker](https://www.docker.com/products/docker-desktop)


## Running the application
To clone the project:

```markdown
git clone https://github.com/hmcts/adoption-backend.git
```

### Running application in IDE

* Open Project with intelliJ IDE
* Setup ``Run Configuration`` for project. (IntelliJ _should_ automatically detect the `Application.java` file)
* For ``Environment variables`` use `spring.profiles.active=local`
* Click on ``Run`` and the application will be started.

### Running application in Docker

To start the application in docker run the following command:

```bash
  ./bin/run-in-docker.sh
```

To bring the application down and clear volumes run:

```shell script
docker-compose down -v
```

### Verify Application is Up & Running

You can verify application is running by visiting ``http://localhost:4550/health`` from a browser or by using ``curl`` via the CLI.
You should get a response similar to this:

```json
  {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}
```

## CCD definition files

To upload definition files run:

```bash
./bin/configurer/import-ccd-definition.sh
```

This will upload a ccd definition to ccd definition store api.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

### Contents:
