# Source provenance

**English** | [简体中文](zh-CN/PROVENANCE.md)

## Device sources

The device tree, common tree, and DTS were exported from the actual build checkout. Original copyright and license notices are retained.

| Component | Upstream | Local source revision |
| --- | --- | --- |
| ishtar | [ishtar-aosp/android_device_xiaomi_ishtar](https://github.com/ishtar-aosp/android_device_xiaomi_ishtar) | `719e3e212764a33bb65a82e824594e30c562eb4e` plus working-tree changes |
| SM8550 common | [ishtar-aosp/android_device_xiaomi_sm8550-common](https://github.com/ishtar-aosp/android_device_xiaomi_sm8550-common) | `9b424fbf62981955997c1e11edc9ffc4a26fcae5` plus working-tree changes |
| DTS | [ishtar-aosp/android_kernel_xiaomi_sm8550-devicetrees](https://github.com/ishtar-aosp/android_kernel_xiaomi_sm8550-devicetrees) | `84022f844cfc0a7807e103d672361d0a66a82d48` |

This combined repository imports snapshots rather than replaying the original repositories' commit histories. `integration/source-lock.json` records the source revisions, and the system manifest records platform origins. Each component retains its upstream license, including the DTS NOTICE and per-file SPDX declarations.

## Companion patches

Patches for 12 Git platform components were generated from their actual HEAD and working-tree contents, including new backuptool files. Export checks reconstructed the original files in temporary directories, applied the patches, and verified that all resulting files matched the build inputs.

The kernel and module trees were restored from an archive. The pinned reference revisions and companion patches are included, with matching hashes for the kernel configuration and six module files. Comparison of the complete restored trees against public upstream sources remains outstanding.

## Prebuilt images

The preserved custom TWRP image has SHA256:

```text
c39e0236d4aac787e59827f835d58ec3af127357bcd576fb2b66da72c2c4057e
```

Its complete source repository has not been identified in the available records. The image is included as a compressed build input; decompression preserves its exact bytes. This repository does not contain a full TWRP source project.

KernelSU uses the official [v3.3.1 LKM release](https://github.com/tiann/KernelSU/releases/tag/v3.3.1). The patched image was based on this Android 17 build's own init_boot and preserves its original init. Other ROMs need an init_boot generated and matched to their own build.

## External payloads

Proprietary Xiaomi and GMS APKs and shared libraries are supplied separately. The included vendor build recipes describe those dependencies. User data, raw device logs, ADB identities, and signing private keys are not included in this repository.
