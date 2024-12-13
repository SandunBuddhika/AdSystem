package com.sandun.adSystem.model.adsModel;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.sandun.adSystem.model.AdMethodType;
import com.sandun.adSystem.model.AdType;
import com.sandun.adSystem.model.AdsMediator;
import com.sandun.adSystem.model.ErrorHandler;
import com.sandun.adSystem.model.exceptions.FailedToLoadAdException;
import com.sandun.adSystem.model.handler.AdRequestHandler;

import java.util.Map;

public class OpenAd extends AdsCompact {
    public OpenAd(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds) {
        super(adsMediator, adMethodType, preLoadedAds);
        adType = AdType.OPEN;
    }

    @Override
    public void showAds(AdRequestHandler handler, ErrorHandler errorHandler) throws FailedToLoadAdException {
        if (adMethodType == AdMethodType.ADMOB) {
            showAdMob(handler);
        } else {
            showMeta(handler);
        }
    }

    @Override
    public void showAdMob(AdRequestHandler handler) throws FailedToLoadAdException {
        Object ad = preLoadedAds.get(adMethodType);
        System.out.println("Showing pre loaded app open");
        if (ad instanceof AppOpenAd) {
            AppOpenAd appOpenAd = (AppOpenAd) ad;
            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    handler.onSuccess();
                    adsMediator.clearPreLoadedAd(adType);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    System.out.println("Failed to show Open ad");
                    errorHandler.onFailed();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });
            appOpenAd.show(adsMediator.activity);
        } else {
            AppOpenAd.load(
                    adsMediator.activity, adsMediator.initializer.getGoogleIds().getAppOpenId(), adRequest,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    handler.onSuccess();
                                    adsMediator.clearPreLoadedAd(adType);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    System.out.println("Failed to show Open ad");
                                    errorHandler.onFailed();
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }
                            });
                            ad.show(adsMediator.activity);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            System.out.println("Failed to load Open ad");
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
                            adsMediator.clearPreLoadedAd(adType);
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
