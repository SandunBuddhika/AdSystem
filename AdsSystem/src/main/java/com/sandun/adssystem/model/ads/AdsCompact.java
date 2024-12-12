package com.sandun.adssystem.model.ads;

import android.view.View;

public abstract class AdsCompact {
    protected AdsLoader adsLoader;

    public AdsCompact(AdsLoader adsLoader) {
        this.adsLoader = adsLoader;
    }

    public abstract void loadInterstitialAd(AdDismissCallback callback, AdRequestHandler handler);

    public abstract void loadNativeAd(AdRequestHandler handler, View view, AdViewHandler viewHandler);

    public abstract void loadOpenAd(AdDismissCallback callback, AdRequestHandler handler);

    public abstract void loadBannerAd(View view);

    public abstract void loadRewardAd(AdDismissCallback callback, AdRequestHandler handler);
}
