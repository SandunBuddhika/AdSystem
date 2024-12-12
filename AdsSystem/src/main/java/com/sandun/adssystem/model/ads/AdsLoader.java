package com.sandun.adssystem.model.ads;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AdSettings;
import com.sandun.adssystem.model.AdsInitializer;

public class AdsLoader {
    private boolean isAdsDisable;
    private boolean isStartUpEnable;
    private AdType startUpType = AdType.ADMOB;
    private AdsErrorHandler adsErrorHandler;
    private static AdsLoader adsLoader;
    public AppCompatActivity activity;
    public AdsInitializer initializer;
    private AdType adType = AdType.ADMOB;

    private AdsLoader() {
        adsErrorHandler = new AdsErrorHandler(this);
    }

    //enable and disable facebook test ads
    public AdsLoader setFacebookTestAds(boolean isFacebookTestAds) {
        AdSettings.setTestMode(isFacebookTestAds);
        return this;
    }

    public static AdsLoader getInstance(AppCompatActivity activity, AdsInitializer initializer) {
        init(activity);
        adsLoader.initializer = initializer;
        return adsLoader;
    }

    public static AdsLoader getInstance(AppCompatActivity activity, AdsInitializer initializer, AdType adType) {
        init(activity);
        adsLoader.initializer = initializer;
        adsLoader.adType = adType;
        return adsLoader;
    }

    public boolean isAdsDisable() {
        return isAdsDisable;
    }

    public AdsLoader setAdsDisable(boolean adsDisable) {
        isAdsDisable = adsDisable;
        return this;
    }

    public static void init(AppCompatActivity activity) {
        if (adsLoader == null) {
            adsLoader = new AdsLoader();
        }
        adsLoader.activity = activity;
    }

    public boolean isStartUpEnable() {
        return isStartUpEnable;
    }

    public AdsLoader setStartUpEnable(boolean startUpEnable) {
        isStartUpEnable = startUpEnable;
        return this;
    }

    public AdsLoader setStartUpType(AdType startUpType) {
        this.startUpType = startUpType;
        return this;
    }

    private void startUp() {
        if (isStartUpEnable) {
            adsLoader.adType = startUpType;
        }
    }

    public void showUpInterstitialAd(AdDismissCallback callback) {
        if (!isAdsDisable) {
            startUp();
            adsErrorHandler.process(new AdRequestHandler(new AdHandler() {
                @Override
                public void process(AdsCompact adsCompact, AdRequestHandler handler) {
                    adsCompact.loadInterstitialAd(callback, handler);
                }
            }, adType));
        } else {
            callback.onAdDismissed();
        }
    }

    public void showUpBannerAd(View view) {
        if (!isAdsDisable) {
            startUp();
            adsErrorHandler.process(new AdRequestHandler(new AdHandler() {
                @Override
                public void process(AdsCompact adsCompact, AdRequestHandler handler) {
                    adsCompact.loadBannerAd(view);
                }
            }, adType));
        }
    }

    public void showUpNativeAd(View view, AdViewHandler viewHandler) {
        if (!isAdsDisable) {
            startUp();
            adsErrorHandler.process(new AdRequestHandler(new AdHandler() {
                @Override
                public void process(AdsCompact adsCompact, AdRequestHandler handler) {
                    adsCompact.loadNativeAd(handler, view, viewHandler);
                }
            }, adType));
        }
    }


    public void showUpOpenAppAd(AdDismissCallback callback) {
        if (!isAdsDisable) {
            startUp();
            adsErrorHandler.process(new AdRequestHandler(new AdHandler() {
                @Override
                public void process(AdsCompact adsCompact, AdRequestHandler handler) {
                    adsCompact.loadOpenAd(callback, handler);
                }
            }, adType));
        } else {
            callback.onAdDismissed();
        }
    }

    public void showUpRewardAd(AdDismissCallback callback) {
        if (!isAdsDisable) {
            startUp();
            adsErrorHandler.process(new AdRequestHandler(new AdHandler() {
                @Override
                public void process(AdsCompact adsCompact, AdRequestHandler handler) {
                    adsCompact.loadRewardAd(callback, handler);
                }
            }, adType));
        } else {
            callback.onAdDismissed();
        }
    }

    public AdsErrorHandler getAdsErrorHandler() {
        return adsErrorHandler;
    }

    public AdType getAdType() {
        return adType;
    }

    public void setAdType(AdType adType) {
        this.adType = adType;
    }
}
