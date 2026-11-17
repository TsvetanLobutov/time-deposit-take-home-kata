# XA Bank Time Deposit

## Run locally

Requirements: Java 17+ and Maven.

From this directory, start the application:

```shell
mvn spring-boot:run
```

By default the application uses a file-backed H2 database and seeds example deposits when the database is empty. Set `time-deposit.sample-data.enabled=false` to disable seeding. To use PostgreSQL instead, provide `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`.

## API

Open the Swagger UI at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html). The contract is also available at [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml).

The API intentionally exposes exactly two operations:

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/api/time-deposits` | Retrieve all deposits, including each deposit's withdrawals. |
| `PUT` | `/api/time-deposits/balances` | Apply one monthly balance update to every stored deposit. |

You can also try them with curl:

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

For this assessment iteration, GitHub Copilot's coding-agent experience was used in the repository. The existing application implementation predates this iteration; Copilot was used to review it against the requirements, inspect test coverage, and prepare the submission documentation. The persistent repository instructions are in `../.github/copilot-instructions.md`; they provide reusable constraints for Copilot in supported IDE and agent workflows. To reproduce the setup, open this repository in a supported Copilot client (or coding-agent environment), keep that instruction file in place, and use the normal Maven test command to verify changes. No separate agent framework, external model API, or additional runtime dependency is required.

The repository instructions capture the key task rules: preserve the shared domain API and calculator behavior, expose only the two required operations, prefer the existing architecture, and run tests after behavior changes.
