package com.sandun.adssystem.model.model;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.AdSettings;
import com.sandun.adssystem.model.AdsInitializer;
import com.sandun.adssystem.model.model.adsModel.InterstitialAd;
import com.sandun.adssystem.model.model.adsModel.OpenAd;
import com.sandun.adssystem.model.model.adsModel.RewardAd;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;

public class AdsMediator {
    private static AdsMediator adsMediator;
    public AdsInitializer initializer;
    public AppCompatActivity activity;
    private PreLoader preLoader;
    private AdMethodType adMethodType;


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
        }
        adsMediator.activity = activity;
        adsMediator.initializer = initializer;
    }

    public void setAdMethodType(AdMethodType adMethodType) {
        this.adMethodType = adMethodType;
    }

    public Object getNewPreLoadedAd(AdMethodType methodType, AdType adType) {
        return null;
    }

    public void preLoadAds(AdType adType) {
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
        //i could pass the map object of preloaded ads
        InterstitialAd ad = new InterstitialAd(this, adMethodType, preLoader.getInterstitialAd());
        new ErrorHandler(ad, handler, this);
    }

    public void showRewardAd(AdRequestHandler handler) {
        RewardAd ad = new RewardAd(this, adMethodType, preLoader.getRewardAds());
        new ErrorHandler(ad, handler, this);
    }

    public void showOpenAd(AdRequestHandler handler) {
        OpenAd ad = new OpenAd(this, adMethodType, preLoader.getOpenAds());
        new ErrorHandler(ad, handler, this);
    }

}
