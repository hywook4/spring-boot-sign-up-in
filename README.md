# Sign-up-in
---
## Steps to run on local
1. Build the Java application 
```
./gradlew clean build
```

2. Build the Docker image
```
docker build -t sign-up-in:0.0.1 .
```

3. Run docker-compose
```
// to run as daemon
docker-compose up -d

// else
docker-compose up
```


## APIs

### POST - /sms-auth
Request for SMS authentication


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|name      |string      |O        |
|phoneNumber      |string      |O          |


#### Success Response
Code: 200 (success)
Content:
~~~
// if running on local
{{SMS VERFICATION CODE}}
~~~

---

### POST - /sms-auth/verify
Verify the SMS verificationCode


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|name      |string      |O        |
|phoneNumber      |string       |O          |
|verificationCode      |int       |O          |


#### Success Response
Code: 200 (success)
Content:
~~~
{
    "name": {NAME_STRING},
    "phoneNumber": {PHONE_NUMBER_STRING},
    "verifiedToken": {SMS_VERIFIED_TOKEN}
}
~~~

---

### POST - /users/sign-up
Request for sign-up


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |
|X-Sms-VerifiedToken   |{SMS_VERIFIED_TOKEN}       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|email      |string      |O        |
|nickname      |string       |O          |
|name      |string       |O          |
|phoneNumber      |string       |O          |
|password      |string       |O          |


#### Success Response
Code: 200 (success)
Content:
~~~
{
    "email": {EMAIL_STRING},
    "nickname": {NICK_NAME_STRING},
    "name": {NAME_STRING},
    "phoneNumber": {PHONE_NUMBER_STRING}
}
~~~

---

### POST - /users/change-password
Request change password


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |
|X-Sms-VerifiedToken   |{SMS_VERIFIED_TOKEN}       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|name      |string      |O        |
|phoneNumber      |string       |O          |
|password      |string       |O          |


#### Success Response
Code: 200 (success)
Content:
~~~

~~~

---

### POST - /users/sign-in
Request for sign-in


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|idField      |string("email" or "phone_number"      |O        |
|idValue      |string       |O          |
|password      |string       |O          |


#### Success Response
Code: 200 (success)
Content:
~~~
{
    "email": {EMAIL_STRING},
    "nickname": {NICK_NAME_STRING},
    "name": {NAME_STRING},
    "phoneNumber": {PHONE_NUMBER_STRING}
    "jwtToken": {JWT_TOKEN}
}
~~~

---

### GET - /users/my-info
Request for my info


#### Header
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|Content-Type   |application/json       |O        |
|Authorization   |Bearer {JWT_TOKEN}       |O        |


#### Body Parameters
| KEY  | VALUE | REQUIRED |
| ---- | ----- | -------- |
|      |       |          |


#### Success Response
Code: 200 (success)
Content:
~~~
{
    "email": {EMAIL_STRING},
    "nickname": {NICK_NAME_STRING},
    "name": {NAME_STRING},
    "phoneNumber": {PHONE_NUMBER_STRING}
}
~~~
