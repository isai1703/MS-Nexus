# MS-Nexus

Proyecto Android nativo en Kotlin + Jetpack Compose + Material Design 3.
Pantalla única de bienvenida con "MS Nexus" / "Powered by Multiservicios".

## Stack técnico

- Kotlin 2.0.20
- Jetpack Compose (BOM 2024.09.03) + Material 3
- Gradle Kotlin DSL (`build.gradle.kts`)
- AGP 8.6.0, Gradle 8.9
- `minSdk` 26, `targetSdk`/`compileSdk` 35

## ⚠️ Nota importante sobre `gradle-wrapper.jar`

Este proyecto incluye `gradlew`, `gradlew.bat` y `gradle/wrapper/gradle-wrapper.properties`,
pero **no incluye el binario `gradle/wrapper/gradle-wrapper.jar`**, porque se generó en un
entorno sin acceso a red y ese archivo es un `.jar` binario que no se puede escribir a mano
de forma fiable.

Antes de compilar localmente, genera el jar una sola vez (con Gradle instalado, por ejemplo
vía [SDKMAN](https://sdkman.io) o Android Studio):

```bash
gradle wrapper --gradle-version 8.9 --distribution-type bin
```

Esto crea `gradle/wrapper/gradle-wrapper.jar` y regenera `gradlew`/`gradlew.bat` de forma
oficial. A partir de ahí, `./gradlew build` funcionará normalmente y puedes (y deberías)
commitear el jar al repositorio.

Si abres el proyecto directamente en **Android Studio**, este detecta el wrapper incompleto
y ofrece regenerarlo automáticamente al sincronizar.

En **GitHub Actions** no hace falta hacer nada: el workflow `.github/workflows/android-ci.yml`
regenera el wrapper automáticamente antes de compilar.

## Compilar localmente

```bash
./gradlew assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Estructura

```
MS-Nexus/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/multiservicios/msnexus/
│       │   ├── MainActivity.kt
│       │   └── ui/theme/ (Color.kt, Theme.kt, Type.kt)
│       └── res/
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/gradle-wrapper.properties
├── gradlew / gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
└── .github/workflows/android-ci.yml
```
