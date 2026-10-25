# 来源与边界

[English](../PROVENANCE.md) | **简体中文**

设备树、公共树和 DTS 从实际构建目录导出，原文件版权声明保留；不是从早期候选目录直接打包。对应本地提交分别为：

- ishtar：`719e3e212764a33bb65a82e824594e30c562eb4e` 加实际工作区改动。
- sm8550-common：`9b424fbf62981955997c1e11edc9ffc4a26fcae5` 加实际工作区改动。
- DTS：`84022f844cfc0a7807e103d672361d0a66a82d48`。

上游分别为 `ishtar-aosp/android_device_xiaomi_ishtar`、`ishtar-aosp/android_device_xiaomi_sm8550-common` 和 `ishtar-aosp/android_kernel_xiaomi_sm8550-devicetrees`。

这些本地适配提交的历史不由此单仓库重放。`integration/source-lock.json` 记录原提交，系统清单记录公开组件来源；后续可按此比较上游。每个文件仍适用原许可证，包括 DTS 中的 NOTICE 和 SPDX 声明。

12 个 Git 平台组件的补丁均从其实际 HEAD 和工作区生成，包含新增的 backuptool 文件；发布导出时在临时目录重建原始文件、应用补丁并比较结果与实际构建输入，所有文件一致。

内核和内核模块目前是存档恢复树；本集合保存已有的固定上游参考与配套补丁，并核对配置及 6 个模块文件的实际哈希。尚未进行完整树与公开上游的比对，不能把选定文件一致写成整棵内核来源已完全验证。

TWRP 预制镜像沿用已保护版本，SHA256 为 `c39e0236d4aac787e59827f835d58ec3af127357bcd576fb2b66da72c2c4057e`。当前记录没有定位其完整源码仓库；本集合不宣称提供了 TWRP 的完整源码。镜像压缩仅用于保持所有适配输入在同一仓库，解压后字节不变。

KernelSU 来自官方 `tiann/KernelSU` 的 v3.3.1 LKM 发行版，使用本次 Android 17 自身的 init_boot 作为补丁基底，保留原 init。上游为 <https://github.com/tiann/KernelSU/releases/tag/v3.3.1>；不同 ROM 应自行生成并匹配 init_boot。

本仓库不包含用户数据、设备原始日志、ADB 身份或签名私钥。Xiaomi/GMS 专有 APK/so 不包含在导出中；vendor 构建配方不替代这些依赖。
