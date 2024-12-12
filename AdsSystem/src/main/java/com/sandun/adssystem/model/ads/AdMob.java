package com.sandun.adssystem.model.ads;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.sandun.adssystem.R;
import com.sandun.adssystem.model.ads.model.NativeTemplateStyle;
import com.sandun.adssystem.model.ads.model.TemplateView;

public class AdMob extends AdsCompact {
    private static final String TAG = AdMob.class.getName();
    private AdRequest adRequest;

    public AdMob(AdsLoader adsLoader) {
        super(adsLoader);
        adRequest = new AdRequest.Builder().build();
    }

    @Override
    public void loadInterstitialAd(AdDismissCallback callback, AdRequestHandler handler) {
        InterstitialAd.load(adsLoader.activity, adsLoader.initializer.getGoogleIds().getInitId(), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                System.out.println("Loaded...");
                                callback.onAdDismissed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                if (handler.getTurns() == 1) {
                                    callback.onAdFailed();
                                }
                                adsLoader.getAdsErrorHandler().onFailed(handler);
                            }
                        });
                        interstitialAd.show(adsLoader.activity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        System.out.println(loadAdError);
                        System.out.println(loadAdError.getMessage());
                        if (handler.getTurns() == 1) {
                            callback.onAdFailed();
                        }
                        adsLoader.getAdsErrorHandler().onFailed(handler);
                    }
                });
    }


    @Override
    public void loadBannerAd(View view) {
        LinearLayout container = (LinearLayout) view;
        AdView adView = new AdView(adsLoader.activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        adView.setLayoutParams(layoutParams);
        adView.setAdUnitId(adsLoader.initializer.getGoogleIds().getBannerId());
        adView.setAdSize(AdSize.BANNER);
        container.removeAllViews();
        container.addView(adView);
        adView.loadAd(adRequest);
    }

    @Override
    public void loadNativeAd(AdRequestHandler handler, View view, AdViewHandler viewHandler) {
        try {
            LinearLayout nativeAdContainer = (LinearLayout) view;
            LinearLayout layout = (LinearLayout) LayoutInflater.from(adsLoader.activity).inflate(R.layout.small_native_ad_layout, null, false);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(layout);
            TemplateView nativeAdView = layout.findViewById(R.id.my_template);
            AdLoader adLoader = new AdLoader.Builder(adsLoader.activity, adsLoader.initializer.getGoogleIds().getNativeId())
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        nativeAdView.setVisibility(View.VISIBLE);
                        nativeAdView.setStyles(styles);
                        nativeAdView.setNativeAd(nativeAd);
                        if (viewHandler != null) {
                            viewHandler.handler(layout);
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            adsLoader.getAdsErrorHandler().onFailed(handler);
                        }
                    })
                    .build();
            adLoader.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadOpenAd(AdDismissCallback callback, AdRequestHandler handler) {
        MobileAds.initialize(adsLoader.activity, initializationStatus -> {
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    adsLoader.activity, adsLoader.initializer.getGoogleIds().getAppOpenId(), request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            System.out.println("Loaded...");
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    callback.onAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    if (handler.getTurns() == 1) {
                                        callback.onAdFailed();
                                    }
                                    adsLoader.getAdsErrorHandler().onFailed(handler);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }
                            });
                            ad.show(adsLoader.activity);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            if (handler.getTurns() == 1) {
                                callback.onAdFailed();
                            }
                            adsLoader.getAdsErrorHandler().onFailed(handler);
                        }
                    });
        });
    }


    @Override
    public void loadRewardAd(AdDismissCallback callback, AdRequestHandler handler) {
        RewardedAd.load(adsLoader.activity, adsLoader.initializer.getGoogleIds().getRewardId(),
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
                                if (handler.getTurns() == 1) {
                                    callback.onAdFailed();
                                }
                                adsLoader.getAdsErrorHandler().onFailed(handler);
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
                        ad.show(adsLoader.activity, rewardItem -> callback.onAdDismissed());
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (handler.getTurns() == 1) {
                            callback.onAdFailed();
                        }
                        adsLoader.getAdsErrorHandler().onFailed(handler);
                    }
                });
    }
}
