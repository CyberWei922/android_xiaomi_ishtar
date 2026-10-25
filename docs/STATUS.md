# Validation status

**English** | [简体中文](zh-CN/STATUS.md)

Recorded on October 25, 2026. Build numbers refer to development runs.

## build43 — installed source snapshot

- Android 17 / SDK 37 / Evolution X 12.3, incremental `1792926429`.
- Six build jobs, existing 25G/27G memory guards and Full LTO. All 53 pinned source inputs and all 34 actual OTA payload partitions passed verification.
- Installed from build41 with data preserved. Installer hashes matched all 34 partitions; seven target boot images were read back. All 24 preinstall user packages and the data marker remained present.
- Global **Permissive**, verified KernelSU v3.3.1 LKM root, AVB root flags=3, preserved TWRP and kernel 5.15.211. Android 17 Enforcing runtime acceptance remains pending.
- Actual policy SHA256: `e328e8bd9cf832d5b261b6fc319d0866e121215efae71dc72e4e119274483276`; declared permissive domains: `[]`. Global Permissive mode is distinct from per-domain policy declarations.
- OTA SHA256: `8b87bbcbdc5b72c771080c1047f83bf6317144401420b8209a3586c5f1ed965a`.

## OIS lifecycle repairs in build43

- A fresh build40 trace reproduced 14 request-ID-zero rejections after queue flush. The frame-number guard now excludes immediate request zero; all original checks for nonzero stale frames remain. Eight C predicate checks passed before driver compilation.
- Normal device release now deinitializes a completed lens-parking worker after the power-down decision. This prevents stale STOP/exit state from carrying into the next acquisition. Existing thread joining and power ownership checks are retained; no register tables or firmware were changed.
- The rebuilt camera module was verified in the actual OTA and on the phone: `ea88ece9db5674f8d657ca3202d914a86db52264d4c2b620559cf1eb912f809b`. The kernel Image remains unchanged.
- 3 consecutive camera open/close cycles passed with active/released clients and a stable provider. Fresh OIS traces showed no request-zero rejection, wrong-state parking or OIS packet-parse failure in those cycles. Temporary OIS logging and USB-awake settings were restored.
- Functional camera testing on build43 is assigned to the owner. Photo/video capture, telephoto switching and normal-use behavior remain pending manual feedback. Automated capture attempts are not accepted as complete coverage; photos and phone logs stay private.
- These checks do not establish stabilization quality, all camera modes, or long-duration reliability.

## Thermal startup repair in build43

- Removed the boot-complete wait/write for an attribute that the current thermal driver does not export. The earlier init log showed a 5002 ms timeout for that path.
- The installed init script and fresh boot log were checked. The sconfig action is retained and both mi_thermald and vendor.thermal-hal remain running. Kernel thermal controls were unchanged.

## Earlier repairs retained and checked

- Exact vendor labels for the Bluetooth offload capability and eight OEM radio properties were verified on the device; their captured property-write denials no longer appeared. Existing domain permissions cover them; no allow rules were added for these property repairs. Bluetooth codec configuration was retained; headset playback is not tested.
- WALT performance access, unsupported rootTG hints, kswapd cpuset setup and obsolete ptt/boot_slpi startup entries were corrected in build37. Nine installed outputs still match the audited OTA and their earlier startup errors were absent.
- Haptic primitives without calibrated data return unsupported, NOOP is delay only and THUD uses its corresponding ordinary waveform. Composition stays unadvertised because four mandatory calibrated waveforms are missing. Ordinary vibration was reported working by the owner; no improvement to everyday haptics is claimed.

## Tests on earlier builds

- Build37: rear 0.5x / 1x / 3.2x / 5x and front photos were captured and saved, with EXIF focal-length checks. A rear 1080p video was recorded and saved. This is basic capture/save coverage, not image-quality acceptance.
- All six reapplyDisplayModes regressions passed on the phone using the build39 test binary, SHA256 `2b9a1580e5ae5d00d862314d00636dab374a3e03fced53434b594139e38fdc90`. The corrections concerned Android 17 test assumptions; production SurfaceFlinger was unchanged by those test corrections.

## Remaining work

- Android 17 Enforcing runtime acceptance, Bluetooth headset playback, complete haptic composition and broader hardware/long-duration testing remain open.
- Other AVCs need individual cause review. Captured records include optional lookups, third-party probes, secure-DSP fallback probes and diagnostics; raw counts are not independent bug counts. No blanket allow rules were generated.
- Full upstream identity of the restored kernel/module trees remains unproven; pinned file and patch hashes do not replace that provenance check.

The final Android 16 build, incremental `1792575161`, ran Enforcing with no declared permissive domains. The nine domains seen early in Android 17 migration were an intermediate migration state.
