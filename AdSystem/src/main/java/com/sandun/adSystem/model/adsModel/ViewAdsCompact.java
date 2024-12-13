package com.sandun.adSystem.model.adsModel;

import android.widget.LinearLayout;

import com.sandun.adSystem.model.AdMethodType;
import com.sandun.adSystem.model.AdsMediator;

import java.util.Map;

public abstract class ViewAdsCompact extends AdsCompact {
    protected LinearLayout container;

    public ViewAdsCompact(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds) {
        super(adsMediator, adMethodType, preLoadedAds);
    }

    public ViewAdsCompact(AdsMediator adsMediator, AdMethodType adMethodType, Map<AdMethodType, Object> preLoadedAds, LinearLayout container) {
        super(adsMediator, adMethodType, preLoadedAds);
        this.container = container;
    }


}
