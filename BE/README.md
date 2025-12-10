# KTPM_HK251 – Hướng dẫn chạy dự án với IntelliJ IDEA

## 1. Yêu cầu môi trường
- Java 17+
- IntelliJ IDEA (Community hoặc Ultimate)
- Maven 3.8+
- PostgreSQL
- Git (tùy chọn)

---

## 2. Clone dự án

```bash
git clone https://github.com/<your-repo>/KTPM_HK251.git
cd KTPM_HK251
```

---

## 3. Mở dự án bằng IntelliJ

1. Mở IntelliJ → File → Open
2. Chọn folder `KTPM_HK251`
3. IntelliJ sẽ tự động load Maven và tải dependencies.

Nếu IntelliJ chưa auto-import:
- Mở tab Maven → nhấn “Reload All Maven Projects”

---

## 4. Cấu hình biến môi trường bằng file `.env`

Dự án sử dụng placeholder trong `application.yaml`, vì vậy không đặt secret trực tiếp trong YAML.

Thực hiện:

1. Tạo file `.env` tại thư mục gốc của project:

```env
DB_PASSWORD=123456
JWT_SECRET=abcd1234
```

2. Trong IntelliJ:
   - Vào Run → Edit Configurations…
   - Chọn cấu hình Spring Boot (class có hàm `main`)
   - Ở mục **Environment variables**, nhấn vào dấu `...`
   - Chọn **Load variables from file…**
   - Trỏ tới file `.env`
   - Nhấn OK

IntelliJ sẽ tự nạp toàn bộ biến môi trường khi chạy ứng dụng.

---

## 5. Chạy dự án

### Cách 1: Chạy bằng IntelliJ
- Mở file chứa hàm `main`
- Nhấn Run ▶ hoặc Shift + F10

- Lưu ý: phải chạy service registry và api-gateway nếu như muốn gọi từ url của gateway
---

## 7. Kiểm tra ứng dụng

API Gateway chạy tại:

```
http://localhost:8765
```

Health check cho IAM Service:

```
http://localhost:8761/api/iam-service/users/health
```

---

## 9. Lưu ý
- Không commit file `.env` lên GitHub.
- Với môi trường production nên dùng Environment Variables hoặc Secret Manager.
