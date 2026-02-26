# Atlas UI (prototipo)

## Estructura

```
app/src/main/java/daw/developer/atlas/
├─ MainActivity.kt
├─ ui/
│  ├─ Footer.kt
│  ├─ Create.kt
│  ├─ dashboard/
│  │  ├─ DashboardScreen.kt
│  │  └─ components/
│  │     ├─ ActionButtonsRow.kt
│  │     ├─ DashboardTopBar.kt
│  │     ├─ EmptyStateCard.kt
│  │     └─ JoinTripDialog.kt
│  └─ profile/
│     ├─ ProfileScreen.kt
│     └─ components/
│        ├─ ProfileHeader.kt
│        ├─ ProfileStatsRow.kt
│        ├─ VisitedMapPlaceholder.kt
│        ├─ LibraryTabsRow.kt
│        └─ PhotoGridPlaceholder.kt
```

## Pantallas

- `MainActivity.kt`: Navegacion simple por estado (dashboard, create, profile) y modal de unirme.
- `ui/dashboard/DashboardScreen.kt`: Dashboard principal con top bar, acciones y estado vacio.
- `ui/Create.kt`: Formulario visual para crear viaje (solo UI).
- `ui/profile/ProfileScreen.kt`: Perfil con header, stats, mapa demo y biblioteca.

## Componentes del dashboard

- `DashboardTopBar.kt`: Marca y saludo del usuario.
- `ActionButtonsRow.kt`: Botones Unirme y Nuevo viaje.
- `EmptyStateCard.kt`: Estado vacio con borde punteado.
- `JoinTripDialog.kt`: Modal para codigo de invitacion.

## Componentes de perfil

- `ProfileHeader.kt`: Avatar, nombre, bio y contadores superiores.
- `ProfileStatsRow.kt`: Cards de paises, ciudades, viajes y fotos.
- `VisitedMapPlaceholder.kt`: Mapa demo estilo "been" con continentes simplificados.
- `LibraryTabsRow.kt`: Tabs Grid / Mapa / Viajes.
- `PhotoGridPlaceholder.kt`: Grid demo de fotos.

## Navegacion (prototipo)

- Se usa estado local en `MainActivity.kt` para cambiar de pantalla.
- `Footer.kt` aparece en todas las vistas y dispara callbacks.

## Estilo

- Paleta clara basada en `Color(0xFFF7F3EF)` y acento `Color(0xFFD97942)`.
- Componentes reutilizables en carpetas `components` para orden y claridad.

