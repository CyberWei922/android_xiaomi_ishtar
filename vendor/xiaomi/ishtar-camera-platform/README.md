# Device-selected accepted libhidltransport

This directory deliberately has global Soong scope to pair the accepted prebuilt
with the existing global platform source module. The ishtar camera product enables
it with its own vendor config boolean. Other products keep the source module;
vendor and static variants continue to use the platform source implementation.
ELF checking remains enabled. No system/libhidl source was edited.
