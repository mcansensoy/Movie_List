package com.example.omdb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Hilt'in Application yaşam döngüsünü başlatması için gerekli anotasyon
@HiltAndroidApp
class App : Application()