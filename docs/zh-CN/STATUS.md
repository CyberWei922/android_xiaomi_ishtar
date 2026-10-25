# 验证状态（2026-10-25）

[English](../STATUS.md) | **简体中文**

## build46：当前已安装的源码快照

- Android 17 / SDK 37 / Evolution X 12.3，incremental `1792936083`。
- 默认 **Enforcing**：bootconfig 和内核命令行均为 `androidboot.selinux=enforcing`，内核参数 `enforcing=1`。正常开机通过，主机没有执行开启模式命令；post-fs-data 保护入口观察到 Enforcing，验证成功后撤销保护，没有回退到 Permissive。
- 核心 PID 稳定观察30秒，崩溃缓冲为空，Enforcing 下 kswapd 实际位于 `/kswapd`。这属于限定时间的启动验证，不能代替完整人工硬件或长期验收。
- 6 并行、Full LTO 及原内存保护；57源码输入、34实际OTA/安装分区哈希、7目标启动镜像回读匹配。
- 从 build45 保留数据安装，25 个用户应用和数据标记保留；KSU v3.3.1 LKM root、TWRP、AVB根flags=3和内核5.15.211保留。
- 只修改本机 BoardConfig 默认启动模式，实际策略与45相同：`3dc7b3c412a8927d6fbb8a9174815dd8a476c2ee7efe8d67c9e1e0c911b10191`，ROM permissive域 `[]`；加载策略仅有KSU上游 `ksu` root域例外。
- OTA SHA256：`cfba3de2630dbea88573d05748166b437ba63cc6b04e58c4eb4570122f976687`。
- 机主已反馈相机、指纹、NFC读卡、蓝牙清单在Permissive下通过，Enforcing下功能复测待完成。本轮未自动操作相机界面。

## build45 的内存回收线程绑定修复

- build44 Enforcing 启动暴露剩余 cpuset 绑定失败。隔离文件描述符后使用 vendor shell 的探针确认，线程名读取成功，但绑定触发 `kernel:process setsched` 拒绝。
- 仅补 `allow vendor_qti_init_shell kernel:process setsched;`，未放宽 `sys_ptrace` 或全局 capability；实际 OTA 策略已核验此规则。
- 一次性 post-fs-data 入口在 Android 核心进程启动前启用 Enforcing，手机正常完成启动，核心 PID 稳定观察 43 秒；**Enforcing 下已实读回收线程位于 `/kswapd`**，崩溃缓冲区为空。
- 独立 180 秒回退计时器保护检查；结束后恢复 Permissive，一次性入口已移除。这不代表从初始策略加载起就 Enforcing，也不代替完整人工硬件或长期验收。

## build44 的精确整改

- 给 kernel-post-boot 补上 `readproc` 附加组。`/proc` 使用 `hidepid=invisible` 时，同一 SELinux 域在 Enforcing 下不带该组扫描失败、带该组成功；没有增加 `sys_ptrace` 权限。新开机扫描已核验；随后 Enforcing 启动发现 kswapd 组为空，隔离探针定位到绑定缺失 kernel:process setsched 权限，转 build45 修复。跨应用 MLS 目录元数据探测会跳过，没有额外放宽 MLS。
- 仅在实际使用旧串口设置的四个老平台查询 `persist.serial.enable`，kalama 不再探测未使用的默认属性。
- 仅补 `esimswitcher_app → gpu_service` 和 `updater_app → system_update_service` 的服务查询权限；实际 payload 策略已核验。更新器规则放在 ROM 平台策略补丁中，设备策略不绑定该 ROM 专有类型。
- 一次性 post-fs-data 启动入口临时切 Enforcing，独立 180 秒计时器负责回退。Android 完成启动后核心 PID 稳定观察 43 秒；入口已自行移除，结束后恢复 Permissive。
- 此检查从 post-fs-data 开始，不代表从首次加载策略起就 Enforcing，也不代表 Enforcing 相机及全部硬件或长期验收。本轮未自动操作相机界面。

## 本轮 OIS 生命周期修复

- build40 新日志复现了刷新队列后 14 次 request ID=0 被错误当作旧帧拒绝。修复后即时请求不参与旧帧序号过滤，非零旧帧原有检查保留；驱动编译前 8 项 C 表达式检查通过。
- 正常释放设备时，在掉电判断后清理已结束的镜头停泊线程状态，避免 STOP/退出状态带入下一次打开；保留原有线程等待及电源归属判断，未改寄存器表或固件。
- 实际 OTA 和手机中的新 camera.ko 均核验为 `ea88ece9db5674f8d657ca3202d914a86db52264d4c2b620559cf1eb912f809b`；内核 Image 未改。
- 连续 3 次开关相机，活动客户端及释放正常，provider 稳定；新 OIS 日志未出现 request0 拒绝、停泊状态错误或 OIS 包解析失败。临时 OIS 日志及 USB 常亮设置已恢复。
- 机主已反馈 build43 相机测试通过、未发现问题；这是 Permissive 下的人工功能验收，Enforcing 下测试另行进行。照片和手机日志不公开。
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

- 机主反馈指纹、NFC读卡和蓝牙清单在Permissive下通过，Enforcing下待复测。

## 剩余工作

- 完整启动日志还捕获了早期模块加载器的块设备目录探测和 toolbox 根目录探测拒绝。vendor 模块加载器退出状态为 0，与 build45 相同的 103 条模块加载成功路径均出现；具体探测来源仍待归因，没有增加额外策略权限。

- MTD 的 DAC/属性探测、RIL 默认属性读取、音频调试/启动状态探测和相机空服务名查询仍需逐因归属；这些记录尚未证明对应硬件故障，没有增加宽泛权限。

Enforcing 完整硬件验收、完整复合触感、更广硬件和长期测试尚未完成。eSE 服务在 Permissive 下也初始化失败，请求缺失的 IEsePowerManager 接口并报 GPQESE_CMD_OPEN 失败，仍需匹配 ST NFC/eSE 原厂接口；没有靠宽泛 allow 或移除声明隐藏错误。其他 AVC 逐项调查，可选查询、第三方探测、secure-DSP 回退探测及诊断记录不等于独立故障；未批量放行权限。存档恢复的内核/模块完整上游树身份尚未证明，文件哈希不替代来源证明。

Android 16 最终 incremental `1792575161` 为 Enforcing，最终策略 permissive 域为空。迁移早期的 9 个域不是最终 Android 16 遗留问题。
