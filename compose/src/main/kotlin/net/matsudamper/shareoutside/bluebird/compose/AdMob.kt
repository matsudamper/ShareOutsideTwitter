package net.matsudamper.shareoutside.bluebird.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import net.matsudamper.shareoutside.bluebird.base.BuildConfig

@SuppressLint("MissingPermission", "VisibleForTests")
@Composable
internal fun AdMob(
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = {
            AdView(it).also { view ->
                view.adUnitId = BuildConfig.ADMOB_ID_SHARE
                view.adSize = AdSize.BANNER

                val adRequest = AdRequest.Builder().build()
                view.loadAd(adRequest)
            }
        },
    )
}
