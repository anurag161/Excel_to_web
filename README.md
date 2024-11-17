Here's a detailed README file based on the provided documentation to help new users get started with the project:

---

# Excel to Web App

## Overview

The **Excel to Web App** project enables real-time synchronization between Microsoft Excel and a web application. Changes made in the Excel file are dynamically reflected in the web page, ensuring seamless updates to web components.

This project uses a combination of frontend (HTML, CSS, JavaScript) and backend (Java Spring Boot) technologies, along with WebSocket-based communication for real-time data updates.

---

## Table of Contents

1. [Introduction](#introduction)
2. [System Requirements](#system-requirements)
3. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
4. [Project Setup](#project-setup)
   - [Excel Integration](#excel-integration)
   - [WebSocket Configuration](#websocket-configuration)
5. [Usage](#usage)
6. [Architecture](#architecture)
7. [References](#references)

---

## Introduction

This documentation outlines the steps to set up the project, including:
- Environment configuration
- Dependency installation
- Backend and frontend setup
- Real-time WebSocket communication
- Template management and content rendering

### Intended Audience
- **Developers**: Interested in contributing or modifying the project.
- **End-Users**: Wanting to use the application for monitoring and managing real-time data.

---

## System Requirements

### Hardware Requirements
- **Processor**: Dual-Core or higher (e.g., Intel i3 or equivalent)
- **RAM**: 4 GB (8 GB recommended)
- **Storage**: Minimum 10 GB free disk space

### Software Requirements
- **Operating System**: Windows 10 or higher
- **Backend**:
  - Java Development Kit (JDK) 17+
  - Spring Boot 2.7+
- **Frontend**:
  - Latest versions of Chrome, Firefox, or Edge
- **Other Tools**:
  - Node.js v18+
  - npm or Yarn

### Network Requirements
- Internet connectivity for dependencies and WebSocket communication
- Ports:
  - **8088**: Backend services
  - **5500**: Frontend local development

---

## Getting Started

### Prerequisites
- Install Java 17+
- Install a Spring Boot-compatible IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code with Java extensions)
- Install Maven or Gradle
- Install Microsoft Excel for handling `.xlsx` files

---

### Installation

#### Step 1: Install Java
1. Download and install the JDK from the [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.org/).
2. Set the `JAVA_HOME` environment variable.

#### Step 2: Set Up Spring Boot
1. Use the [Spring Initializr](https://start.spring.io/) to generate a Maven project with dependencies for **Spring Web** and **Apache POI**.
2. Install Spring Boot CLI (optional) and add it to your PATH.

#### Step 3: Install Microsoft Excel
1. Install Excel from the [Microsoft Office](https://www.microsoft.com/en-us/microsoft-365/get-started-with-office-2021) website.

#### Step 4: Install Required Dependencies
1. Add the following dependency to your `pom.xml` for Excel integration:
   ```xml
   <dependency>
       <groupId>org.apache.poi</groupId>
       <artifactId>poi-ooxml</artifactId>
       <version>5.x.x</version>
   </dependency>
   ```

---

## Project Setup

### Excel Integration
1. **Write Data to Excel**:
   - Create a new Excel file (`iitjhtml.xlsx`) with headers like `Section`, `Content Type`, `ID`, etc.
   - Populate the file with data.

2. **Read Data from Excel**:
   - Use Apache POI to parse the file and extract its content.

3. **Monitor Changes**:
   - Set up a file watcher to detect modifications and update the application in real time.

### WebSocket Configuration
1. **Setup WebSocket Server**:
   - Configure Spring Boot for WebSocket communication on `http://localhost:8088/ws`.
   - Use `SockJS` for fallback support.

2. **Implement Messaging**:
   - Use `SimpMessagingTemplate` for broadcasting messages to all connected clients.

3. **Frontend Subscription**:
   - Use `STOMP` over WebSocket to subscribe to updates and handle real-time data on the webpage.

---

## Usage

1. **Run the Backend**:
   - Start the Spring Boot server:
     ```bash
     mvn clean package
     java -jar target/your-app-name.jar
     ```

2. **Serve the Frontend**:
   - Open `code.html` using a local web server (e.g., VS Code's "Go Live" button).

3. **Test Real-Time Updates**:
   - Modify the Excel file and observe the changes reflected on the web page.

---

## Architecture

### Core Components
- **Backend**:
  - Java Spring Boot for Excel file processing and WebSocket server
  - Apache POI for Excel integration
- **Frontend**:
  - HTML, CSS, and JavaScript for rendering dynamic content
  - STOMP and SockJS for real-time updates

### Assumptions
- Replace `127.0.0.1` with `localhost` in your browser URL to resolve CORS issues.
- Provide the full path to the Excel file in the application configuration.

---

## References

1. [Spring Boot Official Documentation](https://spring.io/tools)
2. [Apache POI Maven Repository](https://mvnrepository.com/artifact/org.apache.poi/poi)
3. [STOMP with SockJS Guide](https://stomp-js.github.io/guide/stompjs/rx-stomp/using-stomp-with-sockjs.html)

--- 

This README file provides a comprehensive guide for setting up and running the project from scratch. Let me know if you'd like any modifications!
