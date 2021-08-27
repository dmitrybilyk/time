run keycloak:
docker run -d -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -v $(pwd):/tmp --name kc quay.io/keycloak/keycloak:15.0.2


Run mysql docker image:
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_USERNAME=root -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=time mysql:5.7

Run spring boot image:
1. docker build .
2. 





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

curl -X GET 'http://localhost:8082/test/user' --header 'Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2ZzR5WHhURWstSHZHeHNRQ2FIWWM5MHgwRmk1ci1kTmpHXzRxYlYwakNJIn0.eyJleHAiOjE2MzAwNjI1MTIsImlhdCI6MTYzMDA2MjIxMiwianRpIjoiNDJlMWVlZWYtMTY3OS00ZDU5LWI2YmYtZDZiODgxODMzZTNlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL0RlbW8tUmVhbG0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZDQ0ZTUxODQtMjNiNS00MjBkLWFiMmEtZTVhYTEzMDA3YjE1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic3ByaW5nYm9vdC1taWNyb3NlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiM2JhN2ZlOTEtYzVhZS00YjU4LWE2NWUtZTA2ZDY4YTgwNTI3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtZGVtby1yZWFsbSIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJzcHJpbmdib290LW1pY3Jvc2VydmljZSI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIzYmE3ZmU5MS1jNWFlLTRiNTgtYTY1ZS1lMDZkNjhhODA1MjciLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImVtcGxveWVlMSJ9.fP1ao5PO_MRG71XOCCbosZ199JxZ3ZBbk5S_DYAx0oT3RZjjf8IBOwrzTPx9VGl_5RlV8j-83dkozmFhr-OrC-Z7gJ3K6-q7j2k1NyBlEl2a0vXu181FCzuuCOKBUF50ekbPIM-yMJs_GGB1Wo7JOKj13s7vaUfHyqLUdexGwKQft3yIVysSHAGH0-VeIrmCqxvNnl-ssNyxXlMakScijiEcsWkRL2ycL4HKNnt5BftAbzk5rrv7QjAnT6hNeEU5o3nXeN70CeMhdjCHwoGDWUDQlmwEjT6sumTKDE5nDk7BTQVj8tkq3JL1KG9cdd_o6b2zyiDS22E-fTDk7gAWFw'


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