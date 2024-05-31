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



