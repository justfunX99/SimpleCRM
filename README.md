# SimpleCRM
### tech
* Spring Boot
* H2 Database
* Maven
* Swagger Integration
* JWT Authentication and Authorization
* Docker integration(docker build, package and run)
* Deploy to cloud platform(railway.app)

### function
* 公司資料維護
* 客戶資料維護(包含多筆新增)
* 單元測試與整合測試
  
### 登入系統
* 登入頁面: http://localhost:8080/login
* 取得JWT token:  response header(Authorization) in '/auth/login'
* 登入成功後轉導swagger頁面: http://localhost:8080/swagger-ui/index.html
* 設定swagger請求自動帶JWT Token: 在Authorize Locker設定 'Bearer ' + '<JWT_token>'
* 帳戶資訊確認: GET '/auth/check'

### 切換帳號
* 重覆登入系統流程  

### H2 Console
* 資料庫管理介面: http://localhost:8080/h2
* Settings: H2 embedded
* Driver Class: org.h2.Driver
* JDBC URL: jdbc:h2:mem:dbtest
* User Name/Password: sa/sa  

### 雲平台部署 in railway.app 
* 登入頁面: https://simplecrm-production.up.railway.app

### 測試帳號與角色
* ray/ray: SUPER_USER
* may/may: MANAGER
* jay/jay: OPERATOR

  



