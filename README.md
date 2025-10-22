# deathLineServer

## Description

deathLineServer is the backend component of [deathLineApp](https://github.com/El-GuPo/deathLineApp).

## Features

- **User Authentication**
    - registration and login system

- **Deadline Management**
    - Automatic deadlines update

## Technology Stack

- **Framework**: Spring Boot 3.4.2
- **Language**: Java 21
- **Database**: PostgreSQL
- **Build Tool**: Gradle 8.12.1
- **Documentation**: SpringDoc OpenAPI

## Prerequisites

- JDK 21 or higher
- PostgreSQL
- Gradle 8.12.1+

## Building and Running

1. Clone the repository:
```bash
git clone https://github.com/El-GuPo/deathLineServer.git
```

2. Configure your database settings in `application.properties`

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew bootRun
```

## Configuration

The application requires several configuration parameters:

- Database configuration
- Security settings

Please ensure all necessary credentials are properly configured before running the application.

## Testing

The project includes a comprehensive test suite. To run the tests:

```bash
./gradlew test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is part of the deathLine platform. For licensing information, please contact the repository owners.

## Contact

For more information about the deathLine platform, visit [deathLineApp](https://github.com/El-GuPo/deathLineApp).
