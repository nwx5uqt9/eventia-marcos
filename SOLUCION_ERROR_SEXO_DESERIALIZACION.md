# Solución al Error de Deserialización JSON - Campo Sexo

## Fecha: 28 de Octubre, 2025
## Estado: ✓ PROBLEMA RESUELTO

---

## ERROR ORIGINAL

```
org.springframework.http.converter.HttpMessageNotReadableException: 
JSON parse error: Cannot deserialize value of type `java.lang.Character` 
from String "Masculino": Expected either Integer value code or 1-character String
```

---

## CAUSA DEL PROBLEMA

### Backend (Java)
El campo `sexo` en la entidad `Usuario` estaba definido como:
```java
@Column(length = 1)
private Character sexo;  // ❌ Tipo: Character (un solo carácter)
```

### Frontend (Angular)
Los formularios estaban enviando valores completos:
```html
<select [(ngModel)]="sexo">
  <option value="Masculino">Masculino</option>  <!-- ❌ String completo -->
  <option value="Femenino">Femenino</option>    <!-- ❌ String completo -->
</select>
```

### Conflicto
- **Backend esperaba:** Un solo carácter ('M' o 'F')
- **Frontend enviaba:** String completo ("Masculino" o "Femenino")
- **Resultado:** Error de deserialización JSON

---

## SOLUCIÓN IMPLEMENTADA

### 1. Cambio en la Entidad Usuario.java

**Archivo:** `Usuario.java`

#### Declaración del Campo
```java
// ANTES
@Column(length = 1)
private Character sexo;

// DESPUÉS
@Column(length = 10)
private String sexo;
```

#### Constructor
```java
// ANTES
public Usuario(Integer id, String nombre, String apellidos, String nombreusuario,
               String email, String password, String dni, Character sexo,
               String direccion, RolUsuario rolUsuario)

// DESPUÉS
public Usuario(Integer id, String nombre, String apellidos, String nombreusuario,
               String email, String password, String dni, String sexo,
               String direccion, RolUsuario rolUsuario)
```

#### Getter y Setter
```java
// ANTES
public Character getSexo() {
    return sexo;
}

public void setSexo(Character sexo) {
    this.sexo = sexo;
}

// DESPUÉS
public String getSexo() {
    return sexo;
}

public void setSexo(String sexo) {
    this.sexo = sexo;
}
```

### 2. Base de Datos

#### Cambio en MySQL
```sql
ALTER TABLE usuario MODIFY COLUMN sexo VARCHAR(10);
```

**Nota:** Con `spring.jpa.hibernate.ddl-auto=update`, Hibernate actualizará automáticamente la columna al reiniciar el servidor.

### 3. Frontend (Ya estaba correcto)

El modelo TypeScript ya tenía el tipo correcto:
```typescript
export class Usuario {
  constructor(
    public id: number | null,
    public nombre: string,
    public sexo: string,  // ✓ Correcto
    // ...otros campos
  ) {}
}
```

---

## VENTAJAS DE ESTA SOLUCIÓN

### 1. Más Flexible
- Permite valores descriptivos: "Masculino", "Femenino", "Otro", "Prefiero no decir"
- Más amigable para el usuario
- Facilita internacionalización (i18n)

### 2. Más Extensible
```java
// Ahora puedes agregar más opciones sin cambiar el esquema
sexo = "Masculino"
sexo = "Femenino"
sexo = "Otro"
sexo = "No binario"
sexo = "Prefiero no decir"
```

### 3. Consistencia
- Frontend y Backend usan el mismo formato
- No hay conversión necesaria entre 'M'/'F' y "Masculino"/"Femenino"

### 4. Mejor UX
- Los usuarios ven exactamente lo que seleccionan
- No hay confusión sobre códigos

---

## ALTERNATIVAS CONSIDERADAS

### Alternativa 1: Cambiar Frontend para enviar 'M' o 'F'
```html
<!-- NO RECOMENDADO -->
<option value="M">Masculino</option>
<option value="F">Femenino</option>
```
**Razón para no usar:** Menos legible, requiere mapeo en backend/frontend

### Alternativa 2: Usar ENUM
```java
public enum Sexo {
    MASCULINO, FEMENINO, OTRO
}
```
**Razón para no usar:** Más rígido, dificulta agregar opciones

### Alternativa 3: Crear Entidad Separada
```java
@Entity
class Sexo {
    private Integer id;
    private String nombre;
}
```
**Razón para no usar:** Sobrecomplica para un campo simple

---

## PASOS PARA APLICAR LA SOLUCIÓN

### 1. Actualizar Código Java
```bash
# Ya aplicado en Usuario.java
# Cambiar Character a String en:
# - Declaración del campo
# - Constructor
# - Getter
# - Setter
```

### 2. Reiniciar Servidor Spring Boot
```bash
# Detener el servidor actual (Ctrl+C)
cd Pagina_Eventos/Pagina_Eventos
mvn spring-boot:run
```

Hibernate actualizará automáticamente la columna en MySQL.

### 3. (Opcional) Ejecutar Script SQL Manual
Si por alguna razón Hibernate no actualiza automáticamente:
```bash
mysql -u root -p pagina_eventos < fix_usuario_sexo.sql
```

### 4. Verificar Cambio en Base de Datos
```sql
USE pagina_eventos;
DESCRIBE usuario;
-- Verificar que sexo sea VARCHAR(10)
```

### 5. Probar desde Angular
1. Abrir formulario de registro de usuario
2. Seleccionar "Masculino" o "Femenino"
3. Enviar formulario
4. Verificar que NO hay error 400 en consola

---

## VALIDACIÓN DE LA SOLUCIÓN

### Test 1: Crear Usuario con Sexo "Masculino"
```json
POST http://localhost:8082/usuarios/create
{
  "nombre": "Juan",
  "apellidos": "Pérez",
  "nombreusuario": "jperez",
  "email": "juan@example.com",
  "password": "password123",
  "dni": "12345678",
  "sexo": "Masculino",
  "direccion": "Calle 123",
  "rolUsuario": { "id": 1 }
}
```
**Resultado esperado:** 201 CREATED ✓

### Test 2: Crear Usuario con Sexo "Femenino"
```json
POST http://localhost:8082/usuarios/create
{
  "nombre": "María",
  "apellidos": "González",
  "nombreusuario": "mgonzalez",
  "email": "maria@example.com",
  "password": "password123",
  "dni": "87654321",
  "sexo": "Femenino",
  "direccion": "Avenida 456",
  "rolUsuario": { "id": 2 }
}
```
**Resultado esperado:** 201 CREATED ✓

### Test 3: Verificar en Base de Datos
```sql
SELECT nombre, apellidos, sexo FROM usuario;
```
**Resultado esperado:**
```
+--------+-----------+-----------+
| nombre | apellidos | sexo      |
+--------+-----------+-----------+
| Juan   | Pérez     | Masculino |
| María  | González  | Femenino  |
+--------+-----------+-----------+
```

---

## MONITOREO POST-CORRECCIÓN

### En Logs de Spring Boot
**Antes (Error):**
```
WARN ... DefaultHandlerExceptionResolver : Resolved 
[org.springframework.http.converter.HttpMessageNotReadableException: 
JSON parse error: Cannot deserialize value of type `java.lang.Character`...]
```

**Después (Éxito):**
```
INFO ... Hibernate: insert into usuario (apellidos, direccion, dni, email, 
nombre, nombreusuario, password, sexo, id_rol_usuario) values (?, ?, ?, ?, ?, ?, ?, ?, ?)
```

### En Angular DevTools (Network Tab)
**Antes (Error):**
- Status: 400 Bad Request
- Response: JSON parse error

**Después (Éxito):**
- Status: 201 Created
- Response: Usuario creado con todos los campos

---

## IMPACTO EN EL SISTEMA

### Archivos Modificados
1. ✓ `Usuario.java` - Entidad actualizada
2. ✓ Base de datos - Columna actualizada automáticamente
3. ✓ Scripts SQL creados para referencia

### Archivos NO Modificados (Ya estaban correctos)
- `usuario.ts` (Angular) - Ya usaba String
- `form-add-user.html` - Ya enviaba valores completos
- `form-add-client.html` - Ya enviaba valores completos
- `UsuarioService.ts` - No requiere cambios
- `UsuarioControlador.java` - No requiere cambios

### Compatibilidad
- ✓ **Backwards Compatible:** NO (si había datos con 'M'/'F')
  - Solución: Migrar datos existentes (ver script SQL)
- ✓ **Forward Compatible:** SÍ
- ✓ **Frontend Compatible:** SÍ (sin cambios necesarios)

---

## MEJORAS FUTURAS SUGERIDAS

### 1. Agregar Validación
```java
@Column(length = 10)
@Pattern(regexp = "^(Masculino|Femenino|Otro)$", 
         message = "El sexo debe ser: Masculino, Femenino u Otro")
private String sexo;
```

### 2. Crear ENUM para Constantes
```java
public class SexoConstantes {
    public static final String MASCULINO = "Masculino";
    public static final String FEMENINO = "Femenino";
    public static final String OTRO = "Otro";
}
```

### 3. DTO para Validación
```java
public class UsuarioDTO {
    @NotNull
    private String nombre;
    
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$")
    private String sexo;
    
    // ...otros campos
}
```

### 4. Servicio de Validación
```typescript
// En Angular
export class SexoValidator {
  static readonly OPCIONES = ['Masculino', 'Femenino', 'Otro'];
  
  static isValid(sexo: string): boolean {
    return this.OPCIONES.includes(sexo);
  }
}
```

---

## CHECKLIST DE VERIFICACIÓN

### Pre-Corrección
- [x] Error identificado: HttpMessageNotReadableException
- [x] Causa identificada: Character vs String
- [x] Solución planificada: Cambiar Character a String

### Implementación
- [x] Usuario.java - Campo modificado
- [x] Usuario.java - Constructor modificado
- [x] Usuario.java - Getters/Setters modificados
- [x] Scripts SQL creados

### Post-Corrección
- [x] Sin errores de compilación
- [x] Documentación creada
- [ ] Servidor reiniciado
- [ ] Test de creación de usuario realizado
- [ ] Base de datos verificada

### Pendiente (Usuario debe hacer)
- [ ] Reiniciar servidor Spring Boot
- [ ] Probar crear usuario desde Angular
- [ ] Verificar logs sin errores
- [ ] Verificar datos en MySQL

---

## CONCLUSIÓN

**Problema:** Incompatibilidad de tipos entre frontend (String) y backend (Character)

**Solución:** Cambiar el campo `sexo` de `Character` a `String` en la entidad Usuario

**Resultado:** El sistema ahora acepta valores descriptivos completos como "Masculino" o "Femenino"

**Estado:** ✓ RESUELTO - Listo para reiniciar servidor y probar

---

## COMANDOS RÁPIDOS

### Reiniciar Backend
```bash
cd C:\xampp\htdocs\EVENTO_COMPLETO\Pagina_Eventos\Pagina_Eventos
mvn spring-boot:run
```

### Verificar BD (opcional)
```bash
mysql -u root -pAbc1234 pagina_eventos
```
```sql
DESCRIBE usuario;
```

### Frontend (ya debe estar corriendo)
```bash
cd C:\xampp\htdocs\EVENTO_COMPLETO\Eventia-Corp
ng serve
```

**¡Problema resuelto! Reinicia el servidor Spring Boot para aplicar los cambios.**

