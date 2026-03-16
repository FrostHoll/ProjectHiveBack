# ProjectHive Backend

<p align="center"> 
  <img src="https://img.shields.io/badge/Java-22-blue?logo=java" alt="Java"/> 
  <img src="https://img.shields.io/badge/SpringBoot-3.3.0-green?logo=springboot" alt="SpringBoot"/> 
  <img src="https://img.shields.io/badge/Hibernate-orange" alt="Hibernate"/> 
  <img src="https://img.shields.io/badge/PostgreSQL-yellow?logo=postgresql" alt="PostgreSQL"/> 
  <img src="https://img.shields.io/badge/JWT-0.12-purple" alt="JWT"/> 
</p>

Backend сервис для мобильного приложения управления задачами команды.

Система позволяет тимлидам публиковать задачи, а разработчикам — брать задачи в работу и отслеживать их выполнение через мобильное приложение.

## Технологии

- Java
- Spring Boot
- Spring Security
- Hibernate / JPA
- PostgreSQL
- JWT authentication
- Maven

## Основной функционал

- регистрация и аутентификация пользователей
- авторизация через JWT
- создание и управление задачами
- назначение задач участникам команды
- изменение статуса выполнения задач
- REST API для взаимодействия с мобильным приложением

## Архитектура проекта

Проект построен по многослойной архитектуре:

controller – REST контроллеры
service – бизнес-логика приложения
repository – работа с базой данных (Spring Data JPA)
model – сущности базы данных
auth – конфигурация безопасности и JWT

Такой подход обеспечивает разделение ответственности и упрощает поддержку кода.

## База данных

Используется PostgreSQL.

Основные сущности:

- User
- Task
- Team
- TeamMember
- TaskStatus

Доступ к базе данных реализован через Hibernate (JPA).

## Аутентификация

Для защиты API используется **JWT (JSON Web Token)**.

Процесс авторизации:

1. Пользователь отправляет логин и пароль
2. Сервер генерирует JWT-токен
3. Токен используется для доступа к защищенным endpoint'ам

## REST API

Пример основных endpoint'ов:

POST /api/auth/register

POST /api/auth/authenticate

GET /api/task/{taskID}

GET /api/task/picked

POST /api/task/pick/{taskID}

POST /api/task/finish/{taskID}

POST /api/task/approve/{taskID}

PUT /api/task/{taskID}

DELETE /api/task/{taskID}

## Связь с мобильным приложением

Этот backend используется мобильным приложением, написанным на **Kotlin (Android)**.

Приложение взаимодействует с сервером через REST API.

## Запуск проекта

1. Клонировать репозиторий

`git clone https://github.com/FrostHoll/ProjectHiveBack`

2. Настроить PostgreSQL

3. Запустить приложение

`mvn spring-boot:run`

## Автор

Олег Щербаков
