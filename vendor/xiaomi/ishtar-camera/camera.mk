# Select the accepted legacy shim only for this product.
$(call soong_config_set,ishtar_camera,accepted_hidltransport,true)

# Accepted ishtar camera03 system payloads and matching vendor settings.
PRODUCT_SOONG_NAMESPACES += vendor/xiaomi/ishtar-camera

PRODUCT_PACKAGES += \
    MiuiCamera \
    libhidltransport \
    ishtar_camera_libOpenCL \
    ishtar_camera_libcamera_algoup_jni.xiaomi \
    ishtar_camera_libcamera_mianode_jni.xiaomi \
    ishtar_camera_libcdsprpc \
    ishtar_camera_libmicampostproc_client \
    ishtar_camera_libopencl-camera \
    ishtar_camera_libvmmem \
    ishtar_camera_vendor.qti.hardware.dsp@1.0 \
    ishtar_camera_vendor.xiaomi.hardware.campostproc@1.0

PRODUCT_COPY_FILES += \
    vendor/xiaomi/ishtar-camera/proprietary/system/etc/default-permissions/miuicamera-permissions.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/default-permissions/miuicamera-permissions.xml \
    vendor/xiaomi/ishtar-camera/proprietary/system/etc/permissions/privapp-permissions-miuicamera.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/privapp-permissions-miuicamera.xml \
    vendor/xiaomi/ishtar-camera/proprietary/system/etc/sysconfig/miuicamera-hiddenapi-package-whitelist.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/sysconfig/miuicamera-hiddenapi-package-whitelist.xml \
    vendor/xiaomi/ishtar-camera/proprietary/vendor/etc/camera/camxoverridesettings.txt:$(TARGET_COPY_OUT_VENDOR)/etc/camera/camxoverridesettings.txt
