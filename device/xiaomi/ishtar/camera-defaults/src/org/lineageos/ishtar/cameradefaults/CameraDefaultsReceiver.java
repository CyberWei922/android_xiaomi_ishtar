/* Copyright (C) 2026 CyberWei922
 * SPDX-License-Identifier: Apache-2.0
 */
package org.lineageos.ishtar.cameradefaults;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.List;

/** Runs briefly on boot/package replacement; there is no service or launcher activity. */
public final class CameraDefaultsReceiver extends BroadcastReceiver {
    private static final String TAG = "IshtarCameraDefaults";
    private static final int QUERY_FLAGS = PackageManager.MATCH_DEFAULT_ONLY
            | PackageManager.MATCH_DIRECT_BOOT_AWARE | PackageManager.MATCH_DIRECT_BOOT_UNAWARE;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action)
                && !Intent.ACTION_BOOT_COMPLETED.equals(action)
                && !Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) return;
        // The owner requested migration of this personal phone. Work profiles
        // and secondary users retain their own defaults and administrative policy.
        if (UserHandle.myUserId() != UserHandle.USER_SYSTEM) return;
        Context storage = context.createDeviceProtectedStorageContext();
        SharedPreferences state = storage.getSharedPreferences("camera-defaults-v1", 0);
        PackageManager pm = context.getPackageManager();
        try {
            CameraDefaultsPolicy.migrate(new CameraDefaultsPolicy.Backend() {
                private List<ResolveInfo> candidates(CameraDefaultsPolicy.Route route) {
                    return pm.queryIntentActivities(new Intent(route.action), QUERY_FLAGS);
                }
                private String key(CameraDefaultsPolicy.Route route) { return route.action; }
                public boolean completed(CameraDefaultsPolicy.Route route) {
                    return state.getBoolean(key(route), false);
                }
                public boolean targetAvailable(CameraDefaultsPolicy.Route route) {
                    ComponentName desired = ComponentName.unflattenFromString(route.component);
                    for (ResolveInfo candidate : candidates(route)) {
                        ActivityInfo ai = candidate.activityInfo;
                        if (desired.equals(new ComponentName(ai.packageName, ai.name))) {
                            return ai.exported && ai.enabled && ai.applicationInfo.enabled;
                        }
                    }
                    return false;
                }
                private ResolveInfo resolved(CameraDefaultsPolicy.Route route) {
                    return pm.resolveActivity(new Intent(route.action), QUERY_FLAGS);
                }
                public String resolvedComponent(CameraDefaultsPolicy.Route route) {
                    ResolveInfo ri = resolved(route);
                    return ri == null || ri.activityInfo == null ? null
                            : new ComponentName(ri.activityInfo.packageName,
                                    ri.activityInfo.name).flattenToString();
                }
                public String resolvedPackage(CameraDefaultsPolicy.Route route) {
                    ResolveInfo ri = resolved(route);
                    return ri == null || ri.activityInfo == null ? null : ri.activityInfo.packageName;
                }
                @SuppressWarnings("deprecation")
                public void prefer(CameraDefaultsPolicy.Route route) {
                    LinkedHashSet<ComponentName> components = new LinkedHashSet<>();
                    int bestMatch = 0;
                    for (ResolveInfo ri : candidates(route)) {
                        components.add(new ComponentName(ri.activityInfo.packageName,
                                ri.activityInfo.name));
                        bestMatch = Math.max(bestMatch, ri.match);
                    }
                    IntentFilter filter = new IntentFilter(route.action);
                    filter.addCategory(Intent.CATEGORY_DEFAULT);
                    pm.replacePreferredActivity(filter, bestMatch,
                            components.toArray(new ComponentName[0]),
                            ComponentName.unflattenFromString(route.component));
                }
                public void complete(CameraDefaultsPolicy.Route route) {
                    if (!state.edit().putBoolean(key(route), true).commit()) {
                        Log.w(TAG, "Could not save migration state for " + route.action);
                    }
                }
                public void log(CameraDefaultsPolicy.Route route, String result) {
                    Log.i(TAG, route.action + ": " + result);
                }
            });
        } catch (RuntimeException error) {
            // Keep normal boot usable; an incomplete action can retry next boot.
            Log.e(TAG, "Camera default migration deferred", error);
        }
    }
}
