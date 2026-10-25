# SM8550 common / ishtar Android17 candidate

Base: ishtar-aosp lineage-24.0 668c6497bb6885f193ff27a26af4c1e228f4098d.
Only ishtar is in scope. Full upstream history and file licenses are retained.

Selected24 updates include append-only boot-header args, firmware fallback in
source recovery init, optional media profile property, Vulkan HWUI, eSIM coredomain,
standard USB-C labels, and removal of obsolete restart resource overrides.

Compatibility decisions for the accepted .307 vendor/kernel:
- Keep legacy QTI Power HAL, perf/PASR setup, matching SELinux and module lists.
  Do not build lineage-libperfmgr/libqti-perfd-client alongside the legacy blobs.
  The upstream powerhint reference remains available but is not installed.
- Keep16 proprietary-files/extract recipe and QSPM VINTF contract.24 replaces
  performance blobs and imports WFD from another device; that needs a separately
  validated vendor migration. No .311/.304 vendor replacement is performed.
- Keep VENDOR_SECURITY_PATCH2026-02-01 from the accepted baseline. A newer public
  tree's date does not upgrade these binary payloads.
- Carry accepted property ownership, camera allowlist and init relabel repairs.

This is source preparation. Android17 Soong/policy/VINTF/ELF and runtime checks
are pending. In particular, WiFi Display and legacy power need validation.

The pinned TWRP recovery does not consume the source recovery init changes;
keep the accepted kernel firmware-fallback command line until17 recovery tests.
