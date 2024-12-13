package com.sandun.adssystem.model.model.adsModel;

import android.view.View;
import android.widget.LinearLayout;

import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdsMediator;

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
