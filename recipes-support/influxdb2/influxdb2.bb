FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
DESCRIPTION = "Python HF Library for smartflower"
HOMEPAGE = ""
SECTION = "devel/python"
LICENSE = "CLOSED"

SRC_URI = " \
    file://influxdb \
    file://influx \
    file://influxd \
    file://influxdb.conf \
    file://influxdb2-upgrade.sh \
    file://influxd-systemd-start.sh \
    file://influxdb.service \
"

inherit pkgconfig systemd update-rc.d useradd

RDEPENDS:${PN} += "bash"
INSANE_SKIP:${PN} += "already-stripped"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system -d /var/lib/influxdb -m -s /bin/nologin influxdb"

do_compile[noexec] = "1"

do_install:append() {

    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/influx ${D}${bindir}

    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/influxd ${D}${bindir}

    install -d ${D}${datadir}/influxdb
    install -m 0644 ${WORKDIR}/influxdb2-upgrade.sh ${D}${datadir}/influxdb

    install -d ${D}${nonarch_libdir}/influxdb/scripts
    install -m 755 ${WORKDIR}/influxd-systemd-start.sh ${D}${nonarch_libdir}/influxdb/scripts

    install -d ${D}${sysconfdir}/influxdb
    install -m 0644 ${WORKDIR}/influxdb.conf ${D}${sysconfdir}/influxdb
    chown -R root.influxdb ${D}${sysconfdir}/influxdb

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/influxdb ${D}${sysconfdir}/init.d/influxdb

    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}" ] ; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/influxdb.service ${D}${systemd_unitdir}/system/influxdb.service
    fi

}

FILES:${PN} += "\
    ${bindir}/influx \
    ${bindir}/influxd \
    ${datadir}/influxdb/influxdb2-upgrade.sh \
    ${systemd_unitdir}/system/influxdb.service \
    ${nonarch_libdir}/influxdb/scripts/* \
"
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "influxdb"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE:${PN} = "influxdb.service"