# Реактивное SPRING приложение, реализирующее REST API для сервиса задач.

## Описание работы
Приложение в соответсвии со стандартом `REST API` может по HTTP-запросу создавать, изменять, удалять и предоставлять
данные о задачах и пользователях, хранящиеся в базе данных `MongoDB`. Работа с базой данных реализована в реактивной
парадигме.

## Настройки

### Образ базы данных
В файле `docker/docker-compose.yml` указаны параметры образа базы данных `MongoDB` для платформы `Docker`.
Для ее создания необходимо в командной строке перейти в папку, где находится указанный выше файл, и ввести команду
`docker compose up`.

### Файл конфигурации
В файле конфигурации `src/resources/application.properties` указан параметр для подключения к базе данных, он
полностью соответствуют параметрам, использумым при создании образа в предыдущем пункте.

## Управление
Для работы с приложением необходимо пользоваться любым инструментом для тестирования HTTP-запросов:
* расширение для браузера [Chrome Talend API](https://chrome.google.com/webstore/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm)
  или [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=ru);
* приложение [Postman](https://www.postman.com/downloads/) [(видео, как пользоваться приложением)](https://youtu.be/V1fRT3RVCRw).

Описание обрабатываемых HTTP-запросов доступно в [API Doc](http://localhost:8080/webjars/swagger-ui/index.html) после
запуска приложения.