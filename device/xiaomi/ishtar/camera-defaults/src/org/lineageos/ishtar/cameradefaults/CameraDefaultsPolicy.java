/* Copyright (C) 2026 CyberWei922
 * SPDX-License-Identifier: Apache-2.0
 */
package org.lineageos.ishtar.cameradefaults;

import java.util.List;

/** A one-time migration of six camera actions, independent of Android for host testing. */
final class CameraDefaultsPolicy {
    static final String CAMERA_PACKAGE = "com.android.camera";
    static final String APERTURE_PACKAGE = "org.lineageos.aperture";

    static final class Route {
        final String action;
        final String component;
        Route(String action, String activity) {
            this.action = "android.media.action." + action;
            this.component = CAMERA_PACKAGE + "/" + CAMERA_PACKAGE + "." + activity;
        }
    }

    static final Route[] ROUTES = {
        new Route("STILL_IMAGE_CAMERA", "AssistantCamera"),
        // Camera is the APK's dedicated secure-camera activity. Its second
        // AssistantCamera alias must not leave the secure action at a chooser.
        new Route("STILL_IMAGE_CAMERA_SECURE", "Camera"),
        new Route("IMAGE_CAPTURE", "OneShotImageCapture"),
        new Route("IMAGE_CAPTURE_SECURE", "OneShotCamera"),
        new Route("VIDEO_CAMERA", "VideoCamera"),
        new Route("VIDEO_CAPTURE", "OneShotVideoCapture"),
    };

    interface Backend {
        boolean completed(Route route);
        boolean targetAvailable(Route route);
        String resolvedComponent(Route route);
        String resolvedPackage(Route route);
        void prefer(Route route);
        void complete(Route route);
        void log(Route route, String result);
    }

    static void migrate(Backend backend) {
        for (Route route : ROUTES) {
            if (backend.completed(route)) continue;
            if (!backend.targetAvailable(route)) {
                backend.log(route, "target unavailable; retry at next boot");
                continue;
            }
            if (route.component.equals(backend.resolvedComponent(route))) {
                backend.complete(route);
                continue;
            }
            String current = backend.resolvedPackage(route);
            // Replace the old ROM camera or an unresolved choice. Preserve an
            // explicit third-party choice, and never change it on later boots.
            if (current != null && !"android".equals(current)
                    && !CAMERA_PACKAGE.equals(current) && !APERTURE_PACKAGE.equals(current)) {
                backend.complete(route);
                backend.log(route, "kept explicit third-party default");
                continue;
            }
            backend.prefer(route);
            if (route.component.equals(backend.resolvedComponent(route))) {
                backend.complete(route);
                backend.log(route, "MIUI default applied");
            } else {
                backend.log(route, "resolution unconfirmed; retry at next boot");
            }
        }
    }
}
