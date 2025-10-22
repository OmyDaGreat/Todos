This is a [Kobweb](https://github.com/varabyte/kobweb) project instantiated from the `examples/todos` template.

The purpose of this project is to showcase a minimal Todo app, demonstrating:

* a simple, reactive, single-page web app, making use of both Silk UI and Compose for Web
* API endpoints (e.g. for adding, removing, and fetching items)
* how to share types across client and server (see `TodoItem` which has text and an ID value)
* PostgreSQL database integration using Exposed ORM
* Docker deployment with docker-compose

I'd like to give credit to https://blog.upstash.com/nextjs-todo for sharing the Next.js version.

---

## Running with Docker (Recommended)

This is the easiest way to run the complete application with database:

```bash
# Build and start both the application and PostgreSQL database
docker-compose up --build

# Or run in detached mode (background)
docker-compose up -d --build
```

Then open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

The application will:
- Start a PostgreSQL database with persistent storage
- Build the Kobweb frontend (JavaScript bundle)
- Build the Kobweb backend (API server with database integration)
- Wait for the database to be healthy before starting the app
- Serve the complete application on port 8080

### Verifying the Deployment

You can verify the application is running correctly:

```bash
# Check application health
curl http://localhost:8080/api/health

# Check all services are running
docker-compose ps

# View application logs
docker-compose logs -f app

# View database logs
docker-compose logs -f db
```

### Stopping the Application

To stop the application:

```bash
docker-compose down

# To also remove the database volume (will delete all todos)
docker-compose down -v
```

### Environment Variables

You can customize the database connection by setting these environment variables in `docker-compose.yml`:

- `DB_HOST`: Database host (default: `db`)
- `DB_PORT`: Database port (default: `5432`)
- `DB_NAME`: Database name (default: `todos`)
- `DB_USER`: Database user (default: `malefic`)
- `DB_PASSWORD`: Database password (default: `password`)

## Running Locally for Development

To run the sample in development mode with Kobweb:

```bash
# First, ensure PostgreSQL is running (e.g., via Docker):
docker-compose up -d db

# Then run Kobweb in development mode:
cd site
kobweb run
```

and open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

## Project Structure

- `site/src/commonMain/` - Shared code between client and server
- `site/src/jsMain/` - Client-side code (Compose for Web)
- `site/src/jvmMain/` - Server-side code (API endpoints, database integration)
- `Dockerfile` - Multi-stage Docker build configuration
- `docker-compose.yml` - Docker Compose orchestration for app and database

## Features

- ✅ **Frontend**: Reactive UI built with Compose for Web
- ✅ **Backend**: RESTful API endpoints for todo operations
- ✅ **Database**: PostgreSQL with Exposed ORM for data persistence
- ✅ **Docker**: Complete containerized deployment
- ✅ **Health Checks**: Application and database monitoring
- ✅ **Environment Config**: Flexible configuration via environment variables
