# CoffEmail

Sistema de registro de usuarios para correo electrónico, desarrollado en Java con arquitectura MVC.

## Descripción

CoffEmail es una aplicación de escritorio construida con Java Swing que permite registrar usuarios para un sistema de correo electrónico. La aplicación gestiona el registro completo de usuarios incluyendo encriptación de contraseñas, carga de fotografías de perfil y persistencia de datos en archivos de texto.

## Características

- **Registro de Usuarios** — Formulario completo con validación de todos los campos obligatorios
- **Encriptación de Contraseñas** — Hashing MD5 con salt aleatorio generado mediante `SHA1PRNG` y `SecureRandom`
- **Gestión de Imágenes de Perfil** — Selector de archivos para cargar fotos, que se copian y renombran automáticamente al directorio del sistema
- **Selector de Fechas** — Componente `DateChooser` integrado para selección de fecha de nacimiento
- **Roles de Usuario** — El primer usuario registrado se asigna automáticamente como administrador
- **Persistencia en Archivos** — Almacenamiento en texto plano con formato delimitado por `|`

## Arquitectura

El proyecto sigue el patrón **Modelo-Vista-Controlador (MVC)**:

| Capa | Clase | Responsabilidad |
|------|-------|-----------------|
| **Modelo** | `User.java` | Entidad de datos con 10 atributos (usuario, nombre, apellido, contraseña, rol, fecha de nacimiento, email, teléfono, foto, estado) |
| **Vista** | `RegisterView.java` | Interfaz gráfica Swing con formulario de registro, validación de campos y diálogos de confirmación |
| **Controlador** | `UserController.java` | Lógica de negocio: encriptación, copia de imágenes y escritura de archivos |

## Requisitos

- Java Development Kit (JDK) 1.8 o superior
- Apache Maven
- Sistema operativo: Windows (rutas de almacenamiento configuradas para `C:/MEIA/`)

## Instalación

1. Clona este repositorio:
```bash
git clone https://github.com/Esaban17/CoffEmail.git
cd CoffEmail
```

2. Compila el proyecto con Maven:
```bash
mvn clean install
```

## Uso

Ejecuta la aplicación con:

```bash
mvn exec:java -Dexec.mainClass="com.coffemail.views.RegisterView"
```

O mediante el JAR generado:

```bash
java -jar target/CoffEmail-1.0-SNAPSHOT.jar
```

### Flujo de registro

1. Completa todos los campos del formulario (usuario, nombre, apellido, contraseña, email, teléfono, fecha de nacimiento)
2. Selecciona una fotografía de perfil con el botón **Seleccionar**
3. Haz clic en **Registrar** para guardar el usuario
4. La contraseña se encripta automáticamente y la foto se copia al directorio del sistema

## Estructura del Proyecto

```
CoffEmail/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── coffemail/
│                   ├── controllers/
│                   │   └── UserController.java   # Encriptación, I/O de archivos
│                   ├── models/
│                   │   └── User.java             # Entidad de usuario
│                   └── views/
│                       └── RegisterView.java     # Interfaz gráfica (punto de entrada)
├── lib/                                          # Dependencia DateChooser (JAR local)
├── pom.xml                                       # Configuración de Maven
└── README.md
```

## Almacenamiento de Datos

| Recurso | Ruta |
|---------|------|
| Datos de usuarios | `C:/MEIA/usuario.txt` |
| Imágenes de perfil | `C:/MEIA/images/` |

- El archivo de usuarios se crea automáticamente con encabezados en el primer registro
- Cada registro se almacena en una línea con campos separados por `|`
- Las fotos se renombran al nombre de usuario conservando la extensión original

## Tecnologías

- **Java 1.8** — Lenguaje principal
- **Maven** — Gestión de dependencias y build
- **Swing** — Framework de interfaz gráfica
- **DateChooser** — Componente de selección de fechas (biblioteca local en `lib/`)

## Autor

**Estuardo Sabán** — [@Esaban17](https://github.com/Esaban17)

## Contribuciones

1. Haz fork del proyecto
2. Crea una rama (`git checkout -b feature/nueva-caracteristica`)
3. Haz commit de tus cambios (`git commit -m 'Agrega nueva característica'`)
4. Push a tu rama (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## Licencia

Este proyecto está en desarrollo. Contacta al autor para información sobre licencias.
