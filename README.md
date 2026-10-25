# Xiaomi 13 Ultra / ishtar — Android 17

小米 13 Ultra 的 Android 17 适配集合。一个仓库包含本机设备树、SM8550 公共树、配套 DTS、内核与平台补丁、匹配的调试镜像及接入脚本。开发分支：`android-17`。

硬件配置可以作为其他类原生 ROM 的移植基础；**当前验证的 ROM 是 Evolution X 12.3，不能据此保证所有 ROM 直接编译、开机。** 产品继承、组件、SELinux 和平台补丁需要与目标 ROM 对齐。见 [移植说明](docs/PORTABILITY.md)。

## 当前状态

| 版本 | 构建 | 实机 |
| --- | --- | --- |
| build36 / incremental `1792906981` | 完整 OTA、34 个 payload 分区核验通过 | 已从最终 Android 16 保留数据升级并开机；机主确认大部分基础功能正常；KSU root 已验证 |
| **本仓库当前快照：build37 / `1792914428`** | 完整 OTA、实际 payload 和修复产物核验通过 | **尚未刷入**；新改动与 Enforcing 均未完成实机验收 |

当前是调试配置：全局 **SELinux Permissive**、AVB 根 flags=3、KernelSU v3.3.1 LKM、保留自定义 TWRP，内核 5.15.211。策略中声明为 permissive 的域列表为空，与全局启动模式是两回事。见 [构建与验证状态](docs/STATUS.md)。

机主未发现普通振动故障。37 的振动改动调整缺失 primitive 的返回值及复合能力报告，**没有证明日常触感存在问题或已得到改善**。

## 目录

```text
device/xiaomi/ishtar/                  本机配置
device/xiaomi/sm8550-common/            公共设备配置
kernel/xiaomi/sm8550-devicetrees/       配套 DTS 源码
vendor/xiaomi/                        本次使用的 vendor 构建配方；不含专有载荷
patches/                              14 个组件的配套补丁
assets/                               压缩的匹配 TWRP 与 KSU init_boot 输入
integration/                          提交、文件哈希、依赖和导出检查
manifests/                            本次 EvoX 系统源码固定清单
scripts/                              文件校验与源码接入
docs/                                 构建、移植和验证说明
```

内核完整源码、内核模块完整源码、完整 Android 源码以及 Xiaomi/GMS 专有 APK/so 不复制进此适配仓库；依赖和现有核验范围见 [构建说明](docs/BUILD.md)。

## 使用

**把本仓库克隆到 Android 源码目录之外**，避免同一套 Android.bp 被 Soong 重复扫描。

```bash
git clone -b android-17 https://github.com/CyberWei922/android_xiaomi_ishtar.git
cd android_xiaomi_ishtar
python3 scripts/verify.py
python3 scripts/integrate.py /path/to/android-source
python3 scripts/integrate.py /path/to/android-source --apply
```

先按 [构建说明](docs/BUILD.md) 准备固定版本的依赖。接入脚本默认只检查；已有文件必须与本快照一致，补丁输入必须匹配原始文件哈希，发现冲突会在写入前停止。它不执行编译或刷机。

这是合并布局，不能把整个仓库直接作为 `device/xiaomi/ishtar` 的单个 repo project。接入脚本会把各部分放到 Android 源码中的正确位置。源码内原有 `PORTING.md` 是早期迁移记录，当前状态以本 README 和 `docs/` 为准。

## 来源与许可证

保留原文件的版权和许可证声明；不同上游组件沿用各自许可证，不对整个集合重新许可。来源提交及补丁前后哈希见 `integration/source-lock.json`，完整系统来源见 `manifests/evox17-build37.xml`。内核补丁来源和 TWRP 源码定位的限制见 [来源说明](docs/PROVENANCE.md)。
