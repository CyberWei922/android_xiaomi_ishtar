#
# Copyright (C) 2025 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

DEVICE_PATH := device/xiaomi/ishtar

# NFC
TARGET_USES_ST_NFC := true

# Inherit from xiaomi sm8550-common
include device/xiaomi/sm8550-common/BoardConfigCommon.mk

# NFC
TARGET_KERNEL_EXT_MODULES := $(filter-out nxp/opensource/driver,$(TARGET_KERNEL_EXT_MODULES))
BOARD_VENDOR_KERNEL_MODULES_LOAD := $(filter-out nxp-nci.ko,$(BOARD_VENDOR_KERNEL_MODULES_LOAD))

# Display
TARGET_SCREEN_DENSITY := 560

# Kernel
TARGET_KERNEL_EXT_MODULE_ROOT := kernel/xiaomi/sm8550-modules
TARGET_KERNEL_EXT_MODULES += \
	st/opensource/driver

BOARD_VENDOR_KERNEL_MODULES_LOAD += \
	stm_nfc_i2c.ko \
	cs35l41_dlkm.ko \
	goodix_fod.ko \
	fts_touch_spi.ko \
	mi_thermal_interface.ko \
	qcom_pm8008-regulator.ko \
	stmvl53l5.ko \
	wl2866d.ko

BOARD_VENDOR_RAMDISK_RECOVERY_KERNEL_MODULES_LOAD += \
	fts_touch_spi.ko

BOOT_KERNEL_MODULES += \
	fts_touch_spi.ko

# Owner-accepted Recovery touch repair (2026-10-17): PM8008 loading and ROM FTS firmware.
# Fixed same-kernel prebuilt; Data decryption and new-ROM runtime need separate validation.
BOARD_PREBUILT_RECOVERYIMAGE := $(DEVICE_PATH)/recovery/prebuilt-recovery.img
BOARD_PREBUILT_RECOVERYIMAGE_SHA256 := c39e0236d4aac787e59827f835d58ec3af127357bcd576fb2b66da72c2c4057e

# Refuse a build system that would silently regenerate a different recovery.
ifneq ($(shell grep -c "^ISHTAR_PREBUILT_RECOVERY_CONTRACT_VERSION := 1$$" build/make/core/Makefile 2>/dev/null),1)
$(error Apply the pinned Android17 build/make recovery patch from the integration repository)
endif

# Owner-requested temporary Permissive boot for Android17 first-boot AVC diagnosis.
BOARD_BOOTCONFIG += androidboot.selinux=permissive
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive enforcing=0

# Properties
TARGET_SYSTEM_PROP += $(DEVICE_PATH)/properties/camera-system.prop
# Framework-owned properties must be loaded from the system partition.
TARGET_SYSTEM_PROP += $(DEVICE_PATH)/properties/platform-system.prop
TARGET_ODM_PROP += $(DEVICE_PATH)/properties/odm.prop
TARGET_PRODUCT_PROP += $(DEVICE_PATH)/properties/product.prop
TARGET_VENDOR_PROP += $(DEVICE_PATH)/properties/vendor.prop

# Sepolicy
BOARD_VENDOR_SEPOLICY_DIRS += $(DEVICE_PATH)/sepolicy/vendor

# Inherit from the proprietary version
include vendor/xiaomi/ishtar/BoardConfigVendor.mk

# Keep this system camera application's type exported to its narrow vendor clients.
SYSTEM_EXT_PUBLIC_SEPOLICY_DIRS += $(DEVICE_PATH)/sepolicy/public
SYSTEM_EXT_PRIVATE_SEPOLICY_DIRS += $(DEVICE_PATH)/sepolicy/private

# Owner-requested KernelSU v3.3.1 LKM for first-boot system diagnosis.
BOARD_PREBUILT_INIT_BOOT_IMAGE := $(DEVICE_PATH)/root/init_boot-KernelSU-v3.3.1.img
