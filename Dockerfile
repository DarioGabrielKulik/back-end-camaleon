# IMAGEN MODELO
FROM eclipse-temurin:21-jdk

COPY target/url-0.0.1-SNAPSHOT.jar app.jar

# LEVANTAR NUESTRA APLICACION CUANDO EL CONTENEDOR INICIE
ENTRYPOINT ["java","-jar","/app.jar"]