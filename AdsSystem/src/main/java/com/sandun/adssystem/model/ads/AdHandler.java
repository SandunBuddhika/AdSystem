package com.sandun.adssystem.model.ads;

public interface AdHandler {
    void process(AdsCompact adsCompact, AdRequestHandler handler);
}
