![](./images/logos_feder.png)



| Entregable     | Procesador de entrada                                        |
| -------------- | ------------------------------------------------------------ |
| Fecha          | 17/12/2020                                                   |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](https://www.crue.org/proyecto/hercules/) |
| Módulo         | Procesador de datos                                          |
| Tipo           | Software                                                     |
| Objetivo       | Procesador de datos del módulo de entrada para el proyecto Backend SGI (ASIO). Recibe los datos del importador y los guarda en una base de datos intermedia |
| Estado         | **100%**  |
| Próximos pasos |   |
| Documentación  | [Manual de usuario](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/00-An%C3%A1lisis/Manual%20de%20usuario/Manual%20de%20usuario.md)<br />[Manual de despliegue](https://github.com/HerculesCRUE/ib-asio-composeset/blob/master/README.md)<br />[Documentación técnica](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/00-Arquitectura/arquitectura_semantica/documento_arquitectura/ASIO_Izertis_Arquitectura.md) |

# ASIO - Procesador de entrada

|     | Master |
| --- | ------ |
| Quality Gate | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=HerculesCRUE_ib-input-processor&metric=alert_status)](https://sonarcloud.io/dashboard?id=HerculesCRUE_ib-input-processor) |
| Coverage | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=HerculesCRUE_ib-input-processor&metric=coverage)](https://sonarcloud.io/dashboard?id=HerculesCRUE_ib-input-processor) |

Procesador de datos del módulo de entrada para el proyecto Backend SGI (ASIO). 

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11
* Eclipse JEE 2019-09 con plugins:
  * Spring Tools 4
  * m2e-apt
  * Lombok
* Docker

## Módulos disponibles

* **Módulo back**: módulo que añade una capa de servicios REST a la funcionalidad de la aplicación. Genera un artefacto JAR bootable
* **Módulo kafka**: módulo que añade la capacidad de consumir mensajes de un topic de Kafka
* **Módulo service**: módulo que contiene la lógica de la aplicación. Puede ser utilizado como librería independiente para ser integrado en otras aplicaciones
* **Módulo jpa-abstractions**: módulo con utilidades para el acceso a datos mediante JPA
* **Módulo service-abstractions**: módulo con utilidades para la generación de servicios
* **Módulo swagger**: módulo que contine la funcionalidad necesaria para añadir Swagger para la interacción con el API Rest

## Metodología de desarrollo

La metodología de desarrollo es Git Flow.

## Entorno de desarrollo Docker

La inicialización de los elementos adicionales al entorno de desarrollo se realiza con docker. 

En el directorio docker-devenv se ha configurado un fichero docker-compose.yml para poder arrancar el entorno de desarrollo.

Para arrancar el entorno:

```bash
docker-compose up -d
```

Para pararlo:

```bash
docker-compose down
```

## Enviar mensajes a general-data 

Si queremos saltarnos el proceso ETL y mandar directamente los datos processados de la cola *input-data* a *general-data* basta con configurar
la propiedad send-general-data-topic a true, false en caso contrario.

```xml 
app:
  kafka:
    send-general-data-topic: true
```

## Instalación en entorno real

Será preciso configurar las siguientes variables de entorno cuando se instale en un entorno real:

|Variable|Descripción|Valor por defecto|
|---|---|---|
|`APP_KAFKA_INPUT_TOPIC_NAME`|Nombre del topic de Kafka de entrada|input-data|
|`APP_KAFKA_GENERAL_CONTINGENCY_TOPIC_NAME`|Nombre del topic de Kafka general contingency|general-contingency-data|
|`APP_KAFKA_CREATE_TOPICS`|Flag que indica si debe crear automáticamente los topics de Kafka. Valores admisibles `true` y `false`|false|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | URL del servicio de Kafka para los productores | localhost:29092 |
| `SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS` | URL del servicio de Kafka para los consumidores | localhost:29092 |
| `SPRING_KAFKA_CONSUMER_GROUP_ID` | ID del grupo de consumidores | input-processor |
|`APP_PERSISTENCE_DATASOURCE_USERNAME`|Nombre del usuario de acceso a la base de datos| |
|`APP_PERSISTENCE_DATASOURCE_PASSWORD`|Contraseña del usuario de acceso a la base de datos| |
|`APP_PERSISTENCE_DATASOURCE_URL`|URL de acceso a la base de datos MariaDB|jdbc:mysql://localhost:3307/app?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&createDatabaseIfNotExist=true|
|`APP_KAFKA_SEND_GENERAL_DATA_TOPIC`|Flag que indica si se deben enviar los datos a la siguiente cola sin pasar por ETL. Valores admisibles `true` y `false`|true|
|`APP_SERVICES_ETL_ENDPOINT`| URL del servicio para invocar el proceso ETL | http://localhost:8080/kettle/runJob |
|`APP_SERVICES_ETL_JOB`| Nombre del job que se invocará al hacer la llamada al proceso ETL | main |
|`APP_SERVICES_ETL_USERNAME`| Nombre de usuario para invocar el proceso ETL | asioetl |
|`APP_SERVICES_ETL_PASSWORD`| Contraseña para invocar el proceso ETL | asioetl |
|`APP_MANAGEMENT_SYSTEM_URL`| Url para saber si el Management-System está corriendo (evitar importaciones concurrentes) | http://localhost:9321/etl-notifications/isMSRunning |

### Ejecución

Al generarse un JAR bootable la ejecución se realizará mediante el siguiente comando:

```bash
java -jar {jar-name}.jar
```

Sustituyendo `{jar-name}` por el nombre del fichero JAR generado.

No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

## Testing y cobertura

Se incluyen los resultados del testing y cobertura en los siguientes enlaces:

* [Testing](http://herc-iz-front-desa.atica.um.es:8070/input-processor/surefire/surefire-report.html)
* [Cobertura](https://sonarcloud.io/component_measures?id=HerculesCRUE_ib-input-processor&metric=coverage&view=list)

##  Documentación adicional

* [Compilación](docs/build.md)
* [Generación Docker](docs/docker.md)
