# CoffEmail

Un cliente de correo electrónico simple desarrollado en Java con funcionalidades de gestión de usuarios.

## Descripción

CoffEmail es una aplicación de escritorio que permite gestionar usuarios para un sistema de correo electrónico. Incluye funcionalidades de registro con encriptación de contraseñas y gestión de perfiles de usuario.

## Características

- **Registro de Usuarios**: Sistema completo de registro con validación de datos
- **Encriptación de Contraseñas**: Utiliza MD5 con salt para asegurar las contraseñas de los usuarios
- **Gestión de Imágenes de Perfil**: Permite cargar y almacenar fotografías de perfil
- **Selector de Fechas**: Interfaz gráfica para seleccionar fechas de nacimiento
- **Almacenamiento de Datos**: Sistema de archivos para persistencia de información de usuarios

## Requisitos

- Java Development Kit (JDK) 1.8 o superior
- Apache Maven
- Sistema operativo: Windows, Linux o macOS

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

Para ejecutar la aplicación, utiliza el siguiente comando:

```bash
mvn exec:java -Dexec.mainClass="com.coffemail.views.RegisterView"
```

Alternativamente, puedes ejecutar el archivo JAR generado:

```bash
java -jar target/CoffEmail-1.0-SNAPSHOT.jar
```

## Estructura del Proyecto

```
CoffEmail/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── coffemail/
│                   ├── controllers/    # Controladores de lógica de negocio
│                   ├── models/         # Modelos de datos
│                   └── views/          # Interfaces gráficas
├── lib/                                # Bibliotecas externas
├── pom.xml                             # Configuración de Maven
└── README.md                           # Este archivo
```

## Funcionalidades de Usuario

El modelo de usuario incluye los siguientes campos:

- Usuario (nombre de usuario)
- Nombre
- Apellido
- Contraseña (encriptada)
- Rol (permisos de usuario)
- Fecha de nacimiento
- Correo electrónico
- Teléfono
- Fotografía de perfil
- Estado (activo/inactivo)

## Almacenamiento de Datos

Los datos de usuarios se almacenan en archivos de texto plano en la ruta:
- **Windows**: `C:/MEIA/usuario.txt`
- **Imágenes**: `C:/MEIA/images/`

El formato de almacenamiento utiliza el delimitador `|` para separar los campos.

## Tecnologías Utilizadas

- **Java 1.8**: Lenguaje de programación principal
- **Maven**: Gestión de dependencias y construcción del proyecto
- **Swing**: Framework para la interfaz gráfica de usuario
- **DateChooser**: Componente para selección de fechas

## Seguridad

El proyecto implementa:
- Encriptación de contraseñas usando MD5 con salt generado mediante `SecureRandom`
- Validación de datos de entrada
- Gestión segura de archivos

## Autor

**Estuardo Sabán** - [@Esaban17](https://github.com/Esaban17)

## Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Haz fork del proyecto
2. Crea una rama para tu característica (`git checkout -b feature/nueva-caracteristica`)
3. Realiza commit de tus cambios (`git commit -m 'Agrega nueva característica'`)
4. Sube los cambios a tu rama (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## Licencia

Este proyecto está en desarrollo. Por favor contacta al autor para información sobre licencias.

## Notas de Desarrollo

Este proyecto utiliza NetBeans como IDE de desarrollo y contiene configuraciones específicas para dicho entorno.

---

**Nota**: Este es un proyecto en desarrollo. Algunas funcionalidades pueden estar en proceso de implementación.
