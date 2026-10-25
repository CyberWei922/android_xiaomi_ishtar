# 构建说明

## 依赖

当前验证基线为 Android 17 / Evolution X 12.3，产品 `lineage_ishtar`。系统源使用 `manifests/evox17-build37.xml` 的固定提交；`evox17-system-overrides.xml` 是当时使用的系统清单覆盖记录。

完整源码清单可供 repo 使用，示例：在另一个空目录执行 `repo init -u https://github.com/Evolution-X/manifest -b cnb`，将本仓库的 `manifests/evox17-build37.xml` 复制为该工作区的 `.repo/manifests/ishtar-build37.xml`，再执行 `repo init -m ishtar-build37.xml` 和 `repo sync -j6`。固定清单保留上游的公开下载地址；这些地址未来的可用性未在本次发布整理中重新下载验证。

此外需要：

| 源码目录 | 来源 / 固定版本 |
| --- | --- |
| `device/xiaomi/ishtar` | 本仓库完整快照 |
| `device/xiaomi/sm8550-common` | 本仓库完整快照 |
| `kernel/xiaomi/sm8550-devicetrees` | 本仓库完整快照 |
| `kernel/xiaomi/sm8550` | `ishtar-aosp/android_kernel_xiaomi_sm8550`，参考提交 `34eaaccbb3d71edb8e03eb903d113e0e105b6237`；配套 `patches/kernel.patch` |
| `kernel/xiaomi/sm8550-modules` | `ishtar-aosp/android_kernel_xiaomi_sm8550-modules`，参考提交 `cf984f86bcfc795190429a7c094fe6bbde5f7b02`；配套 `patches/kernel-modules.patch` |
| `hardware/xiaomi` | `LineageOS/android_hardware_xiaomi`，提交 `7dd87795288bf6713795f112056b1bf549355c81`，再应用本仓库补丁；此项目未包含在导出的系统清单中，需另行准备 |
| `vendor/qcom/opensource/power` | 以实际系统清单中的固定项目为准，保留匹配 .307 的 QTI 电源方案 |
| `vendor/qcom/opensource/vibrator` | `97d88ab231d84ba0b5549e6d6148522d6d73ee24`，再应用本仓库补丁 |
| `vendor/xiaomi/{ishtar,sm8550-common,ishtar-camera,ishtar-camera-platform}` | 匹配最终 Android 16 所用 .307 载荷；本仓库提供实际构建配方及相机载荷哈希，不含专有 APK/so |
| `vendor/gms`、WebView | 系统清单对应版本及完整预制载荷 |

UU 当前内核及模块目录来自最终存档恢复，不带 Git 元数据。已核对内核配置和 6 个模块补丁文件与实际构建输入一致，但**尚未证明公开上游完整树加这些补丁与 UU 的整棵恢复树逐字节一致**。因此本次发布不是在全新机器上重新下载、重编译成功的证明，详见 `PROVENANCE.md`。

## 接入与编译

完成源码和专有载荷准备后，在此仓库外的 Android 源码根目录接入：

```bash
python3 /path/to/android_xiaomi_ishtar/scripts/integrate.py "$PWD"
python3 /path/to/android_xiaomi_ishtar/scripts/integrate.py "$PWD" --apply
source build/envsetup.sh
source vendor/lineage/vars/aosp_target_release
lunch "lineage_ishtar-${aosp_target_release}-userdebug"
export OUT_DIR=out
export NINJA_HIGHMEM_NUM_JOBS=1
m -j6 otapackage target-files-package 'KERNEL_CC=CC=" clang" LD="ld.lld --threads=1"'
```

这是实际 build37 使用的产品和内核参数；当时 `aosp_target_release=cp2a`。该构建要求足够内存和磁盘：UU 构建使用临时额外 32 GiB SSD swap、总 swap 约 40 GiB，构建组 `MemoryHigh=25G` / `MemoryMax=27G`，普通并行 6、高内存池 1、Full LTO 和内核链接单线程。仅降低 `m -j` 不能限制所有链接进程的总内存。

接入脚本会恢复两个压缩镜像并核验 SHA256；`BoardConfig.mk` 中固定 Recovery 与 KSU init_boot 路径保持与 build36/37 一致。镜像适用范围见 `PORTABILITY.md`。TWRP 完整源码工程不在本仓库中。

## 检查范围

发布整理执行文件校验、平台补丁往返和接入脚本检查，不重新编译 ROM，不改 UU 的实际源码或输出，不操作手机。现有 ROM 构建和设备结果见 `STATUS.md`。
