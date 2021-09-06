run keycloak:
docker run -d -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -v $(pwd):/tmp --name kc quay.io/keycloak/keycloak:15.0.2


Run mysql docker image:
docker run -d -v $(pwd):/tmp --name mysql -p 3306:3306 -e MYSQL_ROOT_USERNAME=root -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=time mysql:5.7

Run spring boot image:
1. docker build .
2. 



Run Rabbit:
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8-management

0.0.0.0:15672 > guest/guest


Keycloak test:


https://medium.com/devops-dudes/securing-spring-boot-rest-apis-with-keycloak-1d760b2004e

http://localhost:8180/auth/realms/Demo-Realm/protocol/openid-connect/token

get access token:

curl -X POST 'http://localhost:8180/auth/realms/Demo-Realm/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=password' \
 --data-urlencode 'client_id=springboot-microservice' \
 --data-urlencode 'client_secret=a5a6d38d-0615-4209-a0ec-96cc490beadb' \
 --data-urlencode 'username=employee1' \
 --data-urlencode 'password=mypassword'


access user api point:

curl -X GET 'http://localhost:8082/test/user' --header 'Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2ZzR5WHhURWstSHZHeHNRQ2FIWWM5MHgwRmk1ci1kTmpHXzRxYlYwakNJIn0.eyJleHAiOjE2MzAwNjM3NTQsImlhdCI6MTYzMDA2MzQ1NCwianRpIjoiZTY0YTdjMjktN2I5OS00MTNhLTgwNDUtYjIzZmFkODVkMGY2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL0RlbW8tUmVhbG0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZDQ0ZTUxODQtMjNiNS00MjBkLWFiMmEtZTVhYTEzMDA3YjE1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3ByaW5nYm9vdC1taWNyb3NlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiOTEzZDg5MWUtYjU5NC00YmNhLTg5ODAtMmU1MGJjZmNhZTFlIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtZGVtby1yZWFsbSIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJzcHJpbmdib290LW1pY3Jvc2VydmljZSI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI5MTNkODkxZS1iNTk0LTRiY2EtODk4MC0yZTUwYmNmY2FlMWUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImVtcGxveWVlMSJ9.OEbF0fIf6vR07Kvw7-k4GFFIDv6Q4LWbTczA8RQXIvyYtEOc7FWtNiIjYtPSS6J4PnFByy8Si4DYZBH3RZjVA_72NNL4Teg9tVCPYolgRk8ycNHc6WhnK5o6c9mjF7TsSl5wNzd-4Rcv8KOqQCuTkue6ftFDea5oB_d87kSBqC4k1gW6iCgwkh4y6TPKesWY_S4aMijDOOXkgN7cSQornBy15XJmXw9m1olRWmOdU3qwiIVDZTes2GaMkTCqc3aPRlHi-qmKdtc_U5muBPP2IxAhc_8RadNWe-5ua6mEJM8TJ9_BnbJ-Ghkm9nQe5-O6C1GeJkjxxJ0xDXotP8_13A'


curl -X GET 'http://localhost:8000/test/user' \
--header 'Authorization: bearer <ACCESS_TOKEN>'
Outputs:
anonymous: 403 Forbidden
employee1: Hello User
employee2: 403 Forbidden
employee3: Hello User
curl -X GET 'http://localhost:8000/test/admin' \
--header 'Authorization: bearer <ACCESS_TOKEN>'
Outputs:
anonymous: 403 Forbidden
employee1: 403 Forbidden
employee2: Hello Admin
employee3: Hello Admin
curl -X GET 'http://localhost:8000/test/all-user' \
--header 'Authorization: bearer <ACCESS_TOKEN>'
Outputs:
anonymous: 403 Forbidden
employee1: Hello All User
employee2: Hello All User
employee3: Hello All User