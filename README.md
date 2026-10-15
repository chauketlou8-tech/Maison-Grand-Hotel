# Mason Hotel

A full-stack hotel management ecosystem featuring a customer-facing web application, a standalone Java desktop management system, and Python-based analytics. This project demonstrates modern web development, REST API design, relational database architecture, desktop application development, and data analytics within a single integrated ecosystem.

---

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Components](#components)
- [Technology Stack](#technology-stack)
- [Features](#features)
- [License](#license)

---

## Overview

Mason Hotel is a comprehensive hotel management solution built as a learning project to showcase full-stack development capabilities. The system consists of three primary interfaces:

- **Customer Website** – A modern, responsive web application for guests to browse rooms, make reservations, and manage bookings
- **Staff Desktop Application** – A JavaFX-based internal system for hotel employees to manage operations
- **Analytics Module** – Python-based data analysis for business intelligence and insights

All components communicate with a shared PostgreSQL database, ensuring data consistency across the entire platform.

---

## Project Structure

```
mason-hotel/
│
├── frontend/                        # React + Vite guest-facing app
│   ├── public/
│   ├── src/
│   ├── .env                         # VITE_* vars (API URL, WS URL, Yoco redirect)
│   ├── .gitignore
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   └── README.md
│
├── backend/                         # Node + Express API
│   ├── src/
│   │   ├── config/                  # env, db, logger, email transporter
│   │   ├── middleware/              # asyncHandler, errorHandler, validators
│   │   ├── routes/
│   │   ├── controllers/             # book, pay, settings, rooms
│   │   ├── services/                # email.service
│   │   ├── jobs/                    # cancelExpiredReservations, NotifyCheckInReminder, sendFeedBackRequest
│   │   ├── sockets/                 # settingsClient, settingsSocket, websock
│   │   ├── types/
│   │   ├── utils/
│   │   ├── errors/
│   │   ├── app.ts
│   │   └── server.ts
│   ├── .env                         # DATABASE_URL, EMAIL_*, YOCO_*, BASE_URL, WS_PORT
│   ├── .gitignore
│   ├── package.json
│   ├── tsconfig.json
│   └── README.md
│
├── desktop/                         # JavaFX PMS (front-desk client)
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── ui/                  # views, pages, records, layout, charts
│   │   │   ├── models/
│   │   │   ├── data/
│   │   │   ├── repositories/
│   │   │   ├── sockets/
│   │   │   ├── tasks/               # cron-style jobs
│   │   │   ├── utils/
│   │   │   ├── enums/
│   │   │   └── App.java
│   │   └── main/resources/          # css, icons, config/application.properties
│   ├── .gitignore
│   ├── pom.xml
│   └── README.md
│
├── analytics/                       # Python reporting
│   ├── data/
│   ├── notebooks/
│   ├── reports/
│   ├── scripts/                     # report_pdf.py, payment_pdf.py
│   ├── visualizations/
│   ├── requirements.txt
│   ├── .gitignore
│   └── README.md
│
├── database/
│   ├── backups/
│   ├── schema.sql                   # settings + all tables
│   ├── seed.sql
│   └── README.md
│
├── docs/
│   ├── api/
│   ├── architecture/
│   ├── diagrams/
│   ├── screenshots/
│   └── README.md
│
├── .gitignore
├── LICENSE
└── README.md
```

---

## Components

### Frontend (Customer Website)

A responsive, modern web application built for hotel guests.

**Pages:**
- Home
- About
- Rooms & Room Details
- Restaurant
- Gallery
- Booking
- Contact
- Frequently Asked Questions

---

### Backend (REST API)

A robust API server handling all business logic and data operations.

**Responsibilities:**
- Authentication & Authorization
- Room management
- Booking management
- Customer management
- Payment processing
- Business logic
- Database communication

---

### Desktop Application (Staff System)

An internal management system built with JavaFX for hotel staff.

**Features:**
- Guest management
- Reservations
- Room management
- Check-in / Check-out
- Billing
- Reports
- Employee management

---

### Analytics (Python)

Data analysis and business intelligence components for hotel operations.

**Features:**
- Business performance dashboards
- Occupancy trend analysis
- Revenue forecasting
- Customer behavior insights
- Data visualization
- Automated reporting
- KPI tracking
- Predictive analytics

---

### Database

Shared PostgreSQL database serving all system components.

**Tables:**
- Guests
- Employees
- Rooms
- Reservations
- Payments
- Invoices

---

### Documentation

Comprehensive project documentation.

**Contents:**
- API documentation
- Architecture design
- System diagrams
- Screenshots

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Frontend | React, TypeScript, Tailwind CSS, Vite |
| Backend | Node.js, Express, native PostgreSQL driver |
| Desktop | Java, JavaFX, JDBC, Maven |
| Analytics | Python, Pandas, NumPy, Matplotlib, Seaborn, Jupyter |
| Database | PostgreSQL |
| Documentation | Markdown, Diagrams |

---

## Features

### Customer Website
- Responsive design for all devices
- Room browsing with filters and search
- Online booking with availability checks
- Secure payment processing
- Booking management (view, modify, cancel)
- Photo gallery of hotel and rooms

### Staff Desktop Application
- Intuitive JavaFX interface
- Real-time room availability overview
- Guest check-in and check-out workflow
- Billing and invoice generation
- Employee management and permissions
- Comprehensive reporting (occupancy, revenue, etc.)
- Quick reservation management

### Analytics Module
- Business performance dashboards
- Occupancy trend analysis
- Revenue forecasting
- Customer behavior insights
- Data visualization
- Automated report generation
- KPI tracking
- Predictive analytics

### Shared Features
- Real-time data synchronization
- Consistent business logic
- Centralized database
- Role-based access control

---

## Project Goals

This project serves as a comprehensive practice ground for:

- Modern frontend development with React and TypeScript
- RESTful API design and implementation
- Object-Oriented Programming in Java
- JavaFX desktop application development
- Database design and SQL
- Native PostgreSQL database integration
- Data analytics with Python
- Data visualization and reporting
- Authentication and authorization
- Clean project architecture and separation of concerns

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.