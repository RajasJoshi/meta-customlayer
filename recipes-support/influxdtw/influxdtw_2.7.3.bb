FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
DESCRIPTION = "Python HF Library for smartflower"
HOMEPAGE = ""
SECTION = "devel/python"
LICENSE = "CLOSED"

SRC_URI = " \
    file://influxdb \
    file://influx \
    file://influxd \
    file://influxdb.service \
    file://influxdb2-upgrade.sh \
    file://init.sh \
    file://influxd-systemd-start.sh \
"

RDEPENDS:${PN} += "bash"
INSANE_SKIP:${PN} += "already-stripped"

do_install:append() {

    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/influx ${D}${bindir}

    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/influxd ${D}${bindir}

}

FILES:${PN} += "\
    ${bindir}/influx \
    ${bindir}/influxd \
"
