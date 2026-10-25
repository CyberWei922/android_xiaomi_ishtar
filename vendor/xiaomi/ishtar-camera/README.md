# Ishtar accepted camera03 native integration

The exact camera03 APK and nine system libraries are prebuilt with normal Soong rules.
The tenth library libhidltransport uses a product-selected prebuilt paired with
the platform source module in the global Soong scope. Its accepted bytes are used;
vendor/static platform variants remain source-built.
The APK keeps its accepted platform signature; preprocessed APK checks and ELF checks
are enabled. Unique system library module names preserve existing vendor variants.
The optional miui-cameraopt declaration does not imply that the private MIUI framework is supplied.

21 camera payloads are mapped by accepted-payloads.json. Six existing ODM files
and three adapted HALs are verified unchanged. Three permissions/sysconfig XMLs
and the vendor settings mirror are copied by camera.mk.
Camera properties are merged into the device system property file and vendor allowlist.
The pipeline-delay clamp removal lives in the normal camera-kernel source, not a loader.

No KernelSU module, service.d loader, appops ignore or debugging scripts are installed.
Custom LUT service remains unadapted; full camera modes and new-ROM boot need testing.
