# Porting to other ROMs

**English** | [简体中文](zh-CN/PORTABILITY.md)

This repository keeps the configuration used for the successful Evolution X build. Other ROMs have not been validated with this snapshot.

## Reusable hardware configuration

- ishtar partition layout, display, camera, audio, touch, fingerprint, NFC, initialization, and hardware properties.
- Shared SM8550 configuration, matching kernel DTS sources, and compatibility changes for the .307 vendor base.
- Device SELinux rules and kernel camera patches for that combination of sources.

Reuse requires matching Android platform interfaces, kernel sources, and proprietary file versions.

## Target ROM integration

Review the following when adapting the tree:

1. **Product configuration:** product names, `vendor/lineage` inheritance, and packages in `AndroidProducts.mk`, `lineage_ishtar.mk`, `device.mk`, and `common.mk`. The current Evolution X manifest stores its ROM configuration under `vendor/lineage`.
2. **Lineage dependencies:** Soong namespaces, hardware interfaces, extraction tools, settings, and device packages. Each dependency needs a compatible implementation in the target ROM.
3. **Platform patches:** recovery packaging, package self-visibility, OTA/backuptool behavior, SurfaceFlinger, and global SELinux changes in `patches/`. Review each patch against the target source revision before applying it.
4. **Kernel and vendor compatibility:** retain a matching combination of 4 KiB pages, the 5.15 kernel, kernel modules, DTS, and .307 proprietary files. ABI compatibility must be checked when changing any of these inputs.
5. **Development images:** Permissive SELinux, AVB flags=3, KernelSU init_boot, and prebuilt TWRP are choices for the current development build. Generate and match init_boot to the target ROM; the bundled image is specific to this configuration.

After porting, repeat configuration, SELinux, VINTF, full OTA, and device validation. The existing Evolution X results apply to the tested configuration.
