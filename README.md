# Webshop Backend

A RESTful e-commerce backend API built with **Spring Boot 4.0** and **Java 21**. This project serves as the backend for a full-stack webshop application, featuring JWT authentication, role-based access control, and product/category management.

Built as the backend for a [React bootcamp](https://development.ee/react-suvaope-javascripti-oskajale/) project.

**Live Frontend**: [https://mark-webshop.web.app](https://mark-webshop.web.app)

**Frontend repo**: [https://github.com/hallapmark/webshop](https://github.com/hallapmark/webshop)

---

## Features

- **RESTful API** with full CRUD operations for products, categories, and users
- **JWT Authentication** with role-based authorization (user, admin, superadmin)
- **Spring Security** integration with custom JWT filter
- **PostgreSQL** database with JPA/Hibernate ORM
- **Pagination & Sorting** for product listings
- **API Documentation** with OpenAPI 3.0 (Swagger UI)
- **Database Seeding** via Spring profiles for dev/test environments
- **Docker** multi-stage build for production deployment
- **Environment Profiles** for dev and production configurations

---

## Used Technologies

- **Java 21** — Programming language
- **Spring Boot 4.0** — Application framework
- **Spring Security** — Authentication & authorization
- **Spring Data JPA** — Database ORM
- **PostgreSQL** — Relational database
- **JWT (jjwt)** — Token-based authentication
- **Docker** — Containerization & deployment

---

## API Endpoints

### Authentication
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/login` | Public | Authenticate and receive JWT token |
| POST | `/persons` | Public | Register new user |

### Products
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/products?categoryId={id}` | Public | Get paginated products by category |
| GET | `/products/{id}` | Public | Get single product |
| GET | `/admin/products` | Admin | Get all products (no pagination) |
| POST | `/products` | Admin | Create new product |
| PUT | `/products` | Admin | Update product |
| DELETE | `/products/{id}` | Admin | Delete product |

### Categories
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/categories` | Public | Get all categories |
| POST | `/categories` | Admin | Create new category |
| PUT | `/categories` | Admin | Update category |
| DELETE | `/categories/{id}` | Admin | Delete category |

### User Management
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/person` | Authenticated | Get own profile |
| PUT | `/persons` | Authenticated | Update own profile |
| GET | `/persons` | Superadmin | Get all users |
| GET | `/persons/{id}` | Superadmin | Get single user |
| DELETE | `/persons/{id}` | Superadmin | Delete user |

### Employees
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/employees` | Public | Get all employees |
| GET | `/employees/{id}` | Public | Get single employee |
| POST | `/employees` | Superadmin | Create new employee |
| PUT | `/employees` | Superadmin | Update employee |
| DELETE | `/employees/{id}` | Superadmin | Delete employee |

### Orders
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/orders` | Admin | Get all orders |

> Full API documentation available at `/swagger-ui.html` when running locally

---


## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL 15+
- (Optional) Docker

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd webshop-backend
   ```

2. **Set up PostgreSQL**
   ```bash
   # Create a database named 'webshop'
   createdb webshop
   ```

3. **Configure environment variables**
   ```bash
   # Copy the example env file
   cp dev.env.example dev.env
   
   # Edit dev.env with your values:
   # - DEV_DB_USER=your_postgres_user
   # - DEV_DB_PASSWORD=your_postgres_password
   # - DEV_JWT_SIGNING_KEY_BASE64=<generated_key>
   ```

4. **Generate JWT signing key**
   ```bash
   python scripts/generate_jwt_key.py
   # Copy the output to DEV_JWT_SIGNING_KEY_BASE64 in dev.env
   ```

5. **Run the application**
   ```bash
   # Load env variables and start
   source dev.env && ./mvnw spring-boot:run
   ```

6. **Access the API**
   - API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

### Database Seeding

To populate the database with sample data:
```bash
# Set profile to include seed
SPRING_PROFILES_ACTIVE=dev,seed ./mvnw spring-boot:run
```

---


## Security

This application implements a comprehensive security model:

- **Password Hashing**: BCrypt encryption for all stored passwords
- **JWT Tokens**: 60-minute expiration, HMAC-SHA256 signed
- **Role-Based Access Control**:
  - `user` - Basic authenticated access
  - `admin` - Product and category management
  - `superadmin` - Full system access including user management
- **CORS**: Configured for frontend domain only

---

## Deployment

The application is configured for deployment on [Render](https://render.com) with the included `render.yaml`. It uses:
- Docker-based deployment
- Managed PostgreSQL database
- Environment variable injection for secrets

---
## Live Demo
[https://mark-webshop.web.app](https://mark-webshop.web.app)

