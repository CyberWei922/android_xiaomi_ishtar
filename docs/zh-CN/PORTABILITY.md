# 复用到其他类原生 ROM

[English](../PORTABILITY.md) | **简体中文**

仓库名不绑定 EvoX。当前文件保留实际成功构建的配置，尚未做其他 ROM 的验证。

## 可以复用的硬件适配

- ishtar 的分区、屏幕、相机、音频、触控、指纹、NFC、初始化和硬件属性配置。
- SM8550 公共设备配置、配套内核 DTS，以及匹配 .307 vendor 的兼容处理。
- 与上述版本配套的设备 SELinux 规则及内核相机补丁。

“可以复用”指移植基础，依然需要匹配 Android 平台接口、内核与厂商载荷版本。

## 换 ROM 时需要对齐

1. `AndroidProducts.mk`、`lineage_ishtar.mk`、`device.mk`、`common.mk` 中的产品命名、`vendor/lineage` 继承和组件。当前 EvoX 清单把自身配置放在 `vendor/lineage`。
2. Lineage 的 Soong 命名空间、硬件接口、提取工具、设置和设备专属包。不能仅把继承路径中的 ROM 名称替换掉。
3. `patches/` 中的 Recovery 打包、自身包可见性、OTA/backuptool、SurfaceFlinger 和全局策略修改。这些是当前系统的配套改动，必须按目标 ROM 的源码版本逐项比较，不应整包强行套用。
4. 4 KiB 页大小、5.15 内核/模块/DTS 和 .307 专有文件的组合，不能混用不同版本后假定 ABI 兼容。
5. 调试配置：Permissive、AVB flags=3、KSU init_boot 和固定 TWRP 都是当前构建选择。其他 ROM 的 init_boot 应由该 ROM 自己生成并重新匹配，不能把这里的预制镜像视为通用镜像。

迁移后应重新执行配置、SELinux、VINTF、完整 OTA 和设备验收。当前已有的 EvoX 验证结果不能替代这些检查。
