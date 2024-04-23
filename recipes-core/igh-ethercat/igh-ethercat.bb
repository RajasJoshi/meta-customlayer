# Copyright 2021 NXP

PV = "1.5.2"
DESCRIPTION = "IgH EtherCAT Master for Linux"
HOMEPAGE = "https://etherlab.org/en/ethercat/index.php"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SRC_URI = "file://ethercat-${PV}.tar.bz2"
SRC_URI[md5sum] = "6b4001f8d975865d74a0b108b3bdda3d"

SRC_URI:append = "\
    file://0001-Fixed-compilation-error-for-the-EtherCat-drivers.patch \
    file://0002-Modify-the-example-code.patch \
    file://0003-Fixed-compilation-error-for-the-IGH-EtherCAT.patch \
    file://0004-replace-the-init_timer-with-timer_setup-function.patch \
    file://0005-igh-ethernet-change-the-type-of-eccdev_vma_fault-to-.patch \
    file://0006-examples-user-simplify-the-Igh-test-case.patch \
    file://0007-configure-Fix-the-subdir-objects-error.patch \
    file://0008-master-master-fix-the-issue-of-sched_setscheduler-un.patch \
    file://0011-Fix-ethercat-tool-compilation.patch \
    file://ethercat.conf \
"
S = "${WORKDIR}/ethercat-${PV}"

PACKAGECONFIG ??= "generic"

PACKAGECONFIG[generic] = "--enable-generic,--disable-generic,"
PACKAGECONFIG[8139too] = "--enable-8139too,--disable-8139too,"
PACKAGECONFIG[e100]    = "--enable-e100,--disable-e100,"
PACKAGECONFIG[e1000]   = "--enable-e1000,--disable-e1000,"
PACKAGECONFIG[e1000e]  = "--enable-e1000e,--disable-e1000e,"
PACKAGECONFIG[r8169]   = "--enable-r8169,--disable-r8169,"
PACKAGECONFIG[backup]   = "--with-devices=2,--with-devices=1,"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

inherit autotools-brokensep pkgconfig module-base

EXTRA_OECONF += "--with-linux-dir=${STAGING_KERNEL_BUILDDIR} --enable-8139too=no"
EXTRA_OECONF += "--with-module-dir=kernel/ethercat"

do_compile:append() {
	oe_runmake modules
}

inherit systemd
do_install:append() {
	oe_runmake MODLIB=${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION} modules_install
	
    install -d ${D}${sysconfdir}/
    install -m 755 ${WORKDIR}/ethercat.conf ${D}${sysconfdir}/

}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/* ${bindir_native}/* ${sbindir_native}/* ${libdir_native}/* ${sysconfdir_native}/* ${sysconfdir}/ethercat.conf"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
