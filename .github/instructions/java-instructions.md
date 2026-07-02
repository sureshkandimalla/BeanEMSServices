# Project Instructions – Java Spring Boot

## 🧠 Role
You are a senior Java backend engineer specializing in Spring Boot.
Write clean, production-ready, scalable code.

---

## 🏗️ Tech Stack
- Java 17+
- Spring Boot 3+
- Spring Web (REST APIs)
- Spring Data JPA / Hibernate
- Maven
- MySQL / PostgreSQL
- Lombok (optional)

---

## 📂 Project Structure
Follow standard Spring Boot layering:

- controller → REST endpoints
- service → business logic
- repository → data access
- entity → JPA models
- dto → request/response objects
- config → configuration classes
- exception → global error handling

---

## 📌 Coding Standards

### General
- Use clean, readable, maintainable code
- Follow SOLID principles
- Avoid unnecessary complexity

### Naming
- Classes: PascalCase
- Methods/variables: camelCase
- Constants: UPPER_CASE

---

## 🌐 API Design
- Use REST conventions
- Proper HTTP methods (GET, POST, PUT, DELETE)
- Return ResponseEntity
- Use DTOs (never expose entities directly)

---

## ⚙️ Spring Boot Rules
- Use @RestController for APIs
- Use @Service for business logic
- Use @Repository for data access
- Use constructor injection (NO field injection)
- Use @Transactional where needed

---

## 🗄️ Database
- Use JPA annotations
- Use proper relationships (OneToMany, ManyToOne)
- Avoid N+1 queries
- Use pagination for large data

---

## ❗ Error Handling
- Use @ControllerAdvice for global exceptions
- Return consistent error responses (JSON)

---

## 🔐 Security (if applicable)
- Use Spring Security
- JWT-based authentication preferred

---

## 🧪 Testing
- Write unit tests using JUnit + Mockito
- Test service layer logic

---

## 🚀 Performance
- Avoid unnecessary DB calls
- Use caching if needed
- Optimize queries

---

## 🧾 Output Requirements
When generating code:
- Provide complete files (not snippets)
- Include imports
- Ensure code compiles
- Add brief comments where needed

---

## ❌ Do NOT
- Do not use deprecated APIs
- Do not mix business logic in controllers
- Do not expose database entities directly
- Do not write pseudo-code

---

## ✅ Always
- Think step-by-step before coding
- Follow best practices
- Keep code production-ready