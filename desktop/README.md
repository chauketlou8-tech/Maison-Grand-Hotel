# Desktop Application

The desktop application is the internal hotel management system used by hotel staff. Built with Java and JavaFX, it provides a comprehensive interface for managing daily hotel operations.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database Integration](#database-integration)
- [License](#license)

---

## Overview

This desktop application serves as the primary tool for hotel staff to manage all aspects of hotel operations. It connects directly to the shared PostgreSQL database, providing real-time access to guest information, room status, reservations, and financial data.

The application is designed with an intuitive user interface using JavaFX, making it easy for staff to perform daily jobs efficiently.

---

## Features

### Guest Management
- Add, edit, and view guest profiles
- Search and filter guest records
- View guest history and preferences
- Manage guest communications

### Reservations
- Create and manage reservations
- Check availability in real-time
- Modify or cancel existing reservations
- View reservation calendar
- Process walk-in bookings

### Room Management
- View room status (available, occupied, maintenance)
- Assign rooms to guests
- Track room inventory and features
- Manage room rates
- Schedule maintenance

### Check-in / Check-out
- Streamlined check-in process
- Verify guest identity and reservation
- Process payments at check-in
- Simplified check-out with final billing
- Generate invoices

### Billing and Payments
- Generate and print invoices
- Process payments (cash, credit card, etc.)
- View payment history
- Handle refunds and adjustments

### Reports
- Occupancy reports
- Revenue reports
- Guest statistics
- Reservation trends
- Financial summaries
- Export reports to PDF or CSV

### Employee Management
- Staff accounts and permissions
- Role-based access control
- Employee scheduling
- Activity logs

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Java (JDK 17+) |
| UI Framework | JavaFX |
| Database Connectivity | JDBC |
| Build Tool | Maven |
| Reporting | JasperReports |
| Version Control | Git |

---

## Architecture

The desktop application follows a layered architecture pattern:

### Presentation Layer (UI)
- JavaFX components
- FXML for layout
- CSS for styling
- Event handling

### Service Layer
- Business logic implementation
- Transaction management
- Validation and authorization

### Repository Layer
- Data access operations
- CRUD operations
- Query execution

### Database Layer
- Connection management
- Query definitions
- Exception handling

---

## Project Structure

```
desktop/
│
├── src/
│   │
│   ├── main/
│   │   │
│   │   ├── config/
│   │   │
│   │   ├── database/
│   │   │
│   │   ├── exceptions/
│   │   │
│   │   ├── models/
│   │   │
│   │   ├── repositories/
│   │   │
│   │   ├── services/
│   │   │
│   │   ├── ui/
│   │   │   │
│   │   │   ├── common/
│   │   │   ├── dashboard/
│   │   │   ├── employees/
│   │   │   ├── guests/
│   │   │   ├── payments/
│   │   │   ├── reports/
│   │   │   ├── reservations/
│   │   │   └── rooms/
│   │   │
│   │   └── utils/
│   │
│   └── resources/
│       │
│       ├── css/
│       ├── fxml/
│       ├── icons/
│       ├── images/
│       └── sql/
│
├── lib/
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## Database Integration

The desktop application connects directly to the PostgreSQL database using JDBC. Database operations are encapsulated in repositories classes.

### Connection Management
- Connection pooling for performance
- Secure credential storage
- Error handling and retry logic
- Transaction support

### Key Database Operations
- CRUD operations for all entities
- Complex queries for reporting
- Stored procedures for business logic
- Transaction management for check-in/check-out

---

## Setup and Configuration

### Database Configuration
Configuration is managed through a properties file:

```
db.host=localhost
db.port=5432
db.name=mason_hotel
db.user=username
db.password=password
```

### Running the Application

**From IDE:**
1. Open project in IntelliJ IDEA, Eclipse, or VS Code
2. Configure database connection
3. Run Main.java

**Using Maven:**
```
mvn clean compile
mvn javafx:run
```

---

## Authentication and Security

### Login System
- Employee authentication with username/password
- Session management
- Password hashing
- Login attempt tracking

### Role-Based Access Control
- Administrator: Full system access
- Manager: Access to reports and employee management
- Receptionist: Guest and reservation management
- Housekeeping: Room status management

---

## Reports

The application includes comprehensive reporting capabilities:

- **Occupancy Report**: Daily, weekly, monthly occupancy rates
- **Revenue Report**: Income breakdown by source
- **Guest Report**: Guest demographics and preferences
- **Reservation Report**: Booking trends and patterns
- **Financial Report**: Profit and loss statements

Reports can be:
- Previewed on screen
- Printed
- Exported as PDF or CSV

---

## Build and Deployment

### Packaging
The application can be packaged as:
- JAR file with dependencies
- Native installer using jpackage
- Platform-specific executable

### Deployment Options
- Local installation on hotel computers
- Network deployment
- Remote desktop access

---

## Development Notes

### Coding Standards
- Follow Java naming conventions
- Use meaningful variable and method names
- Comment complex logic
- Write unit tests for critical components

### Version Control
- Feature branch workflow
- Descriptive commit messages
- Regular commits with atomic changes

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.