
# Personal-Finance-Manager API

Full-stack-ready Spring Boot REST service that lets a registered user record income & expense transactions, organise them in categories, and track progress toward savings goals.

![Java 17](https://img.shields.io/badge/Java-17-blue) ![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen) ![Maven](https://img.shields.io/badge/Build-Maven-orange)

---

## Table of Contents
1. Features  
2. Quick demo  
3. Tech stack & Project layout  
4. Local setup  
5. End-to-end test-suite  
6. API cheat-sheet  
7. License  

---

## 1 Features
- User registration & session-cookie login  
- CRUD for Transactions (income / expense) with category, date & free-text description  
- Category management (built-in defaults + user custom)  
- Savings Goals with real-time `currentProgress`, `progressPercentage`, `remainingAmount`  
- Monthly / yearly reports  
- Soft delete & validation hooks  
- H2 web console enabled in *dev* profile  
- 80 + integration tests in a single Bash script

---

## 2 Quick Demo

```bash
# register & login
curl -s -X POST -H 'Content-Type: application/json'   -d '{"username":"demo@x.com","password":"pw","fullName":"Demo","phoneNumber":"1"}'   http://localhost:8080/api/auth/register
curl -s -c cookie.txt -X POST -H 'Content-Type: application/json'
-d '{"username":"demo@x.com","password":"pw"}'
http://localhost:8080/api/auth/login
# add a transaction
curl -s -b cookie.txt -X POST -H 'Content-Type: application/json'
-d '{"amount":100,"date":"2025-01-01","category":"Food","description":"groceries"}'
http://localhost:8080/api/transactions | jq .
```

---

## 3 Tech Stack & Layout

```
src 
├─ main 
│  ├─ java/com/example/pfm 
│  │  ├─ controller ← REST endpoints 
│  │  ├─ service ← business logic 
│  │  ├─ repository ← Spring-Data JPA 
│  │  ├─ dto ← request / response records 
│  │  └─ entity ← JPA models 
│  └─ resources 
│     ├─ application.yml 
│     └─ db/migration ← Flyway scripts 
└─ test ← JUnit + Mockito
```

• Java 17 + Spring Boot 3.2  
• Maven wrapper (`./mvnw`) included  

---

## 4 Local Setup

**Prerequisites**: Java 17+ & Git.

```bash
git clone https://github.com/sudo-raghib/personal-finance-manager.git
cd personal-finance-manager
./mvnw clean package          # compile & unit tests
./mvnw spring-boot:run        # http://localhost:8080
# H2 console: http://localhost:8080/h2-console
# JDBC URL jdbc:h2:file:./data/pfm, user sa, empty password.
```

## 5 End-to-End Test-Suite

The repo ships with `finance_e2e_tests.sh` (86 scenarios).

```bash
chmod +x finance_e2e_tests.sh
./finance_e2e_tests.sh http://localhost:8080/api
# → Tests passed: 86 / Failed: 0
```

Pass a different base-URL to hit your cloud deploy:

```bash
./finance_e2e_tests.sh https://pfm-raghib.onrender.com/api
```


## 6 API Cheat-Sheet

```http
# ── auth ───────────────────────────────────────────────────────
POST /api/auth/register   { username,password,fullName,phoneNumber }
POST /api/auth/login      { username,password }

# ── transactions ──────────────────────────────────────────────
GET /api/transactions?category=&startDate=&endDate= 
POST /api/transactions { amount,date,category,description } 
PUT /api/transactions/{id} { amount?,description? } 
DELETE /api/transactions/{id}

# ── categories ────────────────────────────────────────────────
GET /api/categories 
POST /api/categories { name,type } # INCOME|EXPENSE 
DELETE /api/categories/{name}

# ── goals ─────────────────────────────────────────────────────
GET /api/goals 
POST /api/goals { goalName,targetAmount,targetDate,startDate? } 
GET /api/goals/{id} 
PUT /api/goals/{id} { targetAmount?,targetDate? } 
DELETE /api/goals/{id}

# ── reports ───────────────────────────────────────────────────
GET /api/reports/monthly?year=2025 
GET /api/reports/yearly?start=2024&end=2025
```

All error responses carry `{ "message": "…" }` and proper HTTP codes: 400 / 404 / 409.

---

## 7 License

Apache License 2.0 – see `LICENSE` file.
