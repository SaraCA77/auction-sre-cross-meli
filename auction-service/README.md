# Auction Service

This project implements a full-featured auction system using Spring Boot. It supports scheduled tasks, in-memory H2 database, WebSocket integration, metrics monitoring with Actuator, and more.

## ✅ Requirements

- Java 21+
- Gradle 8+
- (Optional) Postman or curl to test REST endpoints
- (Optional) Browser to run the auction simulation HTML (WebSocket)

## 🚀 How to run the project

```bash
./gradlew bootRun
```

Or if using Windows PowerShell:

```powershell
.\gradlew bootRun
```

Service will be available at: **http://localhost:8080**

## 🧪 How to run tests

```bash
./gradlew test
```

HTML report location:
```
build/reports/tests/test/index.html
```

## ⚙️ What you need to have installed

- Java Development Kit 21
- Gradle (wrapper included, so you can run `./gradlew` without installing globally)

## 💡 Project explanation

This project simulates an auction platform with:

- Create and list auctions (active/inactive)
- Place bids (via REST or WebSocket)
- Auto-close auctions after their end time (scheduled task)
- Reactivate suspended auctions after 30 days
- Metric tracking with Spring Boot Actuator (bid count, processing time, etc.)
- In-memory H2 database for fast testing without external dependencies
- Optional Spring Boot Admin integration for real-time monitoring

## 📡 WebSocket support

- Endpoint: `ws://localhost:8080/ws-auctions`
- Example browser client included (HTML file) to simulate massive bid loads
- Topic: `/topic/updates`
- Send destination: `/app/bid`

## 📊 Metrics and monitoring

- Actuator endpoints at `/actuator`
- Example metrics:
    - `/actuator/metrics/websocket.bids.received`
    - `/actuator/metrics/websocket.bids.process.time`
    - `/actuator/metrics/http.server.requests`

## 🗺️ Example REST endpoints

- `POST /auctions` — Create a new auction
- `GET /auctions` — List all auctions
- `GET /auctions/active` — List active auctions
- `GET /auctions/inactive` — List inactive auctions
- `POST /auctions/{id}/bid` — Place a bid
- `POST /auctions/{id}/close` — Force close an auction
- `POST /auctions/{id}/reactivate` — Reactivate a suspended auction
- `GET /auctions/{id}` — Get auction details by ID

## 🇪🇸 README en Español

### ✅ Requisitos

- Java 21+
- Gradle 8+
- (Opcional) Postman o curl para probar endpoints REST
- (Opcional) Navegador para el simulador HTML de pujas (WebSocket)

### 🚀 Cómo ejecutar el proyecto

```bash
./gradlew bootRun
```

O en Windows PowerShell:

```powershell
.\gradlew bootRun
```

Servicio disponible en: **http://localhost:8080**

### 🧪 Cómo correr los tests

```bash
./gradlew test
```

Reporte HTML:
```
build/reports/tests/test/index.html
```

### ⚙️ Qué necesitas tener instalado

- JDK 21
- Gradle (ya incluye wrapper)

### 💡 Explicación

Simula una plataforma de subastas con:

- Crear y listar subastas (activas/inactivas)
- Pujar vía REST o WebSocket
- Cierre automático de subastas
- Reactivación automática después de 30 días
- Métricas expuestas vía Actuator
- H2 en memoria
- Spring Boot Admin para monitoreo

### 📡 WebSocket

- Endpoint: `ws://localhost:8080/ws-auctions`
- Archivo HTML de ejemplo incluido
- Tópico: `/topic/updates`
- Envío: `/app/bid`

### 📊 Métricas

- `/actuator`
- Ejemplos:
    - `/actuator/metrics/websocket.bids.received`
    - `/actuator/metrics/websocket.bids.process.time`
    - `/actuator/metrics/http.server.requests`

### 🗺️ Endpoints REST

- `POST /auctions` — Crear subasta
- `GET /auctions` — Listar todas
- `GET /auctions/active` — Activas
- `GET /auctions/inactive` — Inactivas
- `POST /auctions/{id}/bid` — Pujar
- `POST /auctions/{id}/close` — Cerrar manual
- `POST /auctions/{id}/reactivate` — Reactivar
- `GET /auctions/{id}` — Detalle

---

Enjoy bidding! 🚀🏆