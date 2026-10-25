# Validation status

**English** | [简体中文](zh-CN/STATUS.md)

Recorded on October 25, 2026. Build numbers below refer to development runs.

## build36 — installed baseline

- Android 17 / SDK 37 / Evolution X 12.3, incremental `1792906981`.
- Upgraded from the final Android 16 build while preserving data. All 34 payload partitions and postinstall checks passed.
- Booted successfully. The device owner reported that most core functions worked. Existing user apps and the data marker were preserved, and KernelSU `su uid=0` was verified.
- Global SELinux mode is Permissive. Android 17 Enforcing validation remains pending.
- OTA SHA256: `9edbe1b9ab25ae2c00f6fbb7386a39d5e542d56b14f11f141be274432490db03`.

## build37 — current source snapshot, not installed

- Incremental `1792914428`. The full build, 50 pinned source inputs, nine changed vendor outputs, and all 34 actual payload partitions passed verification.
- Added performance HAL access to the actual WALT policy label, removed unsupported rootTG hint entries, and created the kswapd cpuset with kernel-thread label filtering for task assignment.
- Removed absent ptt / boot_slpi startup entries while retaining active DSP and sensor configuration.
- Haptics changes return unsupported for missing primitives, treat NOOP as delay only, map THUD to its matching ordinary waveform, and stop advertising composition when four required calibrated primitive waveforms are absent. The owner did not report an ordinary vibration fault; an improvement in everyday haptics has not been demonstrated.
- Fifteen independent new-library checks and six isolated HAL-method checks passed. Temporary kswapd assignment tests were restored afterward. The running HAL was not replaced.
- Camera drivers were unchanged. Existing evidence does not establish a camera defect; targeted lens-switching and shutdown validation remains pending.
- Retains global Permissive mode, KernelSU v3.3.1 LKM, AVB root flags=3, the preserved TWRP image, and kernel 5.15.211.
- Actual policy SHA256: `e328e8bd9cf832d5b261b6fc319d0866e121215efae71dc72e4e119274483276`; declared permissive domains: `[]`.
- OTA SHA256: `d6d2f11d29315cd751a86fcbfad1fdce6082b7b4bf12d7836a1d23d5f335bf5b`.
- **This build has not been installed.** Validation of the new HAL in the installed ROM, overall runtime behavior, and Enforcing mode remains pending.
- Six depmod metadata warnings and a Ninja restat warning are unchanged from build36.

## Android 16 reference

The final Android 16 build, incremental `1792575161`, ran in Enforcing mode and had no declared permissive domains in its final policy. The nine permissive domains seen during early Android 17 migration were an intermediate migration state.
