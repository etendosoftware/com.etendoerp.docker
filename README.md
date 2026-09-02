# Getting Started

This module aims to provide a simple way to deploy a Docker container with the Etendo ERP.

## Requirements

- Docker
- Docker Compose

## How to extend

On a module level, create a new folder called compose and add a <<your-package>>.yml file with the configuration of the new service.

Example:

module/com.etendoerp.busybox/compose/com.etendoerp.busybox.yml
```yaml
services:
  busybox:
    image: busybox:latest
    command: [ "/bin/busybox", "watch", "ls", "-l" ]
```

## Running

Execute:

```bash
./gradlew resources.up
```

### Local pgvector database

The database image and data directory are configurable via `etendo.db.image` and
`etendo.db.data.directory` (declared in `config.gradle`, defaults `postgres:16` and `db`).
Set them in the root `gradle.properties` — **not as `-P` command-line flags**: `resources.up`
generates `.env` from the `gradle.properties` file on disk (`generateEnvFile` in
`tasks.gradle`), so a `-P` override never reaches the Docker Compose environment.

```properties
# gradle.properties
etendo.db.image=pgvector/pgvector:pg16
etendo.db.data.directory=db-pgvector
```

Then run normally:

```bash
./gradlew resources.up
```

Use a new data directory to preserve any existing local database volume. The pgvector
image makes the extension available but does not activate it in a database. Activation
remains an explicit application operation. To return to the previous local database,
stop the container, remove those two lines (or reset them to `postgres:16` and `db`)
from `gradle.properties`, and run `resources.up` again.

This command will search for all resources configured and start the containers.

## Stopping

Execute:

```bash
./gradlew resources.stop
```

This command will stop all containers.

## Down

Execute:

```bash
./gradlew resources.down
```

This command will stop and remove all containers.


