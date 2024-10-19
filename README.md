## Prerequisites

- **Java JDK** (version 8 or later)
- **Gradle** (or use the included Gradle wrapper)
- **SQLite** (installed locally for development)

## Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/Isaiah512/TaskManagement.git
    cd task-manager
    ```

2. **Build the project** using Gradle:
    ```bash
    ./gradlew build
    ```

3. **Run the application**:
    ```bash
    ./gradlew run
    ```

## Database

The project uses SQLite for task persistence. When the application is run, it automatically creates an `tasks.db` SQLite database file in the project directory and a `tasks` table with the following schema:

```sql
CREATE TABLE IF NOT EXISTS tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL
);
```
