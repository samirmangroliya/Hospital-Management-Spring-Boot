# Hospital Management System - Distributed Microservices Architecture

A production-grade, event-driven microservices architecture built with **Spring Boot**, **Spring Cloud Gateway**, **Apache Kafka** (Saga Pattern with Transactional Outbox), and **PostgreSQL**.

---

## 🏛️ Architecture Overview

```text
                  ┌──────────────────────┐
                  │    Client / UI       │
                  └──────────┬───────────┘
                             │ HTTP (Port 8080)
                             ▼
                  ┌──────────────────────┐
                  │    Gateway Service   │ (Spring Cloud Gateway)
                  └─────┬────┬────┬──────┘
                        │    │    │
         ┌──────────────┘    │    └──────────────┐
         ▼                   ▼                   ▼
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│Appointment Service│ │  Patient Service │ │  Doctor Service  │
│  (Port 8083)     │ │   (Port 8081)    │ │   (Port 8082)    │
└────────┬─────────┘ └────────┬─────────┘ └────────┬─────────┘
         │                    │                    │
         └────────────────────┼────────────────────┘
                              │ Async Messaging
                              ▼
                     ┌──────────────────┐
                     │   Apache Kafka   │ (Saga Orchestration & Outbox)
                     └──────────────────┘
