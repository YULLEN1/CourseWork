# Автоматизация тестирования сервиса по покупке тура

## Бизнес-часть
Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:

1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

* сервису платежей, далее Payment Gate;
* кредитному сервису, далее Credit Gate.

## Начало работы
### Prerequisites

Перед написанием автотестов на компьютер важно установить:
* IntelliJ IDEA Community Edition;
* Git;
* браузер Google Chrome;
* SDK Java 11;
* Docker Desktop;
* DBeaver.

### Установка и запуск

#### Запуск SUT, авто-тестов и генерация репорта

1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале в корне проекта запустить контейнер:

`docker-compose up -d`

4. Запустить приложение:

`java -jar artifacts/aqa-shop.jar`

5. Открыть в браузере адрес "localhost:8080"

5. Открыть второй терминал

6. Запустить тесты:

`.\gradlew clean test`

7. Создать отчёт Allure и открыть в браузере

`.\gradlew allureServe`

8. Закрыть отчёт:

`CTRL + C -> y -> Enter`

9. Перейти в первый терминал

10. Остановить приложение:

`CTRL + C`

11. Остановить контейнеры:

`docker-compose down`

### Лицензия
Лицензия - свободная.

### Документация
1. [План автоматизации](https://github.com/YULLEN1/CourseWork/blob/main/docs/Plan.md)
2. [Отчёт по итогам тестирования](https://github.com/YULLEN1/CourseWork/blob/main/docs/Report.md)
3. [Отчёт по итогам автоматизации](https://github.com/YULLEN1/CourseWork/blob/main/docs/Summary.md)

