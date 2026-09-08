# Synchronous

Синхронизация чатов Discord ↔ Telegram, связь серверов и пользователей, оценка крутости сообщений с мнениями бота в чате.

# Что нужно для развёртки

- **Java 17** — обязательно. На Java 25/26 сборка падает (старый Lombok). Проверка: `java -version`
- **Docker** — только для инфраструктуры (Postgres + Kafka), сами боты запускаются через Gradle
- **Discord-бот**: приложение в [Discord Developer Portal](https://discord.com/developers/applications/), бот добавлен на сервер с правами читать/писать сообщения и менять ники
- **Telegram-бот**: токен от [@BotFather](https://t.me/BotFather), бот добавлен в чат
- **Для мнений о крутости** (иначе бот отвечает по простым запасным правилам): установленный [OpenCode CLI](https://opencode.ai) (`opencode`) со входом в аккаунт — скоринг идёт через `opencode run --model opencode/big-pickle`

# Переменные окружения

| Переменная | Обязательна | Что это |
|---|---|---|
| `DISCORD_TOKEN` | да | Токен Discord-бота |
| `TELEGRAM_TOKEN` | да | Токен Telegram-бота |
| `COOLNESS_MODE` | нет (`opencode`) | `opencode` — через CLI; `http` — через OpenAI-совместимый API |
| `COOLNESS_OPENCODE_BIN` | нет (`opencode`) | Путь к бинарю OpenCode CLI |
| `COOLNESS_OPENCODE_MODEL` | нет (`opencode/big-pickle`) | Модель для оценки |
| `COOLNESS_API_URL` / `COOLNESS_API_MODEL` / `COOLNESS_API_KEY` | только для `http` | Параметры внешнего API |
| `COOLNESS_API_TIMEOUT_SECONDS` | нет (`60`) | Таймаут одного скоринга |

Postgres и Kafka захардкожены: Postgres `localhost:5454/Synchronous` (логин/пароль `larffxx`), Kafka `localhost:29092`.

# Запуск

**1. Инфраструктура:**
```sh
cd IdeaProjects/Synchronous
docker compose up db kafka kafka-init
```
Сервис `kafka-init` обязателен — автосоздание топиков выключено (`KAFKA_AUTO_CREATE_TOPICS_ENABLE=false`), без него топиков не будет. Kafka готова примерно через 10 секунд после старта (healthcheck).

**2. Сборка (в каждом модуле свой wrapper):**
```sh
cd IdeaProjects/Synchronous/SynchronousDiscord && ./gradlew build
cd IdeaProjects/Synchronous/SynchronousTelegram && ./gradlew build
```
Если `Permission denied` — запускать через `sh gradlew ...`. Если ругается на Lombok — проверить, что `JAVA_HOME` указывает на Java 17.

**3. Старт ботов (каждый в своём терминале):**
```sh
cd IdeaProjects/Synchronous/SynchronousDiscord && ./gradlew bootRun
cd IdeaProjects/Synchronous/SynchronousTelegram && ./gradlew bootRun
```

**4. Связка чатов:**
- В Discord: `/register <telegramChatName>` — связать сервер с Telegram-чатом
- `/connect <telegramUserName>` — связать пользователей
- Дальше сообщения синхронизируются, а бот отвечает мнением о крутости на каждое сообщение и копит рейтинг (видны звёзды в никах, команды `/cool`, `/coolness`)

# P.S.
Проект в разработке. `Dockerfile` в корне — stale-шаблон, не использовать (сборка только через `gradlew` модулей).
