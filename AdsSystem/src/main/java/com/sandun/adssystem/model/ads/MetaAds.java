package com.sandun.adssystem.model.ads;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.sandun.adssystem.R;

import java.util.ArrayList;
import java.util.List;

public class MetaAds extends AdsCompact {


    public MetaAds(AdsLoader adsLoader) {
        super(adsLoader);
    }

    @Override
    public void loadInterstitialAd(AdDismissCallback callback, AdRequestHandler handler) {
        InterstitialAd mInterstitialAd = new InterstitialAd(adsLoader.activity, adsLoader.initializer.getFacebookIds().getInitId());
        InterstitialAdListener adListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                System.out.println("onInterstitialDisplayed");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                System.out.println("onInterstitialDismissed");
                callback.onAdDismissed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
//                callback.onAdFailed(adsLoader.getAdsErrorHandler());
                if (handler.getTurns() == 1) {
                    callback.onAdFailed();
                }
                adsLoader.getAdsErrorHandler().onFailed(handler);
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

    @Override
    public void loadNativeAd(AdRequestHandler handler, View view, AdViewHandler viewHandler) {
        NativeAd nativeAd = new NativeAd(adsLoader.activity, adsLoader.initializer.getFacebookIds().getNativeId());
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                System.out.println("Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                System.out.println("Native ad failed to load: " + adError.getErrorMessage());
                adsLoader.getAdsErrorHandler().onFailed(handler);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                System.out.println("Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                inflateAd(nativeAd, (LinearLayout) view, viewHandler);
            }

            @Override
            public void onAdClicked(Ad ad) {
                System.out.println("Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                System.out.println("Native ad impression logged!");
            }
        };
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    @Override
    public void loadOpenAd(AdDismissCallback callback, AdRequestHandler handler) {
        loadInterstitialAd(callback, handler);
    }

    @Override
    public void loadBannerAd(View view) {
        AdView bannerAdView = new AdView(adsLoader.activity, adsLoader.initializer.getFacebookIds().getBannerId(), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) view;
        adContainer.removeAllViews();
        adContainer.addView(bannerAdView);
        bannerAdView.loadAd();
    }

    @Override
    public void loadRewardAd(AdDismissCallback callback, AdRequestHandler handler) {
        loadInterstitialAd(callback, handler);
    }

    private void inflateAd(NativeAd nativeAd, LinearLayout container, AdViewHandler viewHandler) {

        try {
            nativeAd.unregisterView();
            NativeAdLayout nativeAdLayout = new NativeAdLayout(adsLoader.activity);
            LayoutInflater inflater = LayoutInflater.from(adsLoader.activity);
            LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, null, false);
            nativeAdLayout.addView(adView);
            if (viewHandler != null) {
                viewHandler.handler(nativeAdLayout);
            }
            container.removeAllViews();
            container.addView(nativeAdLayout);

            LinearLayout adChoicesContainer = adsLoader.activity.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(adsLoader.activity, nativeAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            nativeAd.registerViewForInteraction(
                    adView, nativeAdMedia, nativeAdIcon, clickableViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}