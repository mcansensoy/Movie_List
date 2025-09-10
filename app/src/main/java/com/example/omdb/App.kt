package com.example.omdb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Hilt'in Application yaşam döngüsünü başlatması için gerekli anotasyon.
// Projenin AndroidManifest.xml içinde <application android:name=".App" ... /> olarak tanımlı olmalı.
// (Eğer manifest'te uygulama adı yoksa Hilt tarafından otomatik bulunmayabilir; ekleyin.)
@HiltAndroidApp
class App : Application()