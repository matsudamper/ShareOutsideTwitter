package net.matsudamper.shareoutside.bluebird

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.firebase.FirebaseApp

public class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG.not()) {
            FirebaseApp.initializeApp(this)
        }
        MobileAds.initialize(
            this,
        ) { }
    }
}
