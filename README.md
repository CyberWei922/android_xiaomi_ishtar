# Device configuration for Xiaomi 13 Ultra (`ishtar`)

**English** | [简体中文](README.zh-CN.md)

Android 17 device configuration for the Xiaomi 13 Ultra, including the SM8550 common tree, device trees (DTS), and companion patches.

The current configuration targets **Evolution X 12.3**. The hardware configuration can serve as a base for other AOSP-based ROMs; product configuration and platform patches need to be adapted to the target ROM. See [Porting](docs/PORTABILITY.md).

## Device specifications

| Component | Specification |
| --- | --- |
| Device | Xiaomi 13 Ultra |
| Codename | `ishtar` |
| SoC | Qualcomm Snapdragon 8 Gen 2 (SM8550) |
| Architecture | ARM64 |
| Display | 6.73-inch LTPO, 3200 × 1440, up to 120 Hz |
| Shipped Android version | Android 13 |
| Target Android version | Android 17 |
| Kernel | Linux 5.15.211, GKI architecture, 4 KiB pages |

## Status

**Work in progress.** The current Android 17 source snapshot has been built and installed with data preserved and root verified. Repeated camera open/close tests passed after OIS lifecycle repairs. Functional camera acceptance of this revision is assigned to the owner and remains pending. Basic photo/video capture and six SurfaceFlinger regressions passed on an earlier build. SELinux remains Permissive; see [Validation status](docs/STATUS.md) for test scopes and remaining work.

The current development configuration uses **Permissive SELinux**, disabled AVB verification, KernelSU v3.3.1 LKM, and prebuilt TWRP. Detailed results and remaining validation are documented in [Status](docs/STATUS.md).

## Repository layout

| Path | Contents |
| --- | --- |
| `device/xiaomi/ishtar` | Device-specific configuration |
| `device/xiaomi/sm8550-common` | Shared SM8550 configuration |
| `kernel/xiaomi/sm8550-devicetrees` | Kernel DTS sources |
| `vendor/xiaomi` | Vendor build recipes and camera payload requirements |
| `patches` | Companion kernel and platform patches |
| `assets` | Compressed recovery and KernelSU init_boot inputs |
| `manifests` | Pinned system source manifests |
| `integration` | Source revisions, file hashes, and integration records |
| `scripts` | Verification and integration tools |

## Getting started

Prepare the Android source tree, matching kernel sources, and proprietary dependencies described in the [Build guide](docs/BUILD.md).

Clone this combined repository **outside the Android source tree**, then integrate its contents into the appropriate paths:

```bash
git clone -b android-17 https://github.com/CyberWei922/android_xiaomi_ishtar.git
cd android_xiaomi_ishtar
python3 scripts/verify.py
python3 scripts/integrate.py /path/to/android-source
python3 scripts/integrate.py /path/to/android-source --apply
```

The integration tool checks existing files and patch inputs before writing. A conflicting file stops integration. The combined repository itself is not a single `device/xiaomi/ishtar` repo project.

## Build

From the prepared Android source root:

```bash
source build/envsetup.sh
source vendor/lineage/vars/aosp_target_release
lunch "lineage_ishtar-${aosp_target_release}-userdebug"
export OUT_DIR=out
export NINJA_HIGHMEM_NUM_JOBS=1
m -j6 otapackage target-files-package 'KERNEL_CC=CC=" clang" LD="ld.lld --threads=1"'
```

See the [Build guide](docs/BUILD.md) for pinned dependencies and memory requirements. The bundled boot images match this development configuration; other ROMs need matching images.

## Documentation

- [Build guide](docs/BUILD.md)
- [Porting to other ROMs](docs/PORTABILITY.md)
- [Validation status](docs/STATUS.md)
- [Source provenance](docs/PROVENANCE.md)

## Credits

- [LineageOS](https://github.com/LineageOS)
- [ishtar-aosp](https://github.com/ishtar-aosp)
- [Evolution X](https://github.com/Evolution-X)
- [KernelSU](https://github.com/tiann/KernelSU)
- All original authors and contributors to the included sources.

## License

Original copyright notices and licenses are retained. Each component remains under its respective upstream license. See [Source provenance](docs/PROVENANCE.md) for component origins and prebuilt source availability.
