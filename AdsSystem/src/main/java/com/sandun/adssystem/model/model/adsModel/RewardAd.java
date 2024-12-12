package com.sandun.adssystem.model.model.adsModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdType;
import com.sandun.adssystem.model.model.AdsMediator;
import com.sandun.adssystem.model.model.ErrorHandler;
import com.sandun.adssystem.model.model.exceptions.FailedToLoadAdException;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;

import java.util.Map;

public class RewardAd extends AdsCompact {
    public RewardAd(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds) {
        super(adsMediator, adMethodType, preLoadedAds);
        adType = AdType.REWARD;
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
        if (ad instanceof RewardedAd) {
            RewardedAd rewardedAd = (RewardedAd) ad;
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    errorHandler.onFailed();
                    System.out.println(adError.getMessage());
                    System.out.println("Reward ad failed to show");
                }

                @Override
                public void onAdImpression() {
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });
            rewardedAd.show(adsMediator.activity, rewardItem -> {
                handler.onSuccess();
                adsMediator.clearPreLoadedAd(adType);
            });
        } else {
            RewardedAd.load(adsMediator.activity, adsMediator.initializer.getGoogleIds().getRewardId(),
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    Log.d(TAG, "Ad was clicked.");
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    Log.d(TAG, "Ad dismissed fullscreen content.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    errorHandler.onFailed();
                                    System.out.println(adError.getMessage());
                                    System.out.println("Reward ad failed to show");
                                }

                                @Override
                                public void onAdImpression() {
                                    Log.d(TAG, "Ad recorded an impression.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    Log.d(TAG, "Ad showed fullscreen content.");
                                }
                            });
                            ad.show(adsMediator.activity, rewardItem -> {
                                handler.onSuccess();
                                adsMediator.clearPreLoadedAd(AdType.INTERSTITIAL);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            errorHandler.onFailed();
                            System.out.println(loadAdError.getMessage());
                            System.out.println("Reward ad failed to load");
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
