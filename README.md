# Hướng Dẫn Test RESTful API (Spring Boot + PostgreSQL)

Dự án gồm 2 bảng riêng biệt (`users` và `refresh_tokens`), tích hợp bảo mật mật khẩu BCrypt, JWT Token, và Swagger UI.

---

## 🚀 1. Swagger UI (Giao diện Test trực quan)
Sau khi chạy ứng dụng, truy cập Swagger UI trực tiếp trên trình duyệt tại:
👉 **`http://localhost:8080/swagger-ui/index.html`**

*(Có sẵn nút **Authorize 🔒** để dán Access Token test trực tiếp trên trình duyệt)*

---

## ⚙️ 2. Cấu hình Biến Môi Trường (`.env`)
Kiểm tra file [`.env`](file:///d:/Java/Login-Register/.env) ở thư mục gốc:
```env
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/login_demo_db
DB_USERNAME=postgres
DB_PASSWORD=
JWT_SECRET=day-la-chuoi-secret-key-rat-dai-va-an-toan-cho-jwt-32bytes-123456
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000
```

---

## 📡 3. Danh sách API Test trên Bruno / Postman

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
  "refreshToken": "6483ffd3-908c-46d9-856a-e21c60c244f0"
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
  "refreshToken": "6483ffd3-908c-46d9-856a-e21c60c244f0"
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
  "refreshToken": "6483ffd3-908c-46d9-856a-e21c60c244f0"
}
```
