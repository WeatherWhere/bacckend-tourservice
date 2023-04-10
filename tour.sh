#!/bin/bash

# 자바 프로젝트 build
echo "Building Spring Boot application..."
./gradlew clean build

#docker file을 참조해서 docker image 만들기
echo "Building Docker image..."
docker build -t tourservice .

# 같은 이미지로 컨테이너를 띄워졌을 때 포트가 중복되는 것을 막기 위해서
# 같은 이름의 container는 삭제하고 tour 이미지로 container 생성
echo "Starting Docker container..."
docker rm -f tourservice || true && docker run -it --name tourservice -p 8070:8080 tourservice

echo "Application started!"
