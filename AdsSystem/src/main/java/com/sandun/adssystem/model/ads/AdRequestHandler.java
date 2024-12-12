package com.sandun.adssystem.model.ads;

public class AdRequestHandler {
    private AdHandler handler;
    private AdType adType;
    private int turns = 0;
    private boolean preLoading;

    public AdRequestHandler(AdHandler handler, AdType adType) {
        this.handler = handler;
        this.adType = adType;
    }


    public AdHandler getHandler() {
        return handler;
    }

    public void setHandler(AdHandler handler) {
        this.handler = handler;
    }

    public AdType getAdType() {
        return adType;
    }

    public void setAdType(AdType adType) {
        this.adType = adType;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
}
