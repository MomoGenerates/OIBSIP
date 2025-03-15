# Online Reservation System

A console-based Java train reservation system that allows passengers to book tickets, view train schedules, and manage their reservations. Administrators can manage trains and view booking statistics.

## Features

### For Passengers
- Search trains by source and destination
- View train schedules and availability
- Book tickets with class preference
- View and cancel reservations
- Update profile information

### For Administrators
- Add and manage trains
- Update train schedules
- Manage seat availability
- View booking statistics
- Monitor system status

## Technical Implementation

- Console-based user interface with animated text display
- Secure user authentication system
- PNR generation for bookings
- Data persistence using Java serialization
- Class-based fare calculation

## Project Structure

```
├── src/
│   ├── main/
│   │   └── App.java           # Application entry point
│   ├── models/
│   │   ├── Admin.java         # Admin user model
│   │   ├── Passenger.java     # Passenger user model
│   │   ├── Reservation.java   # Booking details
│   │   ├── Stoppage.java      # Train stop information
│   │   ├── Train.java         # Train details
│   │   └── User.java         # Base user class
│   └── util/
│       ├── AnimatedText.java  # Text animation utility
│       ├── DatabaseManager.java# Data persistence
│       └── PNRGenerator.java  # PNR creation utility
├── database/                  # Serialized data storage
└── README.md                  # Project documentation
```

## Getting Started

1. Ensure you have Java JDK 17 or higher installed
2. Clone the repository
3. Compile the project:
   ```bash
   javac -d bin src/**/*.java
   ```
4. Run the application:
   ```bash
   java -cp bin main.App
   ```

## Usage

### For Passengers
1. Register a new account or login
2. Search trains by entering source and destination
3. Select train and class type
4. Book tickets by specifying number of seats
5. View PNR and booking details
6. Cancel reservations if needed

### For Administrators
1. Login with admin credentials
2. Manage trains (add/update/remove)
3. Update schedules and fares
4. View booking statistics
5. Monitor system status

## Technical Notes

- `AnimatedText`: Provides smooth text animations in console
- `DatabaseManager`: Handles data persistence through serialization
- `PNRGenerator`: Creates unique PNR numbers for bookings
- `Train`: Manages train details including stoppages and fares
- `Reservation`: Handles booking logic and PNR generation
- `User`: Base class with authentication functionality
- `Passenger`: Extends User with reservation management
- `Admin`: Extends User with system management capabilities

## Data Persistence

The system uses Java serialization to store:
- User accounts and profiles
- Train details and schedules
- Reservation records
- System configurations

Data is automatically saved after each operation in the `database` directory.