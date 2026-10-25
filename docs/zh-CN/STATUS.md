# 验证状态（2026-10-25）

[English](../STATUS.md) | **简体中文**

## build36：已安装的基线

- Android 17 / SDK 37 / Evolution X 12.3，incremental `1792906981`。
- 保留数据从最终 Android 16 升级，全部 34 个 payload 分区及 postinstall 通过。
- 正常开机；机主确认大部分基础功能正常；原用户应用和数据标记保留，KSU `su uid=0` 已实读。
- 全局 SELinux 为 Permissive，尚未完成 Android 17 Enforcing 验收。
- OTA SHA256：`9edbe1b9ab25ae2c00f6fbb7386a39d5e542d56b14f11f141be274432490db03`。

## build37：本仓库导出状态，尚未安装

- incremental `1792914428`；完整构建、50 个源码输入、9 项修复产物、全部 34 个实际 payload 分区核验通过。
- 性能 HAL 的实际 WALT 标签访问规则、移除不支持的 rootTG 提示项、创建 kswapd cpuset 并按内核线程标签绑定。
- 移除不存在的 ptt / boot_slpi 启动项，保留运行中的 DSP 与传感器配置。
- 振动：缺失 primitive 返回不支持，NOOP 仅延迟，THUD 使用对应普通 THUD 波形；4 个必需 primitive 缺少校准波形时暂不声明复合能力。普通振动未被机主报告故障，未证明日常触感改善。
- 新振动库 15 项独立检查、HAL 方法隔离 6 项检查通过；kswapd 临时实机绑定测试后已恢复。没有替换当前运行的 HAL。
- 相机未改驱动。现有日志不足以证明相机故障，切换镜头及退出收尾仍待专项实测。
- 全局 Permissive、KSU v3.3.1 LKM、AVB 根 flags=3、原 TWRP、内核 5.15.211 均保留。
- 实际策略 SHA256：`e328e8bd9cf832d5b261b6fc319d0866e121215efae71dc72e4e119274483276`，策略 permissive 域列表 `[]`。
- OTA SHA256：`d6d2f11d29315cd751a86fcbfad1fdce6082b7b4bf12d7836a1d23d5f335bf5b`。
- **尚未刷入；新 HAL、整包运行与 Enforcing 均未实机验收。** 6 条 depmod 元数据警告及 Ninja restat 警告与 36 一致，不称为零警告构建。

Android 16 最终版 incremental `1792575161` 的实机状态是 Enforcing，最终策略 permissive 域列表为空。早期迁移中的 9 个 permissive 不属于最终 Android 16 的遗留问题。
