run keycloak:
docker run -d -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -v $(pwd):/tmp --name kc quay.io/keycloak/keycloak:15.0.2


Run mysql docker image:
docker run -d --name mysql -p 3307:3306 -e MYSQL_ROOT_USERNAME=root -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=time mysql:5.7

Run spring boot image:
1. docker build .
2. 