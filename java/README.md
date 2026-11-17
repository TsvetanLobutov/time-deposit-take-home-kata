# XA Bank Time Deposit

## Run locally

Requirements: Java 17+ and Maven.

From this directory, start the application:

```shell
mvn spring-boot:run
```

By default, the application uses a file-backed H2 database and seeds example deposits when the database is empty. Set `time-deposit.sample-data.enabled=false` to disable seeding.

To use PostgreSQL, create a database and set the connection variables before starting the application. The PostgreSQL JDBC driver is included as a runtime dependency; specify the driver because the default configuration selects H2.

PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/time_deposits"
$env:SPRING_DATASOURCE_USERNAME = "your_username"
$env:SPRING_DATASOURCE_PASSWORD = "your_password"
$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME = "org.postgresql.Driver"
mvn spring-boot:run
```

Bash:

```shell
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/time_deposits
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
mvn spring-boot:run
```

## API

Open [Swagger UI](http://localhost:8080/swagger-ui.html) after starting the application. To invoke either operation, expand it, select **Try it out**, then **Execute**. The GET operation takes no parameters and returns the deposits and their withdrawals. The PUT operation takes no request body and applies one monthly balance update to every stored deposit; a successful request returns HTTP `202 Accepted`.

The OpenAPI contract is available at [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml). The API intentionally exposes exactly two operations:

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/api/time-deposits` | Retrieve all deposits, including each deposit's withdrawals. |
| `PUT` | `/api/time-deposits/balances` | Apply one monthly balance update to every stored deposit. |

Equivalent curl commands:

```shell
curl http://localhost:8080/api/time-deposits
curl -X PUT http://localhost:8080/api/time-deposits/balances
```

The persistence layer uses the conventional snake_case SQL names `time_deposits` and `withdrawals`, with a required `time_deposit_id` foreign key from each withdrawal to its deposit.

## Interest-rate assumption

The plan percentages are treated as annual rates, prorated by 12 for each monthly update. This preserves the existing `TimeDepositCalculator.updateBalance` behavior and its established 30-day, 45-day, and one-year boundaries. Basic deposits accrue at 1% annually after day 30, student deposits at 3% annually after day 30 and before day 366, and premium deposits at 5% annually after day 45.

## Tests

Run the test suite with:

```shell
mvn test
```

The PostgreSQL persistence test uses Testcontainers and runs when Docker is available; it is configured to be skipped when Docker is unavailable.

## AI-assisted development

The AI-assisted review and documentation work for this submission used GitHub Copilot's coding-agent experience and the repository-specific rules in `../.github/copilot-instructions.md`. Those rules preserve the shared domain API and calculator behavior, constrain the API to the two required operations, and capture the architecture and test expectations. To reproduce the setup, open the repository in a supported Copilot coding-agent or IDE client, keep that instruction file in place, use it to review the requirements and tests, and run `mvn test` to verify behavior.

Copilot was used in this submission iteration to compare the implementation and tests with the requirements and to prepare the documentation. The application implementation predates this review; the core application code was not generated as part of this AI-assisted documentation pass. No separate agent framework, external model API, or additional runtime dependency is required.
