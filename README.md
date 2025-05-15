# Bentham Voice API

## Overview

Bentham Voice API is a sophisticated voice agent system designed for a legal tech startup called Bentham. The system aims to automate legal tasks by providing an AI-powered voice interface that can act as a lawyer, interact with clients, provide consultations, and take necessary actions.

## Purpose

This API serves as the backbone for Bentham's voice-based legal assistant, enabling natural conversations between clients and an AI legal advisor. The system is designed to handle complex legal queries, provide guidance, and automate routine legal tasks through voice interaction.

## Architecture

The Bentham Voice API is built on AWS services with a Java-based backend, leveraging the following components:

### Core Components

1. **Speech-to-Text (STT)** - Amazon Transcribe
   - Converts client speech to text in real-time
   - Supports streaming transcription for natural conversation flow

2. **Natural Language Understanding (NLU) & Dialogue Management**
   - Amazon Bedrock with LLM models for advanced understanding
   - Context-aware conversation handling
   - Legal domain-specific knowledge processing

3. **Text-to-Speech (TTS)** - Amazon Polly
   - Converts AI responses to natural-sounding speech
   - Uses neural voices for lifelike interaction

4. **Voice Interface**
   - Handles microphone input and speaker output
   - Manages conversation flow and turn-taking

### Technical Stack

- **Backend**: Java 17+
- **Build System**: Gradle
- **Framework**: Spring Boot
- **Cloud Infrastructure**: AWS
- **APIs**: AWS SDK v2
- **Audio Processing**: Java Sound API

## Features

- Real-time voice conversation with legal AI assistant
- Legal consultation and guidance
- Document analysis and summarization
- Case assessment and recommendations
- Legal research assistance
- Automated document preparation
- Client intake processing
- Appointment scheduling
- Follow-up reminders and notifications

## Project Structure

```
bentham-voice-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bentham/
│   │   │           └── voiceagent/
│   │   │               ├── config/         # Configuration classes
│   │   │               ├── controller/     # REST and WebSocket controllers
│   │   │               ├── model/          # Domain models
│   │   │               ├── service/        # Service interfaces
│   │   │               │   └── impl/       # Service implementations
│   │   │               ├── util/           # Utility classes
│   │   │               └── BenthamVoiceApiApplication.java
│   │   └── resources/
│   │       ├── static/                     # Static resources
│   │       ├── templates/                  # Templates (if needed)
│   │       ├── application.yml             # Application configuration
│   │       └── application-{profile}.yml   # Profile-specific configurations
│   └── test/
│       └── java/
│           └── com/
│               └── bentham/
│                   └── voiceagent/
│                       ├── service/        # Service tests
│                       ├── controller/     # Controller tests
│                       └── integration/    # Integration tests
├── build.gradle                            # Gradle build configuration
├── settings.gradle                         # Gradle settings
├── gradlew                                 # Gradle wrapper script
├── gradlew.bat                             # Gradle wrapper script for Windows
├── .gitignore                              # Git ignore file
└── README.md                               # Project documentation
```

## Getting Started

### Prerequisites

- Java 17 or higher
- AWS account with appropriate permissions
- Gradle 7.0+ or compatible version

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/your-organization/bentham-voice-api.git
   ```

2. Configure AWS credentials:
   ```
   aws configure
   ```

3. Build the project:
   ```
   ./gradlew build
   ```

### Configuration

The application uses `application.yml` for configuration. You can create profile-specific configurations using `application-{profile}.yml` files.

Example configuration:

```yaml
spring:
  application:
    name: bentham-voice-api
  profiles:
    active: dev

server:
  port: 8080
  servlet:
    context-path: /api

# AWS Configuration
aws:
  region: us-west-2
  transcribe:
    language-code: en-US
    sample-rate: 16000
    enable-partial-results: true
  polly:
    voice-id: Joanna
    output-format: mp3
  bedrock:
    model-id: anthropic.claude-3-sonnet-20240229-v1:0
    temperature: 0.7
    max-tokens: 1024
```

## Development

### Building the Project

```
./gradlew clean build
```

### Running Tests

```
./gradlew test
```

### Running the Application

```
./gradlew bootRun
```

Or with a specific profile:

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## API Documentation

API documentation will be available via Swagger UI at `/api/swagger-ui.html` once the application is running.

## Development Roadmap

- [x] High-level architecture planning
- [ ] Core AWS service integration
- [ ] Voice input/output implementation
- [ ] Legal domain knowledge integration
- [ ] Conversation flow management
- [ ] Security and compliance features
- [ ] Testing and quality assurance
- [ ] Deployment and scaling

## Contributing

[Guidelines for contributing to the project will be added]

## License

[License information will be added]

## Contact

[Contact information will be added]
