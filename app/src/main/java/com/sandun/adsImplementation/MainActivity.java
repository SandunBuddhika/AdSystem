package com.sandun.adsImplementation;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sandun.adssystem.model.AdsInitializer;
import com.sandun.adssystem.model.model.AdMethodType;
import com.sandun.adssystem.model.model.AdType;
import com.sandun.adssystem.model.model.AdsMediator;
import com.sandun.adssystem.model.model.handler.AdRequestHandler;
import com.sandun.adssystem.model.model.handler.ViewAdRequestHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        AdsInitializer initializer = AdsInitializer.getInstance(
                new AdsInitializer.FacebookIds("561288649843138", "561288649843138_561290069842996", "561288649843138_561289826509687", "561288649843138_561290649842938"),
                new AdsInitializer.GoogleIds("ca-app-pub-3940256099942544~3347511713", "ca-app-pub-3940256099942544/1033173712", "ca-app-pub-3940256099942544/9214589741", "ca-app-pub-3940256099942544/9257395921", "ca-app-pub-3940256099942544/5224354917", "ca-app-pub-3940256099942544/2247696110"));

        AdsMediator mediator = AdsMediator.getInstance(this, initializer);
        mediator.setAdMethodType(AdMethodType.ADMOB);
//        mediator.preLoadAds(AdType.INTERSTITIAL);
//        mediator.preLoadAds(AdType.REWARD);
//        mediator.preLoadAds(AdType.OPEN);

        findViewById(R.id.interstitial_ad_btn).setOnClickListener(v -> {
            mediator.showInterstitialAd(new AdRequestHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                    mediator.preLoadAds(AdType.INTERSTITIAL);
                }

                @Override
                public void onError() {
                    System.out.println("onError");
                }

            });
        });
        findViewById(R.id.reward_ad_btn).setOnClickListener(v -> {
            mediator.showRewardAd(new AdRequestHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                    mediator.preLoadAds(AdType.REWARD);
                }

                @Override
                public void onError() {
                    System.out.println("onError");
                }
            });
        });
        findViewById(R.id.open_ad_btn).setOnClickListener(v -> {
            mediator.showOpenAd(new AdRequestHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                }

                @Override
                public void onError() {
                    System.out.println("onError");
                }
            });
        });
        findViewById(R.id.native_ad_btn).setOnClickListener(v -> {
            mediator.showNativeAd(new ViewAdRequestHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                }

                @Override
                public void onError() {
                    System.out.println("onError");
                }

                @Override
                public void viewHandler(View adView) {
                }
            }, findViewById(R.id.native_ad_container));
        });
        findViewById(R.id.banner_ad_btn).setOnClickListener(v -> {
            mediator.showBannerAd(new ViewAdRequestHandler() {
                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                }

                @Override
                public void onError() {
                    System.out.println("onError");
                }

                @Override
                public void viewHandler(View adView) {
                }
            }, findViewById(R.id.banner_ad_container));
        });
    }
}