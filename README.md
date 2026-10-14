# Synchronous

Синхронизация чатов Discord ↔ Telegram и связь серверов и пользователей.

# Что нужно для развёртки

- **Java 17** — обязательно для текущего Lombok/JDA стека. Gradle toolchain использует Java 17 даже если системная Java новее. Проверка: `java -version`
- **Docker** — только для инфраструктуры (Postgres + Kafka), сами боты запускаются через Gradle
- **Discord-бот**: приложение в [Discord Developer Portal](https://discord.com/developers/applications/), бот добавлен на сервер с правами читать и писать сообщения
- **Telegram-бот**: токен от [@BotFather](https://t.me/BotFather), бот добавлен в чат

# Переменные окружения

| Переменная | Обязательна | Что это |
|---|---|---|
| `DISCORD_TOKEN` | да | Токен Discord-бота |
| `TELEGRAM_TOKEN` | да | Токен Telegram-бота |
Postgres и Kafka больше не содержат credentials в репозитории. По умолчанию приложение использует Postgres `localhost:5454/Synchronous` и Kafka `localhost:29092`, а логин и пароль берутся из `DB_USERNAME` и `DB_PASSWORD`.

Перед первым запуском создай локальный файл конфигурации и экспортируй переменные:

```sh
cp config/postgres-config.env.example config/postgres-config.env
set -a
. config/postgres-config.env
export DB_USERNAME="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"
set +a
```

`config/postgres-config.env` игнорируется Git и не должен попадать в коммиты. Для production задай `JPA_DDL_AUTO=validate` и используй отдельные миграции схемы.

# Запуск

**1. Инфраструктура:**
```sh
cd IdeaProjects/Synchronous
docker compose up db kafka kafka-init
```
Сервис `kafka-init` обязателен — автосоздание топиков выключено (`KAFKA_AUTO_CREATE_TOPICS_ENABLE=false`), без него топиков не будет. Kafka готова примерно через 10 секунд после старта (healthcheck).

**2. Сборка:**
```sh
cd IdeaProjects/Synchronous/SynchronousDiscord && ./gradlew build
cd IdeaProjects/Synchronous/SynchronousTelegram && ./gradlew build
cd IdeaProjects/Synchronous/SynchronousWeb && ./gradlew build
```
Если Gradle ругается на Java — проверь, что `JAVA_HOME` указывает на Java 17.

**3. Старт ботов (каждый в своём терминале):**
```sh
cd IdeaProjects/Synchronous/SynchronousDiscord && ./gradlew bootRun
cd IdeaProjects/Synchronous/SynchronousTelegram && ./gradlew bootRun
cd IdeaProjects/Synchronous/SynchronousWeb && ./gradlew bootRun
```

**4. Связка чатов:**
- В Discord: `/register <telegramChatName>` — связать сервер с Telegram-чатом
- `/connect <telegramUserName>` — связать пользователей
- Дальше сообщения синхронизируются между чатами

**5. Меню команд Telegram:**
- `/menu` показывает доступные команды в виде кликабельных inline-кнопок
- Telegram также получает нативное меню slash-команд при запуске модуля
- Кнопки с обязательными параметрами запрашивают их следующим сообщением
- Неизвестные callback-действия отклоняются и не выполняются

**6. Веб-чат:**
- Открой `http://localhost:8080/chat` после запуска `SynchronousWeb`
- Страница объединяет нормализованную историю сообщений из Kafka-топиков `Telegram` и `Discord`
- Новые сообщения появляются в реальном времени через Server-Sent Events; историю можно фильтровать по платформе и чату
- История сохраняется в Postgres; при первом запуске веб-модуль читает записи Kafka с начала своей consumer group
- Новые изображения из Telegram и Discord сохраняются на локальный диск и открываются в чате по клику в большом просмотрщике
- Оригинальные URL файлов не публикуются: изображения отдаются через защищённый preview endpoint веб-модуля
- Поддерживаются JPEG, PNG, GIF и WebP до 10 MB; лимит настраивается через `CHAT_MAX_IMAGE_SIZE`
- При заданном `CHAT_ACCESS_TOKEN` страница запрашивает токен и создаёт короткую HTTP-only сессию; без него доступ открыт для локальной разработки

Параметры веб-чата:

| Переменная | По умолчанию | Что это |
|---|---:|---|
| `CHAT_ACCESS_TOKEN` | пусто | Токен доступа к истории и изображениям |
| `CHAT_KAFKA_GROUP_ID` | `web-chat` | Consumer group веб-модуля |
| `CHAT_KAFKA_AUTO_OFFSET_RESET` | `earliest` | С какой позиции читать Kafka при новой consumer group |
| `CHAT_TELEGRAM_TOPIC` | `Telegram` | Kafka-топик Telegram-сообщений |
| `CHAT_DISCORD_TOPIC` | `Discord` | Kafka-топик Discord-сообщений |
| `CHAT_MEDIA_DIR` | `./data/chat-media` | Каталог локального хранения изображений |
| `CHAT_MAX_IMAGE_SIZE` | `10485760` | Максимальный размер изображения в байтах (10 MB) |

Каталог `CHAT_MEDIA_DIR` содержит локальные копии изображений и не должен синхронизироваться с Git или публиковаться без дополнительной защиты.

# P.S.
Проект в разработке. Сборка выполняется отдельным Gradle wrapper каждого модуля; Docker Compose используется только для Postgres и Kafka.
