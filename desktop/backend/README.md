# Backend

The REST API server for the Mason Hotel management system. Built with Node.js and Express, it handles all business logic, authentication, and database operations for the entire ecosystem.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Database Integration](#database-integration)
- [License](#license)

---

## Overview

The backend serves as the core API layer for the Mason Hotel ecosystem. It provides RESTful endpoints for the frontend web application and handles all business logic, data validation, authentication, and database communication.

The API is built with a modular architecture, separating concerns into routes, controllers, services, and repositories.

---

## Features

### Core Features
- User authentication and authorization (JWT)
- Room management (CRUD operations)
- Booking management
- Guest management
- Payment processing
- Availability checking
- Validation and error handling
- Logging and monitoring

### Security
- Password hashing (bcrypt)
- JWT token-based authentication
- Role-based access control
- Input validation and sanitization
- CORS configuration
- Rate limiting

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Runtime | Node.js |
| Framework | Express |
| Language | TypeScript |
| Database | PostgreSQL (native driver) |
| Authentication | JWT, bcrypt |
| Validation | Zod |
| Logging | Winston / Morgan |
| Environment | dotenv |

---

## Project Structure

```
backend/
│
├── src/
│   │
│   ├── config/
│   │
│   ├── controllers/
│   │
│   ├── services/
│   │
│   ├── repositories/
│   │
│   ├── models/
│   │
│   ├── routes/
│   │
│   ├── middlewares/
│   │
│   ├── utils/
│   │
│   ├── types/
│   │
│   ├── validations/
│   │
│   └── app.ts
│
├── .env
├── .gitignore
├── package.json
├── tsconfig.json
└── README.md
```

---

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/logout` - Logout user
- `POST /api/auth/refresh` - Refresh token

### Rooms
- `GET /api/rooms` - Get all rooms
- `GET /api/rooms/:id` - Get room by ID
- `POST /api/rooms` - Create room (admin)
- `PUT /api/rooms/:id` - Update room (admin)
- `DELETE /api/rooms/:id` - Delete room (admin)
- `GET /api/rooms/availability` - Check room availability

### Reservations
- `GET /api/reservations` - Get all reservations
- `GET /api/reservations/:id` - Get reservation by ID
- `POST /api/reservations` - Create reservation
- `PUT /api/reservations/:id` - Update reservation
- `DELETE /api/reservations/:id` - Cancel reservation
- `GET /api/reservations/guest/:guestId` - Get guest reservations

### Guests
- `GET /api/guests` - Get all guests
- `GET /api/guests/:id` - Get guest by ID
- `POST /api/guests` - Create guest
- `PUT /api/guests/:id` - Update guest
- `DELETE /api/guests/:id` - Delete guest

### Payments
- `POST /api/payments` - Process payment
- `GET /api/payments/:id` - Get payment by ID
- `GET /api/payments/reservation/:reservationId` - Get payments for reservation

### Invoices
- `GET /api/invoices` - Get all invoices
- `GET /api/invoices/:id` - Get invoice by ID
- `POST /api/invoices` - Generate invoice

---

## Database Integration

The backend connects directly to PostgreSQL using a native driver. All database operations are encapsulated in repository classes.

### Connection Management
- Connection pooling
- Query parameterization
- Transaction support
- Error handling

### Key Tables
- guests
- employees
- rooms
- reservations
- payments
- invoices

---

## Environment Variables

```
PORT=5000
NODE_ENV=development

DB_HOST=localhost
DB_PORT=5432
DB_NAME=mason_hotel
DB_USER=username
DB_PASSWORD=password

JWT_SECRET=your-secret-key
JWT_EXPIRES_IN=7d

CORS_ORIGIN=http://localhost:5173
```

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.