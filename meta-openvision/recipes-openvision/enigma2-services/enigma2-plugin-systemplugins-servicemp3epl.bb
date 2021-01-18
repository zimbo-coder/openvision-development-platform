DESCRIPTION = "servicemp3 and libeplayer backend for enigma2"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PROVIDES += "virtual/enigma2-mediaservice"
RPROVIDES_${PN} += "virtual/enigma2-mediaservice"

CXXFLAGS_append_sh4 = " -std=c++11 -fPIC -fno-strict-aliasing"

GST_BASE_RDEPS = "\
	gstreamer1.0-plugins-base-alsa \
	gstreamer1.0-plugins-base-app \
	gstreamer1.0-plugins-base-audioconvert \
	gstreamer1.0-plugins-base-audioresample \
	gstreamer1.0-plugins-base-audiorate \
	gstreamer1.0-plugins-base-videoconvert \
	gstreamer1.0-plugins-base-ogg \
	gstreamer1.0-plugins-base-playback \
	gstreamer1.0-plugins-base-subparse \
	gstreamer1.0-plugins-base-typefindfunctions \
	gstreamer1.0-plugins-base-vorbis \
	gstreamer1.0-plugins-base-rawparse \
	"

GST_GOOD_RDEPS = "\
	gstreamer1.0-plugins-good-apetag \
	gstreamer1.0-plugins-good-audioparsers \
	gstreamer1.0-plugins-good-autodetect \
	gstreamer1.0-plugins-good-avi \
	gstreamer1.0-plugins-good-flac \
	gstreamer1.0-plugins-good-flv \
	gstreamer1.0-plugins-good-icydemux \
	gstreamer1.0-plugins-good-id3demux \
	gstreamer1.0-plugins-good-isomp4 \
	gstreamer1.0-plugins-good-matroska \
	gstreamer1.0-plugins-good-mpg123 \
	gstreamer1.0-plugins-good-rtp \
	gstreamer1.0-plugins-good-rtpmanager \
	gstreamer1.0-plugins-good-rtsp \
	gstreamer1.0-plugins-good-soup \
	gstreamer1.0-plugins-good-udp \
	gstreamer1.0-plugins-good-wavparse \
	"

GST_BAD_RDEPS = "\
	gstreamer1.0-plugins-bad-autoconvert \
	gstreamer1.0-plugins-bad-dash \
	gstreamer1.0-plugins-bad-mpegpsdemux \
	gstreamer1.0-plugins-bad-mpegtsdemux \
	gstreamer1.0-plugins-bad-rtmp2 \
	gstreamer1.0-plugins-bad-smoothstreaming \
	gstreamer1.0-plugins-bad-hls \
	gstreamer1.0-plugins-bad-videoparsersbad \
	"

GST_UGLY_RDEPS = "\
	gstreamer1.0-plugins-ugly-asf \
	gstreamer1.0-plugins-ugly-dvdsub \
	"

DEPENDS = "\
	enigma2 \
	${@bb.utils.contains("MACHINE_FEATURES", "libeplayer", "libeplayer3", "gstreamer1.0-plugins-base gstreamer1.0", d)} \
	${PYTHONNAMEONLY} \
	"

GST_NEEDED_PLUGINS = "\
	${@bb.utils.contains("MACHINE_FEATURES", "smallflash", "", "\
	${GST_BASE_RDEPS} \
	${GST_GOOD_RDEPS} \
	${GST_BAD_RDEPS} \
	${GST_UGLY_RDEPS} \
	", d)} \
	"

RDEPENDS_${PN} = "\
	enigma2 \
	${@bb.utils.contains("MACHINE_FEATURES", "libeplayer", "libeplayer3", "\
	glib-networking \
	${GSTVNAME}-plugin-subsink \
	virtual/${GSTVNAME}-dvbmediasink \
	${GST_NEEDED_PLUGINS} \
	", d)} \
	"

SRC_URI = "git://github.com/OpenVisionE2/servicemp3epl.git;branch=master"

S = "${WORKDIR}/git"

inherit autotools gitpkgv ${PYTHONNAMEONLY}native pkgconfig rm_python_pyc compile_python_pyo no_python_src

PV = "git${SRCPV}"
PKGV = "git${GITPKGV}"

EXTRA_OECONF = "\
	${@bb.utils.contains("MACHINE_FEATURES", "libeplayer", "--enable-libeplayer3 --disable-gstreamer", "--enable-gstreamer --with-gstversion=1.0 --disable-libeplayer3", d)} \
	BUILD_SYS=${BUILD_SYS} \
	HOST_SYS=${HOST_SYS} \
	STAGING_INCDIR=${STAGING_INCDIR} \
	STAGING_LIBDIR=${STAGING_LIBDIR} \
	--with-boxtype=${MACHINE} \
	--with-boxbrand=${BOX_BRAND} \
	--with-stbplatform=${STB_PLATFORM} \
	"

FILES_${PN} = "\
	${libdir}/enigma2/python/Plugins/SystemPlugins/ServiceMP3/*.${PYTHONEXTENSION} \
	${libdir}/enigma2/python/Plugins/SystemPlugins/ServiceMP3/servicemp3.so"

FILES_${PN}-dev = "\
	${libdir}/enigma2/python/Plugins/SystemPlugins/ServiceMP3/servicemp3.la"
