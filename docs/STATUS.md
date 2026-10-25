# Validation status

**English** | [简体中文](zh-CN/STATUS.md)

Recorded on October 25, 2026. Build numbers refer to development runs.

## build46 — installed source snapshot

- Android 17 / SDK 37 / Evolution X 12.3, incremental `1792936083`.
- Default **Enforcing** selected by `androidboot.selinux=enforcing` in bootconfig and kernel command line, plus `enforcing=1`. Normal boot completed with no host-side enable command. The post-fs-data guard observed Enforcing; it was disarmed after verification and did not restore Permissive.
- Core PIDs were stable over the 30-second observation, the crash buffer was empty, and kswapd was verified in `/kswapd` under Enforcing. These are bounded startup checks; complete manual hardware and long-duration acceptance remain pending.
- Six jobs, Full LTO and existing memory guards. All 57 source pins, 34 OTA payload/installer partition hashes and seven target boot readbacks passed verification.
- Installed from build45 preserving 25 user applications and the data marker. KSU v3.3.1 LKM root, TWRP, AVB root flags=3 and kernel 5.15.211 remain present.
- Only the device BoardConfig boot mode changed. Actual ROM policy is unchanged from45: `3dc7b3c412a8927d6fbb8a9174815dd8a476c2ee7efe8d67c9e1e0c911b10191`, declared permissive domains `[]`. The live policy has only the upstream KSU `ksu` root-domain exception.
- OTA SHA256: `cfba3de2630dbea88573d05748166b437ba63cc6b04e58c4eb4570122f976687`.
- Owner camera, fingerprint, NFC reading and Bluetooth checklist passed in Permissive. The Enforcing functional checklist remains pending. No camera UI automation was used.

## Kernel reclaim attachment repair in build45

- The build44 Enforcing startup exposed a remaining cpuset attachment failure. A vendor-shell probe with isolated file descriptors confirmed that reading the reclaim thread name succeeded but task attachment failed with `kernel:process setsched` denied.
- Added exactly `allow vendor_qti_init_shell kernel:process setsched;`. No `sys_ptrace` or global capability grant was added. The exact allow was verified in the actual OTA policy.
- A one-use post-fs-data hook enabled Enforcing before the Android core processes started. The phone reached boot completion and core PIDs stayed stable for 43 seconds. **The reclaim thread was verified in `/kswapd` while Enforcing.** The crash buffer was empty.
- An independent 180-second rollback protected the check. Permissive was restored and the one-use trigger was removed. This does not establish Enforcing from the initial policy load, complete manual hardware acceptance, or long-duration reliability.

## Precise Enforcing readiness repairs in build44

- Added the `readproc` supplementary group to kernel-post-boot. On a proc mount with `hidepid=invisible`, the same SELinux domain failed the kernel-thread scan without this group and passed with it under Enforcing. No `sys_ptrace` allow was added. The repaired scan was verified. The first early Enforcing boot left the kswapd group empty; a follow-up isolated probe identified a missing kernel process setsched permission for cpuset attachment, addressed in build45. Cross-category app directory metadata probes are skipped and were not granted extra MLS access.
- Restricted the obsolete `persist.serial.enable` query to the four legacy platforms that consume it. Kalama no longer probes this default property for an unused console setting.
- Added only `service_manager find` for `esimswitcher_app` to `gpu_service`, and `updater_app` to `system_update_service`. Both exact rules were verified in the actual payload policy. The updater rule is in the ROM policy patch, keeping ROM-specific types out of the device policy.
- A one-use post-fs-data hook switched to Enforcing with an independent 180-second rollback. Android reached boot completion and core PIDs remained stable for 43 seconds after completion. The hook removed itself and Permissive was restored.
- This startup check begins after first-stage init. It does not establish a full Enforcing boot from the initial policy load, camera acceptance under Enforcing, or long-duration hardware reliability. No camera UI automation was used in this round.

## OIS lifecycle repairs in build43

- A fresh build40 trace reproduced 14 request-ID-zero rejections after queue flush. The frame-number guard now excludes immediate request zero; all original checks for nonzero stale frames remain. Eight C predicate checks passed before driver compilation.
- Normal device release now deinitializes a completed lens-parking worker after the power-down decision. This prevents stale STOP/exit state from carrying into the next acquisition. Existing thread joining and power ownership checks are retained; no register tables or firmware were changed.
- The rebuilt camera module was verified in the actual OTA and on the phone: `ea88ece9db5674f8d657ca3202d914a86db52264d4c2b620559cf1eb912f809b`. The kernel Image remains unchanged.
- 3 consecutive camera open/close cycles passed with active/released clients and a stable provider. Fresh OIS traces showed no request-zero rejection, wrong-state parking or OIS packet-parse failure in those cycles. Temporary OIS logging and USB-awake settings were restored.
- The owner reported that the camera tests on build43 passed without problems. This is owner functional acceptance in Permissive; camera testing under Enforcing remains separate. Photos and phone logs stay private.
- These checks do not establish stabilization quality, all camera modes, or long-duration reliability.

## Thermal startup repair in build43

- Removed the boot-complete wait/write for an attribute that the current thermal driver does not export. The earlier init log showed a 5002 ms timeout for that path.
- The installed init script and fresh boot log were checked. The sconfig action is retained and both mi_thermald and vendor.thermal-hal remain running. Kernel thermal controls were unchanged.

## Earlier repairs retained and checked

- Exact vendor labels for the Bluetooth offload capability and eight OEM radio properties were verified on the device; their captured property-write denials no longer appeared. Existing domain permissions cover them; no allow rules were added for these property repairs. Bluetooth codec configuration was retained; the owner later reported the Permissive Bluetooth checklist passed.
- WALT performance access, unsupported rootTG hints, kswapd cpuset setup and obsolete ptt/boot_slpi startup entries were corrected in build37. Nine installed outputs still match the audited OTA and their earlier startup errors were absent.
- Haptic primitives without calibrated data return unsupported, NOOP is delay only and THUD uses its corresponding ordinary waveform. Composition stays unadvertised because four mandatory calibrated waveforms are missing. Ordinary vibration was reported working by the owner; no improvement to everyday haptics is claimed.

## Tests on earlier builds

- Build37: rear 0.5x / 1x / 3.2x / 5x and front photos were captured and saved, with EXIF focal-length checks. A rear 1080p video was recorded and saved. This is basic capture/save coverage, not image-quality acceptance.
- All six reapplyDisplayModes regressions passed on the phone using the build39 test binary, SHA256 `2b9a1580e5ae5d00d862314d00636dab374a3e03fced53434b594139e38fdc90`. The corrections concerned Android 17 test assumptions; production SurfaceFlinger was unchanged by those test corrections.

- The owner reported fingerprint, NFC reading and Bluetooth checklist tests passed in Permissive; these need a repeat under Enforcing.

## Remaining work

- Full-startup logging also captured an early module-loader block-directory probe and toolbox root-directory probes. The vendor module loader exited successfully and the same 103 module-success paths appeared as in build45; exact probe attribution remains open. No extra policy permissions were added.

- MTD DAC/property probes, RIL default-property reads, audio diagnostic/boot-status probes and the camera empty-service lookup still need exact cause attribution. These observations do not establish corresponding hardware failures, and no broad permissions were granted.

- Manual Enforcing hardware acceptance, complete haptic composition and broader hardware/long-duration testing remain open.
- The eSE service fails initialization in Permissive as well, requesting missing IEsePowerManager interfaces and reporting GPQESE_CMD_OPEN failure. Matching stock ST NFC/eSE interfaces still need investigation; no broad allow or manifest removal was used to hide it.
- Other AVCs need individual cause review. Captured records include optional lookups, third-party probes, secure-DSP fallback probes and diagnostics; raw counts are not independent bug counts. No blanket allow rules were generated.
- Full upstream identity of the restored kernel/module trees remains unproven; pinned file and patch hashes do not replace that provenance check.

The final Android 16 build, incremental `1792575161`, ran Enforcing with no declared permissive domains. The nine domains seen early in Android 17 migration were an intermediate migration state.
