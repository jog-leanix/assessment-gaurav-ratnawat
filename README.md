# Assessment Gaurav Ratnawat

We want to create an interactive application composed of a backend and frontend. Your task is to architect and develop the backend part of it. It is to be Kotlin based, using Quarkus as a framework and using Postgres for persistence.

🎯 Core Features: The core feature on the frontend involves designing a 50x50 grid interface. Each cell in the grid is interactive and can take on a value (the default value is 0). When a user clicks on any particular cell, the values in all the cells present in the same row and column get incremented by 1. If the cell was empty, it should now have a value of 1.

A secondary trait of the interactive grid is to identify the Fibonacci sequence. If the application detects 5 consecutive numbers from the Fibonacci sequence either horizontally or vertically, the cells carrying those values be cleared out (back to the value 0).

⚠️ Please make sure to have a way to show it working, either with logs, tests or with a small frontend.

⏰ We fully understand that many of our candidates have significant commitments and may not be able to dedicate extensive time to this coding challenge. If you find yourself constrained for time, please do not hesitate to prioritize those features and aspects of the application that best showcase your strengths, skills, and areas of expertise. In that case, feel free to list and explain potential enhancements.

# Grid Game Backend - Technical Assessment

A Kotlin-based backend service for an interactive grid game built with Quarkus and PostgreSQL.

## Overview

This project implements a 50x50 interactive grid where:
- Each cell starts with value 0
- Clicking a cell increments all cells in the same row and column by 1
- When 5 consecutive Fibonacci numbers are detected (horizontally/vertically), those cells are reset to 0

## Tech Stack

- Kotlin 1.9+
- Quarkus 3.x
- PostgreSQL 15+
- Gradle
- JDK 17+

## Prerequisites

- JDK 17 or higher
- Docker and Docker Compose
- PostgreSQL 15 or higher

## Development Process & TDD Approach

This project was developed following Test-Driven Development (TDD) principles. You can follow the development 
process through the commit history.

Key commits showcase the TDD cycle:


1. test: Writing failing tests
2. impl: Making tests pass
3. refactor: Improving code structure

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/jog-leanix/assessment-gaurav-ratnawat.git
cd assessment-gaurav-ratnawat
```

2. Start the application and database using Docker Compose:
```bash
docker-compose up -d
```

This will:
- Start PostgreSQL container
- Build and start the application container
- Set up the required networking between containers
- Expose the application on port 8080

3. Stop all services:
```bash
docker-compose down
```

4. View logs:
```bash
docker-compose logs -f app
```
5. Alternatively, for development, you can run just the database:
```bash
docker-compose up -d postgres
```

6. and to run the application in dev mode:
```bash
./gradlew quarkusDev
```

The application will be available at `http://localhost:8080`

## API Endpoints

### Grid Operations

- Create new grid:
```bash
curl -X POST http://localhost:8080/api/grids
```

- Get grid by ID:
```bash
curl http://localhost:8080/api/grids/{id}
```

- Handle cell click:
```bash
curl -X POST http://localhost:8080/api/grids/{id}/click \
  -H "Content-Type: application/json" \
  -d '{"row": 0, "column": 0}'
```

## Running Tests

Execute unit tests:
```bash
./gradlew test
```

## Design Decisions

1. **Domain-Driven Design**
   - Rich domain models (`Grid` and `Cell`) encapsulating business logic
   - Validation at domain level
   - Clear separation of concerns

2. **Clean Architecture**
   - RESTful API design
   - Exception handling with custom exceptions
   - Transactional boundaries in service layer

3. **Testing Strategy**
   - Unit tests for domain logic
   - Integration tests using test containers
   - High test coverage for critical paths

## Future Improvements

1. WebSocket support for real-time updates
2. User authentication and session management
3. Game statistics and leaderboard
4. Query optimization for large grids
5. React/Angular frontend implementation
6. Caching layer for frequently accessed grids

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/gridgame/
│   │       ├── model/
│   │       │   ├── Grid.kt
│   │       │   └── Cell.kt
│   │       ├── service/
│   │       │   └── GridService.kt
│   │       ├── resource/
│   │       │   └── GridResource.kt
│   │       └── exception/
│   └── resources/
│       └── application.properties
└── test/
    ├── kotlin/
    │   └── com/gridgame/
    │       ├── unit/
    │       └── integration/
    └── resources/
        └── application-test.properties
```

## Author

Gaurav Ratnawat

## License

MIT
