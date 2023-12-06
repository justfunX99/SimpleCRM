# SimpleCRM
## feature:
* JWT Authentication and Authorization
* (其它進行中…)

### 登入
* 登入頁面: http://localhost:8080/login
* 取得JWT token: Authorization in response header
* 登入成功後顯示swagger頁面: http://localhost:8080/swagger-ui/index.html
* 設定swagger請求自動帶JWT Token: 'Bearer ' + '<JWT token>' in Authorize Locker

### 切換帳號
* 重覆登入流程  

### 帳戶資訊確認: POST '/auth/check'  

### H2 Console
* 資料庫管理介面: http://localhost:8080/h2
* Settings: H2 embedded
* Driver Class: org.h2.Driver
* JDBC URL: jdbc:h2:mem:dbtest
* User Name/Password: sa/sa  

### 測試帳號
* super_user: ray/ray
* manager: may/may
* operator: jay/jay




