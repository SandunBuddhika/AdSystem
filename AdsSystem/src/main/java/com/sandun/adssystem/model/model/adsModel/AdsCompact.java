package com.sandun.adssystem.model.model.adsModel;


import com.google.android.gms.ads.AdRequest;
import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdType;
import com.sandun.adssystem.model.model.AdsMediator;
import com.sandun.adssystem.model.model.ErrorHandler;
import com.sandun.adssystem.model.model.exceptions.FailedToLoadAdException;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;

import java.util.Map;

public abstract class AdsCompact {
    protected static final String TAG = AdsCompact.class.getName();
    protected AdType adType;
    protected AdsMediator adsMediator;
    protected AdRequest adRequest;
    protected AdMethodType adMethodType;
    protected ErrorHandler errorHandler;
    protected Map<AdMethodType, Object> preLoadedAds;

    public AdsCompact(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds) {
        this.adsMediator = adsMediator;
        this.adMethodType = adMethodType;
        this.adRequest = new AdRequest.Builder().build();
        this.preLoadedAds = preLoadedAds;
    }

    public abstract void showAds(AdRequestHandler handler, ErrorHandler errorHandler) throws FailedToLoadAdException;

    public abstract void showAdMob(AdRequestHandler handler) throws FailedToLoadAdException;

    public abstract void showMeta(AdRequestHandler handler) throws FailedToLoadAdException;

    public void changeType() {
        adMethodType = AdMethodType.ADMOB == adMethodType ? AdMethodType.META : AdMethodType.ADMOB;
    }

    public AdType getAdType() {
        return adType;
    }

    public AdMethodType getAdMethodType() {
        return adMethodType;
    }
}
