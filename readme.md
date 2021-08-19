Run mysql docker image:
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_USERNAME=root -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=time mysql:5.7

Run spring boot image:
1. docker build .
2. 