package com.sandun.adssystem.model.ads;

public class AdsErrorHandler {
    private AdsLoader adsLoader;
    private AdMob adMob;
    private MetaAds metaAds;

    public AdsErrorHandler(AdsLoader adsLoader) {
        this.adsLoader = adsLoader;
        this.adMob = new AdMob(adsLoader);
        this.metaAds = new MetaAds(adsLoader);
    }

    public void onFailed(AdRequestHandler request) {
        System.out.println("request.getTurns(): " +request.getTurns());
        if (request.getTurns() < 1) {
            System.out.println("OnError: " + request.getAdType());
            if (request.getAdType() == AdType.META) {
                request.setAdType(AdType.ADMOB);
            } else {
                request.setAdType(AdType.META);
            }
            request.setTurns(request.getTurns() + 1);
            process(request);
        }
    }

    public void process(AdRequestHandler adRequesthandler) {
        AdsCompact compact;
        if (adRequesthandler.getAdType() == AdType.ADMOB) {
            compact = adMob;
            adsLoader.setAdType(AdType.META);
        } else {
            compact = metaAds;
            adsLoader.setAdType(AdType.ADMOB);
        }
        System.out.println("change type: " + adRequesthandler.getAdType() + " TO  " + adsLoader.getAdType());
        adRequesthandler.getHandler().process(compact, adRequesthandler);
    }

}
