package com.sandun.adSystem.model;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AdSettings;
import com.sandun.adSystem.model.adsModel.InterstitialAd;
import com.sandun.adSystem.model.handler.AdRequestHandler;
import com.sandun.adSystem.AdsInitializer;
import com.sandun.adSystem.model.adsModel.BannerAd;
import com.sandun.adSystem.model.adsModel.NativeAd;
import com.sandun.adSystem.model.adsModel.OpenAd;
import com.sandun.adSystem.model.adsModel.RewardAd;
import com.sandun.adSystem.model.handler.ViewAdRequestHandler;

public class AdsMediator {
    private static AdsMediator adsMediator;
    public AdsInitializer initializer;
    public AppCompatActivity activity;
    private PreLoader preLoader;
    private AdMethodType adMethodType;
    private boolean isIgnoreAds;

    private AdsMediator() {
        preLoader = new PreLoader(this);
    }

    public static AdsMediator getInstance(AppCompatActivity activity, AdsInitializer initializer) {
        init(activity, initializer);
        AdSettings.setTestMode(true);
        return adsMediator;
    }

    private static void init(AppCompatActivity activity, AdsInitializer initializer) {
        if (adsMediator == null) {
            adsMediator = new AdsMediator();
//            AudienceNetworkAds.initialize(activity);
        }
        adsMediator.activity = activity;
        adsMediator.initializer = initializer;
    }

    public void setIgnoreAds(boolean ignoreAds) {
        isIgnoreAds = ignoreAds;
    }

    public void setAdMethodType(AdMethodType adMethodType) {
        this.adMethodType = adMethodType;
    }

    public Object getNewPreLoadedAd(AdMethodType methodType, AdType adType) {
        return null;
    }

    public void preLoadAds(AdType adType) {
        if (!isIgnoreAds) {
            switch (adType) {
                case INTERSTITIAL:
                    preLoader.preLoadInterstitialAds();
                    break;
                case REWARD:
                    preLoader.preLoadRewardAds();
                    break;
                case OPEN:
                    preLoader.preOpenAds();
                    break;
            }
        }
    }

    public void clearPreLoadedAd(AdType adType) {
        switch (adType) {
            case INTERSTITIAL:
                preLoader.clearInterstitialAd();
                break;
            case REWARD:
                preLoader.clearRewardAds();
                break;
            case OPEN:
                preLoader.clearOpenAds();
                break;
        }

    }

    public void showInterstitialAd(AdRequestHandler handler) {
        if (!isIgnoreAds) {
            InterstitialAd ad = new InterstitialAd(this, adMethodType, preLoader.getInterstitialAd());
            new ErrorHandler(ad, handler, this);
        } else {
            handler.onSuccess();
        }
    }

    public void showRewardAd(AdRequestHandler handler) {
        if (!isIgnoreAds) {
            RewardAd ad = new RewardAd(this, adMethodType, preLoader.getRewardAds());
            new ErrorHandler(ad, handler, this);
        } else {
            handler.onSuccess();
        }
    }

    public void showOpenAd(AdRequestHandler handler) {
        if (!isIgnoreAds) {
            OpenAd ad = new OpenAd(this, adMethodType, preLoader.getOpenAds());
            new ErrorHandler(ad, handler, this);
        } else {
            handler.onSuccess();
        }
    }

    public void showNativeAd(ViewAdRequestHandler handler, LinearLayout container) {
        if (!isIgnoreAds) {
            NativeAd ad = new NativeAd(this, adMethodType, preLoader.getOpenAds(), container);
            new ErrorHandler(ad, handler, this);
        } else {
            handler.onSuccess();
        }
    }

    public void showBannerAd(ViewAdRequestHandler handler, LinearLayout container) {
        if (!isIgnoreAds) {
            BannerAd ad = new BannerAd(this, adMethodType, preLoader.getOpenAds(), container);
            new ErrorHandler(ad, handler, this);
        } else {
            handler.onSuccess();
        }
    }

}
