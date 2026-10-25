# Ishtar / Android17 / Evolution X12 WIP

This branch starts at the original ishtar-aosp lineage-23.2 history and carries
accepted build1792575161 source fixes. It is a candidate, without an Android17
build or phone validation. Scope: Xiaomi13 Ultra ishtar only.

Use the sibling sm8550-common branch and integration repository together.
Product remains lineage_ishtar: official EvoX cnb installs vendor_evolution at
vendor/lineage. Keep launch API33, .307 vendor identity and ST NFC.

The recovery image is an ignored local input, pinned to SHA256
c39e0236d4aac787e59827f835d58ec3af127357bcd576fb2b66da72c2c4057e.
Install the integration build/make patch before evaluating BoardConfig. It copies
and verifies this exact image; boot, touch, decryption and OTA on17 are untested.
Do not upload the image with the source tree. Firmware and other proprietary
payloads must be obtained separately with appropriate redistribution rights.

Local changes: camera03 routes/policy/defaults, .307 Cirrus audio routing,
5500mAh overlay, radio restart handoff, narrow vendor labels and Enforcing.
This port moves UDFPS/NFC product flags before common evaluation and removes
a duplicated vibrator copy block. No fingerprint or vendor SPL upgrade is claimed.
