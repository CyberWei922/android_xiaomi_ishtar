# 验证状态（2026-10-25）

[English](../STATUS.md) | **简体中文**

## build43：当前已安装的源码快照

- Android 17 / SDK 37 / Evolution X 12.3，incremental `1792926429`。
- 6 并行、原 25G/27G 内存保护及 Full LTO；53 项源码输入与实际 OTA 全部 34 个分区核验通过。
- 从 build41 保留数据刷入，安装器 34 分区哈希、目标槽 7 个启动镜像回读匹配；24 个原用户应用及数据标记保留。
- 保持全局 **Permissive**、KSU v3.3.1 LKM root、AVB 根 flags=3、原 TWRP、内核 5.15.211；Android 17 Enforcing 尚未实机验收。
- 实际策略 SHA256：`e328e8bd9cf832d5b261b6fc319d0866e121215efae71dc72e4e119274483276`，策略 permissive 域列表 `[]`。全局模式与域声明是两回事。
- OTA SHA256：`8b87bbcbdc5b72c771080c1047f83bf6317144401420b8209a3586c5f1ed965a`。

## 本轮 OIS 生命周期修复

- build40 新日志复现了刷新队列后 14 次 request ID=0 被错误当作旧帧拒绝。修复后即时请求不参与旧帧序号过滤，非零旧帧原有检查保留；驱动编译前 8 项 C 表达式检查通过。
- 正常释放设备时，在掉电判断后清理已结束的镜头停泊线程状态，避免 STOP/退出状态带入下一次打开；保留原有线程等待及电源归属判断，未改寄存器表或固件。
- 实际 OTA 和手机中的新 camera.ko 均核验为 `ea88ece9db5674f8d657ca3202d914a86db52264d4c2b620559cf1eb912f809b`；内核 Image 未改。
- 连续 3 次开关相机，活动客户端及释放正常，provider 稳定；新 OIS 日志未出现 request0 拒绝、停泊状态错误或 OIS 包解析失败。临时 OIS 日志及 USB 常亮设置已恢复。
- build43 相机功能交由机主人工验收：拍照、录像、长焦切换和日常使用仍待反馈。自动拍摄尝试不作为完整验收，照片和手机日志不公开。
- 这些检查不代表已验收防抖画质、全部模式或长期可靠性。

## 温控启动项修复

- 移除等待及写入当前内核未导出 boot_complete 属性的两行命令；旧日志该路径等待超时 5002ms。
- 已核对实机 init 脚本与新开机日志，保留原 sconfig 动作，mi_thermald 与 vendor.thermal-hal 均运行；未改内核温控。

## 继续保留的修复及验证

- 蓝牙 offload 能力与 8 个 OEM 基带属性精确标签已实机核验，所采对应写入 AVC 未再出现；沿用现有权限，未新增 allow。蓝牙编解码配置保留，尚未验收耳机播放。
- build37 的 WALT 性能访问、rootTG 提示项、kswapd cpuset、过时 ptt/boot_slpi 启动项修复仍在；9 个实机产物与 OTA 匹配，对应旧报错未再出现。
- 缺失波形的振动 primitive 返回不支持，NOOP 仅延迟，THUD 使用对应普通波形；4 个必需校准波形未齐，暂不声明复合能力。机主反馈普通振动正常，不能称为改善了日常振动。

## 此前版本的实机测试

- build37 实测后置 0.5x/1x/3.2x/5x、前摄拍照与 EXIF 焦距，后摄 1080p 录像均可保存；范围是基础拍摄保存，未验收画质。
- build39 测试程序的 6 项 reapplyDisplayModes 在手机全部通过，程序 SHA256 `2b9a1580e5ae5d00d862314d00636dab374a3e03fced53434b594139e38fdc90`；修正的是 Android 17 测试假设，未因此改生产 SurfaceFlinger。

## 剩余工作

Android 17 Enforcing、蓝牙耳机播放、完整复合触感、更广硬件和长期测试尚未完成。其他 AVC 逐项调查，可选查询、第三方探测、secure-DSP 回退探测及诊断记录不等于独立故障；未批量放行权限。存档恢复的内核/模块完整上游树身份尚未证明，文件哈希不替代来源证明。

Android 16 最终 incremental `1792575161` 为 Enforcing，最终策略 permissive 域为空。迁移早期的 9 个域不是最终 Android 16 遗留问题。
