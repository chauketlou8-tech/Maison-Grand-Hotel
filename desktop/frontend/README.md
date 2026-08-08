# Frontend

The customer-facing web application for the Mason Hotel management system. Built with React and TypeScript, it provides a modern, responsive interface for guests to browse rooms, make reservations, and manage bookings.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Pages](#pages)
- [License](#license)

---

## Overview

The frontend application serves as the public-facing website for Mason Hotel. It provides an intuitive and visually appealing interface for potential guests to explore the hotel, view room options, check availability, and complete bookings.

The application communicates with the backend REST API to handle all data operations and business logic.

---

## Features

### Guest-Facing Features
- Browse room types with images and descriptions
- Check room availability for specific dates
- Make online reservations
- View and manage existing bookings
- Contact hotel staff
- View hotel gallery and restaurant information
- Frequently asked questions

### User Experience
- Fully responsive design for all devices
- Fast page loads with Vite
- Type-safe development with TypeScript
- Modern UI with Tailwind CSS

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | React |
| Language | TypeScript |
| Styling | Tailwind CSS |
| Build Tool | Vite |
| State Management | React Context / Zustand |
| Routing | React Router |
| HTTP Client | Axios |
| Form Handling | React Hook Form |
| Validation | Zod |

---

## Project Structure

```
frontend/
│
├── public/
│   │
│   └── images/
│
├── src/
│   │
│   ├── components/
│   │   │
│   │   ├── common/
│   │   ├── layout/
│   │   ├── rooms/
│   │   ├── booking/
│   │   └── gallery/
│   │
│   ├── pages/
│   │   │
│   │   ├── Home/
│   │   ├── About/
│   │   ├── Rooms/
│   │   ├── RoomDetails/
│   │   ├── Restaurant/
│   │   ├── Gallery/
│   │   ├── Booking/
│   │   ├── Contact/
│   │   └── FAQ/
│   │
│   ├── hooks/
│   │
│   ├── services/
│   │
│   ├── contexts/
│   │
│   ├── utils/
│   │
│   ├── types/
│   │
│   ├── styles/
│   │
│   ├── App.tsx
│   │
│   └── main.tsx
│
├── .env
├── .gitignore
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

---

## Pages

### Home
Landing page featuring hotel highlights, promotions, and quick booking access.

### About
Information about the hotel, its history, and amenities.

### Rooms
Browse available room types with filtering and sorting options.

### Room Details
Detailed view of a specific room with images, amenities, and pricing.

### Restaurant
Information about on-site dining options and menus.

### Gallery
Photo gallery showcasing the hotel and its facilities.

### Booking
Reservation form with date selection, room choice, and guest details.

### Contact
Contact form and hotel location information.

### FAQ
Frequently asked questions and answers.

---

## Environment Variables

```
VITE_API_URL=http://localhost:5000
VITE_STRIPE_PUBLIC_KEY=pk_test_xxx
```

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.