# Compilación

A continuación se indican los pasos que hay que seguir para llevar a cabo la generación del artefacto.

## Prerrequisitos

Se precisa disponer los siguientes elementos configurados:

* OpenJDK 11
* Maven 3.6.x

## Compilación

El siguiente comando se ejecuta para realizar la compilación:

```bash
mvn clean package
```

También es posible instalar o desplegar los artefactos sustituyendo `package` por `install` o `deploy` respectivamente.

Los artefactos se generarán dentro del directorio `target` de cada uno de los módulos:

### Back

Los artefactos se encuentran dentro de input-processor-back/target

* Artefacto: input-processor-back-{version}.jar

### Kafka

Los artefactos se encuentran dentro de input-processor-kafka/target

* Artefacto: input-processor-kafka-{version}.jar

### Service

Los artefactos se encuentran dentro de input-processor-service/target

* Artefacto: input-processor-service-{version}.jar

### Service Abstractions

Los artefactos se encuentran dentro de input-processor-service-abstractions/target

* Artefacto: input-processor-service-abstractions-{version}.jar

### JPA Abstractions

Los artefactos se encuentran dentro de input-processor-jpa-abstractions/target

* Artefacto: input-processor-jpa-abstractions-{version}.jar

### Swagger

Los artefactos se encuentran dentro de input-processor-swagger/target

* Artefacto: input-processor-swagger-{version}.jar
