# 小米 13 Ultra（`ishtar`）设备配置

[English](README.md) | **简体中文**

适用于小米 13 Ultra 的 Android 17 设备配置，包含 SM8550 公共设备树、内核设备树（DTS）及配套补丁。

当前配置面向 **Evolution X 12.3**。硬件配置可作为其他 AOSP 类原生 ROM 的移植基础；产品配置和平台补丁需要适配目标 ROM。参见[移植说明](docs/zh-CN/PORTABILITY.md)。

## 设备规格

| 组件 | 规格 |
| --- | --- |
| 设备 | 小米 13 Ultra |
| 代号 | `ishtar` |
| SoC | 高通骁龙 8 Gen 2（SM8550） |
| 架构 | ARM64 |
| 屏幕 | 6.73 英寸 LTPO，3200 × 1440，最高 120 Hz |
| 出厂 Android 版本 | Android 13 |
| 目标 Android 版本 | Android 17 |
| 内核 | Linux 5.15.211，GKI 架构，4 KiB 页大小 |

## 当前状态

**开发中。** build47 已保留数据刷入，KSU root 与默认 Enforcing 已核验。机主已确认 build46 Enforcing 下相机、指纹、NFC、蓝牙清单正常；build47 补正一条相机调试属性标签。范围及剩余问题见[验证状态](docs/zh-CN/STATUS.md)。

当前开发配置使用 **SELinux Enforcing**、关闭 AVB 验证、KernelSU v3.3.1 LKM 和预制 TWRP。详细结果及待验证项目见[验证状态](docs/zh-CN/STATUS.md)。

## 仓库结构

| 路径 | 内容 |
| --- | --- |
| `device/xiaomi/ishtar` | 本机设备配置 |
| `device/xiaomi/sm8550-common` | SM8550 公共设备配置 |
| `kernel/xiaomi/sm8550-devicetrees` | 内核 DTS 源码 |
| `vendor/xiaomi` | Vendor 构建配方和相机载荷要求 |
| `patches` | 内核及平台配套补丁 |
| `assets` | 压缩的 Recovery 和 KernelSU init_boot 输入 |
| `manifests` | 固定版本的系统源码清单 |
| `integration` | 源码版本、文件哈希和接入记录 |
| `scripts` | 文件校验和源码接入工具 |

## 开始使用

按[构建说明](docs/zh-CN/BUILD.md)准备 Android 源码、配套内核源码及专有依赖。

将本合并仓库克隆到 **Android 源码目录之外**，再把内容接入对应路径：

```bash
git clone -b android-17 https://github.com/CyberWei922/android_xiaomi_ishtar.git
cd android_xiaomi_ishtar
python3 scripts/verify.py
python3 scripts/integrate.py /path/to/android-source
python3 scripts/integrate.py /path/to/android-source --apply
```

接入工具会在写入前检查已有文件和补丁输入，发现冲突即停止。整个合并仓库不能直接作为单个 `device/xiaomi/ishtar` repo project。

## 编译

在准备好的 Android 源码根目录执行：

```bash
source build/envsetup.sh
source vendor/lineage/vars/aosp_target_release
lunch "lineage_ishtar-${aosp_target_release}-userdebug"
export OUT_DIR=out
export NINJA_HIGHMEM_NUM_JOBS=1
m -j6 otapackage target-files-package 'KERNEL_CC=CC=" clang" LD="ld.lld --threads=1"'
```

固定依赖及内存要求见[构建说明](docs/zh-CN/BUILD.md)。仓库内的启动镜像对应当前开发配置，其他 ROM 需要匹配自身构建的镜像。

## 文档

- [构建说明](docs/zh-CN/BUILD.md)
- [移植到其他 ROM](docs/zh-CN/PORTABILITY.md)
- [验证状态](docs/zh-CN/STATUS.md)
- [源码来源](docs/zh-CN/PROVENANCE.md)

## 致谢

- [LineageOS](https://github.com/LineageOS)
- [ishtar-aosp](https://github.com/ishtar-aosp)
- [Evolution X](https://github.com/Evolution-X)
- [KernelSU](https://github.com/tiann/KernelSU)
- 所有相关源码的原作者与贡献者。

## 许可证

保留原文件的版权和许可证声明，各组件沿用对应的上游许可证。组件来源及预制镜像源码的可用情况见[源码来源](docs/zh-CN/PROVENANCE.md)。
