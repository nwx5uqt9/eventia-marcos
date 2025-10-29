# Solución: Tablas No Se Crean Automáticamente con Hibernate

## Problema
Las tablas no se están generando automáticamente en MySQL cuando se inicia Spring Boot.

## Causa Probable
1. La base de datos `pagina_eventos` no existe
2. Hibernate no tiene permisos para crear tablas
3. Las entidades no están siendo escaneadas correctamente
4. Configuración incorrecta en application.properties

## SOLUCIÓN PASO A PASO

### Paso 1: Verificar que MySQL está corriendo

**Windows (XAMPP):**
1. Abrir XAMPP Control Panel
2. Verificar que MySQL tenga el botón "Stop" (verde)
3. Si dice "Start", hacer clic para iniciar MySQL

**Verificar manualmente:**
```bash
mysql -u root -pAbc1234 -e "SELECT 'MySQL está corriendo' AS status"
```

### Paso 2: Crear la base de datos manualmente

**Opción A: Desde línea de comandos**
```bash
cd C:\xampp\htdocs\EVENTO_COMPLETO
mysql -u root -pAbc1234 < crear_base_datos.sql
```

**Opción B: Desde phpMyAdmin**
1. Abrir http://localhost/phpmyadmin
2. Click en "Nueva" (o "New")
3. Nombre de base de datos: `pagina_eventos`
4. Cotejamiento: `utf8mb4_unicode_ci`
5. Click en "Crear"

**Opción C: Desde MySQL Workbench**
1. Abrir MySQL Workbench
2. Conectar al servidor local
3. Ejecutar:
```sql
CREATE DATABASE IF NOT EXISTS pagina_eventos 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Paso 3: Verificar que la base de datos existe

```bash
mysql -u root -pAbc1234 -e "SHOW DATABASES LIKE 'pagina_eventos'"
```

Debe mostrar:
```
+-----------------------------+
| Database (pagina_eventos)   |
+-----------------------------+
| pagina_eventos              |
+-----------------------------+
```

### Paso 4: Detener Spring Boot si está corriendo

En la terminal donde corre Spring Boot, presiona: **Ctrl + C**

### Paso 5: Limpiar y recompilar el proyecto

```bash
cd C:\xampp\htdocs\EVENTO_COMPLETO\Pagina_Eventos\Pagina_Eventos
mvn clean install -DskipTests
```

### Paso 6: Iniciar Spring Boot

```bash
mvn spring-boot:run
```

### Paso 7: Verificar logs de Spring Boot

Busca en los logs estas líneas que indican que Hibernate está creando tablas:

```
Hibernate: create table rol_usuario (...)
Hibernate: create table usuario (...)
Hibernate: create table tipo_evento (...)
Hibernate: create table estado_evento (...)
Hibernate: create table organizador (...)
Hibernate: create table ubicacion (...)
Hibernate: create table eventos (...)
Hibernate: create table boleto (...)
Hibernate: create table boleta_venta (...)
Hibernate: create table lista_evento (...)
Hibernate: create table lista_boleto (...)
Hibernate: create table lista_boletos_pagados (...)
```

Si ves `alter table` en lugar de `create table`, significa que las tablas ya existen.

### Paso 8: Verificar que las tablas se crearon

```bash
mysql -u root -pAbc1234 -e "USE pagina_eventos; SHOW TABLES;"
```

Deberías ver:
```
+---------------------------+
| Tables_in_pagina_eventos  |
+---------------------------+
| boleta_venta              |
| boleto                    |
| estado_evento             |
| eventos                   |
| lista_boleto              |
| lista_boletos_pagados     |
| lista_evento              |
| organizador               |
| rol_usuario               |
| tipo_evento               |
| ubicacion                 |
| usuario                   |
+---------------------------+
12 rows in set
```

## CONFIGURACIÓN ACTUALIZADA

Tu `application.properties` ahora tiene:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pagina_eventos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Abc1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Cambios importantes:
1. ✓ `ddl-auto=update` (en lugar de create)
2. ✓ `show-sql=true` para ver las queries
3. ✓ `format_sql=true` para mejor legibilidad
4. ✓ Logging DEBUG para Hibernate
5. ✓ Parámetros adicionales en la URL de conexión

## TROUBLESHOOTING

### Problema 1: "Access denied for user 'root'@'localhost'"

**Causa:** Contraseña incorrecta

**Solución:**
```bash
# Verificar contraseña
mysql -u root -p
# Ingresar contraseña cuando lo pida

# Si no funciona, cambiar contraseña
mysqladmin -u root password "Abc1234"
```

### Problema 2: "Unknown database 'pagina_eventos'"

**Causa:** La base de datos no existe

**Solución:** Ejecutar Paso 2 de la guía (crear base de datos)

### Problema 3: Las tablas no se crean pero no hay errores

**Causa:** Hibernate no encuentra las entidades

**Solución:** Verificar que todas las entidades tengan:
```java
@Entity
@Table(name = "nombre_tabla")
public class MiEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // ...
}
```

Y que estén en el paquete: `com.Pagina_Eventos.Pagina_Eventos.Entidad`

### Problema 4: "Table already exists"

**Causa:** Estás usando `ddl-auto=create` y las tablas ya existen

**Solución:** Cambiar a `ddl-auto=update` (ya está configurado)

### Problema 5: Tablas se crean pero sin datos

**Causa:** Normal, las tablas se crean vacías

**Solución:** Ejecutar el script de inserción de roles:
```bash
mysql -u root -pAbc1234 pagina_eventos < insert_roles_basicos.sql
```

## DIFERENCIAS ENTRE DDL-AUTO

### `create`
- Elimina todas las tablas existentes
- Crea las tablas desde cero
- **PELIGRO:** Pierdes todos los datos
- Usar solo en desarrollo inicial

### `create-drop`
- Crea las tablas al iniciar
- Las elimina al cerrar la aplicación
- Útil para testing

### `update` ✓ RECOMENDADO
- Crea las tablas si no existen
- Actualiza la estructura si cambias las entidades
- NO elimina datos existentes
- Seguro para desarrollo

### `validate`
- Solo valida que la estructura coincida
- No crea ni modifica tablas
- Útil para producción

### `none`
- Hibernate no hace nada
- Debes crear las tablas manualmente
- Más control, más trabajo

## COMANDOS ÚTILES

### Ver estructura de una tabla
```sql
USE pagina_eventos;
DESCRIBE usuario;
```

### Ver todas las tablas con detalles
```sql
USE pagina_eventos;
SHOW TABLE STATUS;
```

### Eliminar todas las tablas (CUIDADO)
```sql
USE pagina_eventos;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS lista_boletos_pagados;
DROP TABLE IF EXISTS lista_boleto;
DROP TABLE IF EXISTS lista_evento;
DROP TABLE IF EXISTS boleta_venta;
DROP TABLE IF EXISTS boleto;
DROP TABLE IF EXISTS eventos;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS ubicacion;
DROP TABLE IF EXISTS organizador;
DROP TABLE IF EXISTS estado_evento;
DROP TABLE IF EXISTS tipo_evento;
DROP TABLE IF EXISTS rol_usuario;
SET FOREIGN_KEY_CHECKS = 1;
```

### Recrear tablas desde cero
```bash
# 1. Detener Spring Boot (Ctrl+C)
# 2. Eliminar tablas (comando anterior)
# 3. Cambiar temporalmente a create
#    spring.jpa.hibernate.ddl-auto=create
# 4. Iniciar Spring Boot
#    mvn spring-boot:run
# 5. Esperar a que cree las tablas
# 6. Detener Spring Boot
# 7. Cambiar de vuelta a update
#    spring.jpa.hibernate.ddl-auto=update
# 8. Insertar roles básicos
#    mysql -u root -pAbc1234 pagina_eventos < insert_roles_basicos.sql
```

## VERIFICACIÓN FINAL

Una vez que Spring Boot esté corriendo, ejecuta:

```bash
mysql -u root -pAbc1234 pagina_eventos -e "
SELECT 
    TABLE_NAME, 
    TABLE_ROWS,
    CREATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'pagina_eventos'
ORDER BY TABLE_NAME;
"
```

Deberías ver las 12 tablas con sus fechas de creación.

## LOGS ESPERADOS EN SPRING BOOT

Cuando todo funciona correctamente, deberías ver:

```
2025-10-29 ... : HHH000412: Hibernate ORM core version ...
2025-10-29 ... : HHH000400: Using dialect: org.hibernate.dialect.MySQLDialect
2025-10-29 ... : HHH000476: Executing import script 'org.hibernate.tool.schema.internal.exec.ScriptSourceInputNonExistentImpl@...'
2025-10-29 ... : Hibernate: create table rol_usuario ...
2025-10-29 ... : Hibernate: create table usuario ...
...
2025-10-29 ... : Started PaginaEventosApplication in X.XXX seconds
```

## SIGUIENTE PASO

Una vez que las tablas estén creadas:

1. Insertar roles básicos:
```bash
mysql -u root -pAbc1234 pagina_eventos < insert_roles_basicos.sql
```

2. Verificar los roles:
```bash
mysql -u root -pAbc1234 pagina_eventos -e "SELECT * FROM rol_usuario"
```

3. Iniciar el frontend Angular:
```bash
cd C:\xampp\htdocs\EVENTO_COMPLETO\Eventia-Corp
ng serve
```

4. Abrir http://localhost:4200

## CHECKLIST

- [ ] MySQL está corriendo
- [ ] Base de datos `pagina_eventos` existe
- [ ] application.properties configurado correctamente
- [ ] Spring Boot compilado sin errores
- [ ] Spring Boot iniciado correctamente
- [ ] Logs muestran creación de tablas
- [ ] `SHOW TABLES` muestra 12 tablas
- [ ] Roles básicos insertados
- [ ] Frontend Angular corriendo
- [ ] Puedes crear usuarios desde la interfaz

## RESULTADO ESPERADO

```sql
mysql> USE pagina_eventos;
Database changed

mysql> SHOW TABLES;
+---------------------------+
| Tables_in_pagina_eventos  |
+---------------------------+
| boleta_venta              |
| boleto                    |
| estado_evento             |
| eventos                   |
| lista_boleto              |
| lista_boletos_pagados     |
| lista_evento              |
| organizador               |
| rol_usuario               |
| tipo_evento               |
| ubicacion                 |
| usuario                   |
+---------------------------+
12 rows in set (0.00 sec)
```

¡Problema resuelto! Las tablas deberían crearse automáticamente.

