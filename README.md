# МОИ ФИНАНСЫ — Android проект (Сборка APK)

Готовый проект для сборки установочного APK-файла.

## Способ 1: Сборка через Android Studio (на компьютере)
1. Распакуйте этот архив `MoiFinansy.zip`.
2. Откройте **Android Studio** (рекомендуется версия Hedgehog / Iguana / Koala / Ladybug).
3. Нажмите **File -> Open** и выберите распакованную папку **MoiFinansy**.
4. Дождитесь завершения Gradle Sync (потребуется интернет для первой загрузки).
5. В верхнем меню выберите: **Build -> Build Bundle(s) / APK(s) -> Build APK(s)**.
6. Готовый установочный файл появится по пути:
   `app/build/outputs/apk/debug/app-debug.apk`
7. Скопируйте `app-debug.apk` на телефон через USB, Telegram или облачный диск и нажмите для установки!

## Способ 2: Сборка через командную строку
Если у вас установлен JDK 17:
```bash
./gradlew assembleDebug
```
Файл APK будет в `app/build/outputs/apk/debug/app-debug.apk`.

## Способ 3: Автосборка через GitHub
Загрузите проект в репозиторий GitHub — настроенный `.github/workflows/build-apk.yml` сам соберет APK в облаке и даст ссылку на скачивание в разделе Actions / Artifacts!

---
Приложение разработано для учета доходов и расходов в сумах (UZS).
