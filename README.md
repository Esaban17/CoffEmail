# CoffEmail

Aplicación de escritorio en Java (Swing) para el registro y la autenticación de usuarios.

## Descripción

CoffEmail gestiona el alta de usuarios: valida los datos del formulario, deriva la
contraseña con un algoritmo diseñado para ello, copia la fotografía de perfil y
persiste todo en un archivo de texto dentro del perfil del usuario.

> **Estado**: la aplicación implementa el registro y la verificación de credenciales.
> Todavía no incluye funcionalidad de correo electrónico ni pantalla de inicio de sesión.

## Características

- **Registro de usuarios** con validación campo por campo y mensajes específicos
- **Derivación de contraseñas** con PBKDF2-HMAC-SHA512 (210 000 iteraciones) y salt
  aleatorio por usuario, almacenado junto al hash para poder verificarlo después
- **Verificación de credenciales** con comparación de tiempo constante
- **Fotografías de perfil** validadas por contenido, no solo por extensión
- **Selector de fechas** gráfico para la fecha de nacimiento
- **Persistencia en archivo de texto** en UTF-8, con los campos escapados

## Requisitos

- JDK 17 o superior
- Apache Maven 3.8+
- Windows, Linux o macOS

## Instalación

```bash
git clone https://github.com/Esaban17/CoffEmail.git
cd CoffEmail
mvn clean package
```

## Uso

Con Maven:

```bash
mvn exec:java
```

O con el jar generado, que ya incluye sus dependencias y su `Main-Class`:

```bash
java -jar target/CoffEmail-1.0-SNAPSHOT.jar
```

## Pruebas

```bash
mvn test
```

## Estructura del proyecto

```
CoffEmail/
├── src/
│   ├── main/java/com/coffemail/
│   │   ├── models/      # User, Role
│   │   ├── security/    # PasswordHasher
│   │   ├── storage/     # AppPaths, DelimitedRecord, UserRepository, PhotoStore
│   │   ├── services/    # UserService, RegistrationValidator
│   │   └── views/       # RegisterView (formulario Swing)
│   └── test/java/       # Pruebas JUnit 5
├── lib/                 # Dependencias no publicadas en Maven Central
├── pom.xml
└── README.md
```

## Modelo de usuario

| Campo | Tipo | Notas |
|---|---|---|
| `username` | `String` | 3-32 caracteres; único, sin distinguir mayúsculas |
| `name` / `lastName` | `String` | obligatorios |
| `passwordHash` | `String` | `pbkdf2$<iteraciones>$<salt>$<hash>`; nunca la contraseña en claro |
| `role` | `Role` | `ADMIN` para el primer usuario, `USER` para el resto |
| `birthDate` | `LocalDate` | anterior a hoy, hasta 120 años atrás |
| `email` | `String` | validado por formato |
| `phone` | `String` | texto, para admitir código de país y ceros a la izquierda |
| `photoPath` | `String` | ruta de la copia dentro del almacén |
| `active` | `boolean` | una cuenta inactiva no puede autenticarse |

## Almacenamiento de datos

Por omisión, los datos viven en el perfil del usuario:

| Sistema | Ruta |
|---|---|
| Linux / macOS | `~/.coffemail/` |
| Windows | `C:\Users\<usuario>\.coffemail\` |

Dentro de ese directorio: `usuarios.txt` y `images/`.

La ubicación puede cambiarse con una propiedad de sistema:

```bash
java -Dcoffemail.data.dir=/ruta/personalizada -jar target/CoffEmail-1.0-SNAPSHOT.jar
```

El archivo usa `|` como separador y UTF-8 como codificación. Los caracteres `\`, `|`
y los saltos de línea se escapan (`\\`, `\p`, `\n`, `\r`), de modo que un campo que los
contenga no rompe el registro.

## Tecnologías

- **Java 17** — lenguaje y plataforma
- **Swing** — interfaz gráfica
- **Maven** — construcción y dependencias
- **JUnit 5** — pruebas
- **DateChooser** — componente de selección de fechas (jar versionado en `lib/`,
  no está publicado en Maven Central)

## Seguridad

- Contraseñas derivadas con **PBKDF2-HMAC-SHA512**, 210 000 iteraciones
  (mínimo recomendado por OWASP), salt aleatorio de 16 bytes por usuario
- El salt se guarda junto al hash, en un único campo autocontenido
- Comparación de tiempo constante mediante `MessageDigest.isEqual`
- Las contraseñas se manejan como `char[]` y se limpian tras usarse
- Los campos de contraseña de la interfaz son `JPasswordField`
- Las fotografías se validan por extensión, tamaño máximo y contenido real

## Notas de desarrollo

El formulario `RegisterView` se diseñó con el editor visual de NetBeans; el archivo
`RegisterView.form` acompaña a `RegisterView.java` y ambos deben mantenerse
sincronizados. `nbactions.xml` contiene las acciones de ejecución del IDE.

## Autor

**Estuardo Sabán** — [@Esaban17](https://github.com/Esaban17)

## Contribuciones

1. Haz fork del proyecto
2. Crea una rama para tu característica (`git checkout -b feature/nueva-caracteristica`)
3. Asegúrate de que `mvn test` pasa
4. Realiza commit de tus cambios
5. Abre un Pull Request

## Licencia

Este proyecto está en desarrollo. Por favor contacta al autor para información
sobre licencias.
