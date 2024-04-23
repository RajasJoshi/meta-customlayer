SUMMARY = "Tool to modify display settings by ddc (hdmi)"
HOMEPAGE = "http://www.ddcutil.com"
AUTHOR = "Sanford Rockowitz  <rockowitz@minsoft.com>"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 libusb xrandr"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'udev', d)}"

PACKAGECONFIG:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"

PACKAGECONFIG[x11] = "--enable-x11=yes,--enable-x11=no,"

SRCREV = "39ce66b42305b304816d635b42a5b38ce280e2dd"
SRC_URI = "git://github.com/rockowitz/ddcutil;protocol=git"

PV = "1.1.0+${SRCPV}"

S = "${WORKDIR}/git"

FILES:${PN}:append = " \
    /usr/share/ddcutil/data*  \
"

inherit autotools pkgconfig
