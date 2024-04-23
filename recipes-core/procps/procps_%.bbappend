SRC_URI += "file://sysctl.conf \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_install:append () { 
	install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}/sysctl.conf
}