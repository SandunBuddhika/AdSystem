package com.sandun.adSystem.model.adsModel;

import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sandun.adSystem.model.AdMethodType;
import com.sandun.adSystem.model.AdType;
import com.sandun.adSystem.model.AdsMediator;
import com.sandun.adSystem.model.ErrorHandler;
import com.sandun.adSystem.model.adsModel.ViewAdsCompact;
import com.sandun.adSystem.model.exceptions.FailedToLoadAdException;
import com.sandun.adSystem.model.handler.AdRequestHandler;

import java.util.Map;

public class BannerAd extends ViewAdsCompact {
    public BannerAd(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds, LinearLayout container) {
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
        AdView adView = new AdView(adsMediator.activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        adView.setLayoutParams(layoutParams);
        adView.setAdUnitId(adsMediator.initializer.getGoogleIds().getBannerId());
        adView.setAdSize(AdSize.BANNER);
        container.removeAllViews();
        container.addView(adView);
        adView.loadAd(adRequest);
    }

    @Override
    public void showMeta(AdRequestHandler handler) throws FailedToLoadAdException {
        com.facebook.ads.AdView bannerAdView = new com.facebook.ads.AdView(adsMediator.activity, adsMediator.initializer.getFacebookIds().getBannerId(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        container.removeAllViews();
        container.addView(bannerAdView);
        bannerAdView.loadAd();
    }
}
