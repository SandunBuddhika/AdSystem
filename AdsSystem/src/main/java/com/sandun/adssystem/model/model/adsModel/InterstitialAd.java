package com.sandun.adssystem.model.model.adsModel;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdType;
import com.sandun.adssystem.model.model.AdsMediator;
import com.sandun.adssystem.model.model.ErrorHandler;
import com.sandun.adssystem.model.model.exceptions.FailedToLoadAdException;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;

import java.util.Map;

public class InterstitialAd extends AdsCompact {


    public InterstitialAd(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds) {
        super(adsMediator, adMethodType, preLoadedAds);
        adType = AdType.INTERSTITIAL;
    }

    @Override
    public void showAds(AdRequestHandler handler, ErrorHandler errorHandler) throws FailedToLoadAdException {
        this.errorHandler = errorHandler;
        if (adMethodType == AdMethodType.ADMOB) {
            showAdMob(handler);
        } else {
            showMeta(handler);
        }
    }

    @Override
    public void showAdMob(AdRequestHandler handler) throws FailedToLoadAdException {
        Object ad = preLoadedAds.get(adMethodType);
        if (ad instanceof com.google.android.gms.ads.interstitial.InterstitialAd) {
            com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd = ((com.google.android.gms.ads.interstitial.InterstitialAd) preLoadedAds.get(adMethodType));
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    System.out.println("Ad Dismiss");
                    handler.onSuccess();
                    adsMediator.clearPreLoadedAd(AdType.INTERSTITIAL);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    System.out.println(adError.getMessage());
                    errorHandler.onFailed();
                }
            });
            interstitialAd.show(adsMediator.activity);
        } else {
            com.google.android.gms.ads.interstitial.InterstitialAd.load(adsMediator.activity, adsMediator.initializer.getGoogleIds().getInitId(), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    System.out.println("Ad Dismiss");
                                    handler.onSuccess();
                                    adsMediator.clearPreLoadedAd(AdType.INTERSTITIAL);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    System.out.println(adError.getMessage());
                                    errorHandler.onFailed();
                                }
                            });
                            interstitialAd.show(adsMediator.activity);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            System.out.println(loadAdError.getMessage());
                            errorHandler.onFailed();
                        }
                    });
        }
    }

    @Override
    public void showMeta(AdRequestHandler handler) throws FailedToLoadAdException {
        Object ad = preLoadedAds.get(adMethodType);
        if (ad instanceof com.facebook.ads.InterstitialAd && ((com.facebook.ads.InterstitialAd) ad).isAdLoaded()) {
            com.facebook.ads.InterstitialAd interstitialAd = ((com.facebook.ads.InterstitialAd) ad);
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig()
                    .withAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                            System.out.println("onInterstitialDisplayed");
                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            System.out.println("onInterstitialDismissed");
                            handler.onSuccess();
                            adsMediator.clearPreLoadedAd(AdType.INTERSTITIAL);
                        }

                        @Override
                        public void onError(Ad ad, com.facebook.ads.AdError adError) {
                            System.out.println(adError.getErrorMessage());
                            errorHandler.onFailed();
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            System.out.println("hi");
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    })
                    .build());
            interstitialAd.show();
        } else {
            com.facebook.ads.InterstitialAd mInterstitialAd = new com.facebook.ads.InterstitialAd(adsMediator.activity, adsMediator.initializer.getFacebookIds().getInitId());
            InterstitialAdListener adListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    System.out.println("onInterstitialDisplayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    System.out.println("onInterstitialDismissed");
                    handler.onSuccess();
                    adsMediator.clearPreLoadedAd(AdType.INTERSTITIAL);
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    System.out.println(adError.getErrorMessage());
                    errorHandler.onFailed();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    System.out.println("onAdLoaded");
                    mInterstitialAd.show();
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
    }

}
