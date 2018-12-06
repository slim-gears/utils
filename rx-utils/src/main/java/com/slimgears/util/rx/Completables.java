package com.slimgears.util.rx;

import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.Disposable;

public class Completables {
    public static CompletableTransformer startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }
}
