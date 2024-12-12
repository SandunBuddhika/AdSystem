package com.sandun.adssystem.model.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.HashMap;
import java.util.Map;

public class PreLoader {

    private final AdRequest adRequest;
    private final AdsMediator adsMediator;
    private final Map<AdMethodType, Object> interstitialAds;
    private final Map<AdMethodType, Object> rewardAds;
    private final Map<AdMethodType, Object> openAds;

    public PreLoader(AdsMediator adsMediator) {
        this.adsMediator = adsMediator;
        adRequest = new AdRequest.Builder().build();
        interstitialAds = new HashMap<>();
        rewardAds = new HashMap<>();
        openAds = new HashMap<>();
    }

    public void preLoadInterstitialAds() {
        com.facebook.ads.InterstitialAd mInterstitialAd = new com.facebook.ads.InterstitialAd(adsMediator.activity, adsMediator.initializer.getFacebookIds().getInitId());
        InterstitialAdListener adListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                System.out.println("showed2");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                System.out.println(AdMethodType.META + " Interstitial Ad pre loaded");
                interstitialAds.put(AdMethodType.META, mInterstitialAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        mInterstitialAd.loadAd(
                mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(adListener)
                        .build()
        );

        com.google.android.gms.ads.interstitial.InterstitialAd.load(adsMediator.activity, adsMediator.initializer.getGoogleIds().getInitId(), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        System.out.println(AdMethodType.ADMOB + " Interstitial Ad pre loaded");
                        interstitialAds.put(AdMethodType.ADMOB, interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println("Failed to pre load admob interstitial ad");
                    }
                });
    }

    public Map<AdMethodType, Object> getInterstitialAd() {
        return interstitialAds;
    }

    public void clearInterstitialAd() {
        try {
            interstitialAds.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preLoadRewardAds() {
        RewardedAd.load(adsMediator.activity, adsMediator.initializer.getGoogleIds().getRewardId(),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        System.out.println(AdMethodType.ADMOB + " Reward Ad pre loaded");
                        rewardAds.put(AdMethodType.ADMOB, ad);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println(loadAdError.getMessage());
                        System.out.println("Reward ad failed to pre load");
                    }
                });


        com.facebook.ads.InterstitialAd mInterstitialAd = new com.facebook.ads.InterstitialAd(adsMediator.activity, adsMediator.initializer.getFacebookIds().getInitId());
        InterstitialAdListener adListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                System.out.println("showed2");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                System.out.println(AdMethodType.META + " Interstitial Ad pre loaded (REWARD)");
                rewardAds.put(AdMethodType.META, mInterstitialAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        mInterstitialAd.loadAd(
                mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(adListener)
                        .build()
        );
    }

    public Map<AdMethodType, Object> getRewardAds() {
        return rewardAds;
    }

    public void clearRewardAds() {
        try {
            rewardAds.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preOpenAds() {
        AppOpenAd.load(
                adsMediator.activity, adsMediator.initializer.getGoogleIds().getAppOpenId(), adRequest,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        System.out.println("App Open Ad Pre loaded");
                        openAds.put(AdMethodType.ADMOB, ad);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        System.out.println("Failed to load Open ad");
                    }
                });


        com.facebook.ads.InterstitialAd mInterstitialAd = new com.facebook.ads.InterstitialAd(adsMediator.activity, adsMediator.initializer.getFacebookIds().getInitId());
        InterstitialAdListener adListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                System.out.println("showed2");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                System.out.println(AdMethodType.META + " Interstitial Ad pre loaded (OPEN AD)");
                openAds.put(AdMethodType.META, mInterstitialAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        mInterstitialAd.loadAd(
                mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(adListener)
                        .build()
        );
    }

    public Map<AdMethodType, Object> getOpenAds() {
        return openAds;
    }

    public void clearOpenAds() {
        try {
            openAds.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
