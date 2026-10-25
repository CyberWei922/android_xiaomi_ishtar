# Build guide

**English** | [简体中文](zh-CN/BUILD.md)

## Source requirements

The validated platform is Android 17 / Evolution X 12.3, using the `lineage_ishtar` product. System source revisions are pinned in `manifests/evox17-build37.xml`. The companion `evox17-system-overrides.xml` records the manifest overrides used during development.

To initialize the pinned system checkout in a separate, empty directory:

```bash
repo init -u https://github.com/Evolution-X/manifest -b cnb
cp /path/to/android_xiaomi_ishtar/manifests/evox17-build37.xml .repo/manifests/ishtar-build37.xml
repo init -m ishtar-build37.xml
repo sync -j6
```

The manifest retains the original source URLs. Availability of every upstream revision has not been rechecked by downloading a fresh checkout for this publication.

Prepare these additional inputs:

| Source path | Required input |
| --- | --- |
| `device/xiaomi/ishtar` | Complete snapshot in this repository |
| `device/xiaomi/sm8550-common` | Complete snapshot in this repository |
| `kernel/xiaomi/sm8550-devicetrees` | Complete snapshot in this repository |
| `kernel/xiaomi/sm8550` | `ishtar-aosp/android_kernel_xiaomi_sm8550`, reference commit `34eaaccbb3d71edb8e03eb903d113e0e105b6237`, plus `patches/kernel.patch` |
| `kernel/xiaomi/sm8550-modules` | `ishtar-aosp/android_kernel_xiaomi_sm8550-modules`, reference commit `cf984f86bcfc795190429a7c094fe6bbde5f7b02`, plus `patches/kernel-modules.patch` |
| `hardware/xiaomi` | `LineageOS/android_hardware_xiaomi` at `7dd87795288bf6713795f112056b1bf549355c81`, plus the companion patch; prepare this separately because it is absent from the exported system manifest |
| `vendor/qcom/opensource/power` | Revision pinned in the system manifest; retain the QTI power implementation matched to the .307 vendor base |
| `vendor/qcom/opensource/vibrator` | `97d88ab231d84ba0b5549e6d6148522d6d73ee24`, plus the companion patch |
| `vendor/xiaomi/{ishtar,sm8550-common,ishtar-camera,ishtar-camera-platform}` | Proprietary files matching the final Android 16 .307 base; build recipes and camera payload hashes are included here, while proprietary APKs and shared libraries are supplied separately |
| `vendor/gms`, WebView | Manifest-matched revisions and complete prebuilt payloads |

The working kernel and module trees were restored from the final development archive and have no Git metadata. The kernel configuration and six patched module files match the actual build inputs. **The complete restored trees have not been compared byte for byte with the referenced upstream trees plus these patches.** A fresh build from newly downloaded dependencies remains to be verified. See [Source provenance](PROVENANCE.md).

## Integration

Keep this combined repository outside the Android source root to avoid duplicate Soong module discovery. After preparing the source and proprietary dependencies, run from the Android source root:

```bash
python3 /path/to/android_xiaomi_ishtar/scripts/integrate.py "$PWD"
python3 /path/to/android_xiaomi_ishtar/scripts/integrate.py "$PWD" --apply
```

The first command performs a preflight check. The second applies the checked changes. Existing source files must match this snapshot; patch inputs must match the recorded original hashes. Conflicts stop the preflight before destination files are changed.

The tool also restores the two compressed images and checks their SHA256 hashes. Their paths in `BoardConfig.mk` match build36/37. See [Porting](PORTABILITY.md) for image compatibility; the full TWRP source project is not included.

## Compilation

```bash
source build/envsetup.sh
source vendor/lineage/vars/aosp_target_release
lunch "lineage_ishtar-${aosp_target_release}-userdebug"
export OUT_DIR=out
export NINJA_HIGHMEM_NUM_JOBS=1
m -j6 otapackage target-files-package 'KERNEL_CC=CC=" clang" LD="ld.lld --threads=1"'
```

These are the product and kernel arguments used for build37; `aosp_target_release` was `cp2a`.

The development host used:

- Six regular build jobs and one high-memory job.
- Full LTO, with one kernel linker thread.
- A temporary additional 32 GiB of SSD swap, approximately 40 GiB total swap.
- A build cgroup with `MemoryHigh=25G` and `MemoryMax=27G`.

Plan sufficient memory and disk space for the full build. Setting `m -j` alone does not cap the combined memory use of all linker processes.

## Validation

Publication checks cover file integrity, patch round trips, and integration behavior. They did not rebuild the ROM or operate the phone. Existing build and device results are documented in [Validation status](STATUS.md).
