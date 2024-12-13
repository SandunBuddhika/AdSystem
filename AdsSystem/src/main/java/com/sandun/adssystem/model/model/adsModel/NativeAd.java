package com.sandun.adssystem.model.model.adsModel;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.sandun.adssystem.R;
import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdType;
import com.sandun.adssystem.model.model.AdsMediator;
import com.sandun.adssystem.model.model.ErrorHandler;
import com.sandun.adssystem.model.model.exceptions.FailedToLoadAdException;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;
import com.sandun.adssystem.model.model.handler.ViewAdRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NativeAd extends ViewAdsCompact {

    public NativeAd(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds, LinearLayout container) {
        super(adsMediator, adMethodType, preLoadedAds, container);
        adType = AdType.NATIVE;
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
        try {
            ViewAdRequestHandler viewHandler = (ViewAdRequestHandler) handler;
            LinearLayout nativeAdContainer = (LinearLayout) container;
            LinearLayout layout = (LinearLayout) LayoutInflater.from(adsMediator.activity).inflate(R.layout.small_native_ad_layout, null, false);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(layout);
            TemplateView nativeAdView = layout.findViewById(R.id.my_template);
            AdLoader adLoader = new AdLoader.Builder(adsMediator.activity, adsMediator.initializer.getGoogleIds().getNativeId())
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        nativeAdView.setVisibility(View.VISIBLE);
                        nativeAdView.setStyles(styles);
                        nativeAdView.setNativeAd(nativeAd);
                        viewHandler.viewHandler(nativeAdView);
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            errorHandler.onFailed();
                        }
                    })
                    .build();
            adLoader.loadAd(adRequest);
        } catch (Exception e) {
            errorHandler.onFailed();
            e.printStackTrace();
        }
    }

    @Override
    public void showMeta(AdRequestHandler handler) throws FailedToLoadAdException {
        try {
            ViewAdRequestHandler viewHandler = (ViewAdRequestHandler) handler;
            NativeBannerAd nativeAd = new NativeBannerAd(adsMediator.activity, adsMediator.initializer.getFacebookIds().getNativeId());
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    System.out.println("Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    System.out.println("Native ad failed to load: " + adError.getErrorMessage());
                    errorHandler.onFailed();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    inflateAd(nativeAd, viewHandler, errorHandler);
                    viewHandler.onSuccess();
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
        } catch (Exception e) {
            errorHandler.onFailed();
            e.printStackTrace();
        }
    }

    private void inflateAd(NativeBannerAd nativeBannerAd, ViewAdRequestHandler handler, ErrorHandler errorHandler) {
        try {
            nativeBannerAd.unregisterView();

            NativeAdLayout nativeAdLayout = new NativeAdLayout(adsMediator.activity);

            LayoutInflater inflater = LayoutInflater.from(adsMediator.activity);
            LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.banner_native_ad_layout, null, false);
            nativeAdLayout.addView(adView);

            handler.viewHandler(nativeAdLayout);

            container.removeAllViews();
            container.addView(nativeAdLayout);

            RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(adsMediator.activity, nativeBannerAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
            nativeAdCallToAction.setVisibility(
                    nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
            nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
            sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
        } catch (Exception e) {
            errorHandler.onFailed();
            e.printStackTrace();
        }
    }
}
