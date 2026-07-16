# Clinic Management System

A desktop clinic management application — patient and doctor records, appointment booking with conflict detection, doctor schedules, and patient history. Java Swing front end over a PostgreSQL database.

Built for **CSCI 217 — Advanced Programming**, Nile University, Spring 2026. Five-person team project.

> Uploaded to GitHub in 2026. The team built this before any of us were using version control, so there's no meaningful commit history — this is the submitted project, published as-is apart from the credential fix noted below.

---

## What it does

- **Patients** — add, edit, remove, search; view full visit history
- **Doctors** — manage records and specialties; view per-doctor schedules
- **Appointments** — book, reschedule, cancel, with conflict detection on double-booking
- **Persistence** — PostgreSQL via JDBC, tables auto-created on first run

## Design

The project was an exercise in applying OOP properly rather than getting a GUI on screen:

```
src/
├── models/       Person (abstract) → Doctor, Patient; Appointment
├── services/     IClinicService (interface) → ClinicService (implementation)
├── exceptions/   AppointmentConflictException, InvalidInputException
├── gui/          Swing panels — one per screen, MainFrame composes them
├── utils/        DatabaseHelper — JDBC access layer
└── main/         ClinicApp — entry point
```

**Decisions worth noting:**

- `Person` is abstract and implements `Serializable`; `Doctor` and `Patient` extend it. Shared identity fields live in one place.
- The GUI talks to `IClinicService`, never to `ClinicService` or `DatabaseHelper` directly. Swapping the persistence layer would not touch a single panel.
- **Every query uses `PreparedStatement`** — no string-concatenated SQL anywhere in the codebase, so user input can't alter query structure.
- Domain errors are typed exceptions, not return codes or booleans. A double-booking throws `AppointmentConflictException`, and the GUI decides how to present it.

## Running it

Requires JDK 11+ and PostgreSQL. The PostgreSQL JDBC driver is bundled in `lib/`.

Credentials are read from environment variables — nothing is committed:

```bash
export CLINIC_DB_URL="jdbc:postgresql://localhost:5432/clinic_db"
export CLINIC_DB_USER="postgres"
export CLINIC_DB_PASSWORD="your_password"
```

Create the database, then run:

```bash
createdb clinic_db
javac -cp "lib/postgresql-42.7.3.jar:src" -d bin $(find src -name "*.java")
java -cp "lib/postgresql-42.7.3.jar:bin" main.ClinicApp
```

Tables are created automatically on first launch.

Or import into Eclipse as an existing Java project and run `ClinicApp`.

## Changed since submission

The submitted version hardcoded the database password in `DatabaseHelper.java`. It now reads from environment variables. No other code was modified.

## Team

Six-person team, CSCI 217, Spring 2026.

- Yousef Nasser Abozaid
- Yasmin Saad
- Maryam Mashaly
- Mina Adel
- Ahmed Hassan
- Yousef Ashraf
