# Users CLI con DDD y arquitectura hexagonal

Ejemplo pedagógico simple para comprender las bases de Domain-Driven Design (DDD) y arquitectura hexagonal en Java, sin depender de frameworks avanzados ni contenedores de inyección de dependencias.

## Objetivo

El proyecto muestra cómo estructurar y organizar el código separando las reglas de negocio, los casos de uso, los puertos y los adaptadores.

La intención no es construir una aplicación lista para producción. El objetivo es facilitar el aprendizaje de los conceptos fundamentales antes de incorporar herramientas como Spring, bases de datos, proveedores de caché, ORM o mecanismos automáticos de inyección de dependencias.

## Casos de uso

La aplicación permite gestionar usuarios desde una interfaz de línea de comandos:

1. Registrar un usuario.
2. Buscar un usuario por ID.
3. Listar los usuarios registrados.

### Clientes de la agencia de castings (CRUDL)

Como extensión del caso de estudio *Agencia de Castings*, se agrega la entidad `Cliente`. Según el caso, cada
cliente se identifica por un código único y tiene nombre, dirección, teléfono, persona de contacto y tipo de
actividad (moda, o publicidad y cine).

| Operación | Caso de uso | Puerto de entrada | Puerto de salida |
|---|---|---|---|
| **C**reate | Registrar cliente | `AgregarClienteUseCase` | `GuardarClientePort` |
| **R**ead | Buscar cliente por código | `ObtenerClienteUseCase` | `ObtenerClientesPort` |
| **U**pdate | Actualizar cliente | `ActualizarClienteUseCase` | `ActualizarClientePort` |
| **D**elete | Eliminar cliente | `EliminarClienteUseCase` | `EliminarClientePort` |
| **L**ist | Listar clientes | `ObtenerClienteUseCase` | `ObtenerClientesPort` |

Los datos se almacenan en memoria durante la ejecución de la aplicación.

## Arquitectura

El proyecto aplica arquitectura hexagonal para mantener las reglas de negocio independientes de la interfaz de usuario y del mecanismo de persistencia.

```text
src/main/java/com/jcaa/udec/collections
├── domain
│   ├── core
│   │   ├── exception
│   │   ├── model
│   │   └── valueobject
│   └── port
│       └── out
├── application
│   └── service
├── adapter
│   └── persistence
│       └── memory
└── entrypoint
    ├── cli
    └── controller
```

### Dominio

Contiene el modelo `Usuario`, las excepciones y los Value Objects responsables de proteger las invariantes:

- `UsuarioId`
- `NombreUsuario`
- `Password`
- `Email`

También contiene el modelo `Cliente`, sus excepciones (`ClienteInvalidoException`, `ClienteNoExisteException`,
`ClienteYaExisteException`) y sus Value Objects:

| Value Object | Regla |
|---|---|
| `CodigoCliente` | entre 3 y 10 letras o dígitos |
| `NombreCliente` | mínimo 3 caracteres |
| `Direccion` | mínimo 5 caracteres |
| `Telefono` | solo dígitos, entre 7 y 10 |
| `PersonaContacto` | mínimo 3 caracteres |
| `TipoActividad` | `MODA` o `PUBLICIDAD_CINE` |

### Aplicación

Contiene los servicios que coordinan los casos de uso y se comunican con el exterior mediante puertos.

### Adaptadores

Implementan persistencia en memoria para guardar y consultar usuarios. No son mocks: son implementaciones concretas no durables.

Para clientes, `ClientesMemoria` guarda la lista compartida y cada operación tiene su adaptador:
`GuardarClienteAdapter`, `ObtenerClientesAdapter`, `ActualizarClienteAdapter` y `EliminarClienteAdapter`.

### Entrypoints

Incluyen la interfaz CLI y el controlador que transforma las entradas del usuario en comandos de aplicación.

`ClienteControlador` traduce las peticiones de la CLI a comandos y consultas, y devuelve `ClienteResponse` para que la
entidad `Cliente` no salga de la aplicación. `ClienteCli` implementa el submenú de clientes.

### Flujo de una operación (ejemplo: registrar cliente)

```text
ClienteCli ──► ClienteControlador ──► AgregarClienteUseCase ──► AgregarClienteService
 (entrada)      (petición→comando)     (puerto de entrada)       │ ClienteMapper: comando→Cliente (valida VO)
                                                                 ▼
                      ClientesMemoria ◄── GuardarClienteAdapter ◄── GuardarClientePort
                      (memoria)           (adaptador de salida)     (puerto de salida)
```

## Decisiones pedagógicas

- Las dependencias se ensamblan manualmente en `Main`.
- No se utiliza Spring ni un contenedor de inyección de dependencias.
- No se utiliza una base de datos.
- No se utiliza ORM.
- No se utiliza caché.
- La persistencia en memoria permite observar con claridad el flujo entre capas.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.

## Ejecutar pruebas

```bash
mvn clean install
```

## Revisar cobertura

El build genera el reporte JaCoCo en:

```text
target/site/jacoco/index.html
```

Para revisar la cobertura diferencial respecto a `main`:

```bash
diff-cover target/site/jacoco/jacoco.xml --compare-branch=main
```
Debes instalar **diff-cover** que es una utilidad desarrollando sobre Python.

## Ejecutar la aplicación

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.jcaa.udec.Main
```

### Menú de la CLI

```text
Menú principal                      Gestión de clientes (opción 4)
1 - Agregar                         1 - Registrar cliente
2 - Buscar por Id                   2 - Buscar cliente por codigo
3 - Ver todos                       3 - Listar clientes
4 - Gestionar clientes              4 - Actualizar cliente
5 - Salir                           5 - Eliminar cliente
                                    6 - Volver al menu principal
```
