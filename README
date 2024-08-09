Эндпоинты API

Регистрация
URL: /auth/register
Метод: POST
Тело запроса:
{
    "name": "Иван Иванов",
    "phone": "+79001234567",
    "email": "ivan@example.com",
    "password": "password123",
    "repeatPassword": "password123",
    "role": "Студент"
}

Подтверждение регистрации
URL: /auth/confirm
Метод: POST
Параметры:
userId (Long) — идентификатор пользователя
code (String) — код подтверждения

Логин
URL: /auth/login
Метод: POST
Тело запроса:
{
    "phone": "+79001234567",
    "password": "password123"
}

Добавление курса
URL: /admin/courses/add
Метод: POST
Тело запроса:
{
    "name": "Название курса",
    "description": "Описание курса",
    "image": "URL изображения"
}

Удаление курса
URL: /admin/courses/delete/{id}
Метод: DELETE
Параметры:
id (Long) — идентификатор курса

Получение всех курсов
URL: /admin/courses/all
Метод: GET

Скачивание pdf файла
URL: /admin/courses/download/{id}
Метод: GET
Параметры:
id (Long) — идентификатор курса

Получение избранных курсов
URL: /users/{userId}/favorites
Метод: GET
Параметры:
userId (Long) — идентификатор пользователя

Добавление в избранное
URL: /users/{userId}/favorites/add/{courseId}
Метод: POST
Параметры:
courseId (Long) — идентификатор курса

Удаление из избранного
URL: /users/{userId}/favorites/delete/{courseId}
Метод: DELETE

