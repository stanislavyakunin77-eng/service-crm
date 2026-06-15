# Service CRM - Technical Service Management System

Полнофункциональная CRM-система для управления сервисным центром технического обслуживания.

## Основные возможности

### Участники системы:
- **Клиент**: Создание заявок, отслеживание статуса, управление платежами
- **Менеджер**: Регистрация клиентов, управление заказ-нарядами, координация работы
- **Мастер**: Диагностика, проведение ремонта, управление запчастями
- **Администратор**: Управление пользователями, настройка доступа

## Технологический стек

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Flyway** (миграция БД)

### Frontend
- **Vue.js 3**
- **JavaScript ES6+**
- **Axios**
- **Bootstrap 5**

## Структура проекта

```
service-crm/
├── src/main/
│   ├── java/com/servicecrm/
│   │   ├── controller/      # REST контроллеры
│   │   ├── service/         # Бизнес-логика
│   │   ├── repository/      # Доступ к БД
│   │   ├── model/
│   │   │   ├── entity/      # JPA сущности
│   │   │   └── dto/         # DTO объекты
│   │   └── ServiceCrmApplication.java
│   └── resources/
│       ├── application.yml  # Конфигурация
│       └── db/migration/    # SQL миграции
├── frontend/                # Vue.js приложение
├── pom.xml                  # Maven конфигурация
└── README.md

```

## Установка и запуск

### Предварительные требования
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Node.js 16+ (для фронтенда)

### 1. Подготовка базы данных

```bash
createdb service_crm
```

### 2. Запуск Backend

```bash
# Перейти в корневую папку проекта
cd service-crm

# Собрать проект
mvn clean install

# Запустить приложение
mvn spring-boot:run
```

Backend будет доступен по адресу: `http://localhost:8080/api`

### 3. Запуск Frontend

```bash
# Перейти в папку фронтенда
cd frontend

# Установить зависимости
npm install

# Запустить разработочный сервер
npm run dev
```

Frontend будет доступен по адресу: `http://localhost:3000`

## API Endpoints

### Клиенты
- `GET /api/clients` - Получить всех клиентов
- `GET /api/clients/{id}` - Получить клиента по ID
- `POST /api/clients` - Создать нового клиента
- `PUT /api/clients/{id}` - Обновить данные клиента
- `DELETE /api/clients/{id}` - Удалить клиента

### Заказы
- `GET /api/orders` - Получить все заказы
- `GET /api/orders/{id}` - Получить заказ по ID
- `GET /api/orders/client/{clientId}` - Получить заказы клиента
- `GET /api/orders/manager/{managerId}` - Получить заказ�� менеджера
- `GET /api/orders/master/{masterId}` - Получить заказы мастера
- `GET /api/orders/status/{status}` - Получить заказы по статусу
- `POST /api/orders` - Создать новый заказ
- `PUT /api/orders/{id}` - Обновить заказ
- `PUT /api/orders/{id}/status/{status}` - Изменить статус заказа
- `PUT /api/orders/{id}/assign-manager/{managerId}` - Назначить менеджера
- `PUT /api/orders/{id}/assign-master/{masterId}` - Назначить мастера
- `PUT /api/orders/{id}/diagnostics` - Обновить диагностику
- `PUT /api/orders/{id}/approve` - Одобрить диагностику
- `PUT /api/orders/{id}/reject` - Отклонить ремонт
- `PUT /api/orders/{id}/complete` - Завершить ремонт
- `PUT /api/orders/{id}/pay` - Отметить как оплачено
- `PUT /api/orders/{id}/return` - Выдать клиенту
- `DELETE /api/orders/{id}` - Удалить заказ

## Статусы заказов

1. **CREATED** - Создана
2. **RECEIVED** - Принята в сервис
3. **DIAGNOSTICS** - На диагностике
4. **AWAITING_APPROVAL** - Ожидает подтверждения
5. **APPROVED** - Одобрена к ремонту
6. **REJECTED** - Отклонена
7. **IN_PROGRESS** - В процессе ремонта
8. **COMPLETED** - Завершена
9. **PAID** - Оплачена
10. **RETURNED** - Выдана клиенту

## Роли пользователей

- **ADMIN** - Администратор системы
- **MANAGER** - Менеджер сервиса
- **MASTER** - Мастер (техник)
- **CLIENT** - Клиент

## Конфигурация

Основные параметры приложения находятся в файле `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/service_crm
    username: postgres
    password: postgres
```

Измените данные для подключения к вашей БД при необходимости.

## Разработка

### Добавление новой сущности

1. Создать Entity класс в `model/entity/`
2. Создать DTO класс в `model/dto/`
3. Создать Repository интерфейс в `repository/`
4. Создать Service класс в `service/`
5. Создать Controller класс в `controller/`
6. Добавить SQL миграцию в `db/migration/`

## Лицензия

MIT License

## Контактная информация

Автор: Your Name
Email: your.email@example.com
