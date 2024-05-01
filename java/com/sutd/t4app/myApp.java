package com.sutd.t4app;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
/**
 * The `myApp` class in the Android application initializes Realm
 */

@HiltAndroidApp
public class myApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm
        Realm.init(this);

}}


