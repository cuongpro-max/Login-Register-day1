# Hướng Dẫn Test RESTful API (Dùng Postman / Bruno)

Dự án Spring Boot thuần, tối giản, không cấu hình rườm rà.

---

## 1. Cấu hình Database
Trong file [`src/main/resources/application.properties`](file:///d:/Java/Login-Register/src/main/resources/application.properties):
- URL: `jdbc:postgresql://localhost:5432/login_demo_db`
- Username: `postgres`
- Password: `(để trống nếu dùng chế độ trust)`

---

## 2. Danh sách API Test trên Postman / Bruno

### 🔹 1. Đăng ký tài khoản (Register)
- **Method & URL**: `POST http://localhost:8080/api/v1/auth/register`
- **Body (JSON)**:
```json
{
  "username": "admin",
  "password": "123",
  "name": "Quản Trị Viên",
  "phone": "0988888888"
}
```

---

### 🔹 2. Đăng nhập (Login)
- **Method & URL**: `POST http://localhost:8080/api/v1/auth/login`
- **Body (JSON)**:
```json
{
  "username": "admin",
  "password": "123"
}
```
- **Response**:
```json
{
  "message": "Đăng nhập thành công!",
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "e3a8904e-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

---

### 🔹 3. Lấy thông tin tài khoản hiện tại (User Info)
- **Method & URL**: `GET http://localhost:8080/api/v1/users/me`
- **Headers**:
  - Key: `Authorization`
  - Value: `Bearer <paste_accessToken_vào_đây>`
- **Response**:
```json
{
  "id": 1,
  "username": "admin",
  "name": "Quản Trị Viên",
  "phone": "0988888888"
}
```

---

### 🔹 4. Cấp lại Access Token mới (Refresh Token)
- **Method & URL**: `POST http://localhost:8080/api/v1/auth/refresh-token`
- **Body (JSON)**:
```json
{
  "refreshToken": "e3a8904e-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```
- **Response**:
```json
{
  "message": "Cấp mới Access Token thành công!",
  "accessToken": "eyJhbGciOi...chuoi_token_moi..."
}
```

---

### 🔹 5. Đăng xuất (Logout)
- **Method & URL**: `POST http://localhost:8080/api/v1/auth/logout`
- **Body (JSON)**:
```json
{
  "refreshToken": "e3a8904e-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```
