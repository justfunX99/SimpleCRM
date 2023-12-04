# SimpleCRM
## feature:
* JWT Authentication and Authorization
* (其它進行中…)

## API List:
### POST '/auth/login'
* request  
form data parameters: username, password

* response  
header的Set-Cookie取得idToken的值為JWT Token(後續在header的Authorization加入)

## Platform User for test: (可先用/api/companies/ 相關API測試AA)
* super_user: ray/ray
* manager: may/may
* operator: jay/jay




