# Keycloak Adapter

Librería compartida de seguridad — configuración centralizada JWT/Keycloak.

## Propósito

Proporciona la configuración de validación JWT (`spring-boot-starter-oauth2-resource-server`) como dependencia reutilizable para todos los servicios que necesiten validar tokens emitidos por Keycloak.

## Uso

Agregar como dependencia en el `pom.xml` del servicio:

```xml
<dependency>
    <groupId>cl.duoc.sanosysalvos</groupId>
    <artifactId>keycloak-adapter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quién lo usa

| Componente | Uso |
|---|---|
| **API Gateway** | Validación JWT en el punto de entrada |
