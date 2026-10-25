# Owner-accepted Recovery touch repair (2026-10-17)

prebuilt-recovery.img is pinned to SHA256 c39e0236d4aac787e59827f835d58ec3af127357bcd576fb2b66da72c2c4057e.
Size: 104857600 bytes. It preserves TWRP 3.7.1_12-Xiaomi13Ultra_v9.0_A15 provenance.
The owner flashed this image and confirmed the touchscreen repair.

Changes are limited to ordinary PM8008 module loading, the matching supply module,
and ROM firmware st_fts_m1.ftb (0015/0018). Other 4033 CPIO entries are preserved.
The image has no independent kernel; the accepted runtime kernel is
5.15.211-g34eaaccbb3d7-dirty. Recheck compatibility after kernel changes.

Data decryption, OTA, repeated-entry and complete rescue behavior need separate tests.
The build checks the exact SHA and existing AVB integrity; target-files and final
package must carry this prebuilt, without regenerating it from the recovery tree.
Prior image b7577724... is preserved in this integration's before/ backup.
Full source evidence: ishtar-native-integration-20261017-01 under /mnt/android-download/control.
No flashing or phone operation is part of this source integration.
