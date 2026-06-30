# Datos de prueba — Video de Uso

> Usá estos datos para la demostración en vivo. Seguí el orden para que se vea el flujo completo del negocio.

---

## Paso 1: Crear usuarios

Ir a **Usuarios** → llenar formulario → **Registrar usuario**

| Nombre | Email | Teléfono | Dirección |
|---|---|---|---|
| María González | maria@email.com | +56911112222 | Av. Providencia 1234 |
| Carlos Muñoz | carlos@email.com | +56933334444 | Calle Merced 567 |

---

## Paso 2: Crear mascotas

Ir a **Mascotas** → **+ Nueva mascota** → llenar formulario → **Guardar mascota**

| Nombre | Especie | Raza | Color | Edad | ID Dueño |
|---|---|---|---|---|---|
| Firulais | Perro | Labrador | Negro | 3 | 1 |
| Luna | Gato | Siames | Blanco | 2 | 1 |
| Max | Perro | Pastor Alemán | Café | 5 | 2 |

---

## Paso 3: Crear reportes

Ir a **Reportes** → llenar formulario → **Crear reporte**

| Mascota ID | Ubicación | Tipo | Descripción |
|---|---|---|---|
| 1 | Parque Forestal | Pérdida | Labrador negro con collar rojo, se perdió el 15 de enero |
| 3 | Plaza de Armas | Pérdida | Pastor alemán café, responde al nombre Max |

---

## Paso 4: Demostrar flujo completo

1. Mostrar que **Firulais** y **Max** ahora aparecen con reporte activo (`Sí` en la tabla Mascotas)
2. Ir a Reportes → **Encontrada** en el reporte de Firulais (ID 1)
3. Mostrar que Firulais ahora dice `No` en reporte activo
4. Ir a Reportes → **Cerrar** el reporte de Max (ID 2)
5. Mostrar que el reporte ahora está en estado `Cerrado`

### Bonus: demostrar validaciones

- Intentar crear una mascota con **ID Dueño que no existe** (ej: 99) → debe mostrar error
- Intentar crear un reporte **sin ubicación** → debe mostrar error
- Intentar crear un usuario con **email duplicado** → debe mostrar error

---

## Paso 5: Eliminar datos (opcional)

Mostrar el botón **Eliminar** en cada tabla (Mascotas, Usuarios, Reportes).
