mvn clean package
cp target/app-0.0.1-SNAPSHOT.jar docker/app.jar
docker build -t taskagile:latest docker/
docker run --rm --name taskagile -e "SPRING_PROFILES_ACTIVE=dev" \
    -p 8080:8080 -p 9000:9000 taskagile
