SUMMARY = "WebKit web rendering engine for the GTK+ platform"
HOMEPAGE = "http://www.webkitgtk.org/"
BUGTRACKER = "http://bugs.webkit.org/"

LICENSE = "BSD & LGPLv2+"
LIC_FILES_CHKSUM = "file://Source/JavaScriptCore/COPYING.LIB;md5=d0c6d6397a5d84286dda758da57bd691 \
                    file://Source/WebKit/LICENSE;md5=4646f90082c40bcf298c285f8bab0b12 \
                    file://Source/WebCore/LICENSE-APPLE;md5=4646f90082c40bcf298c285f8bab0b12 \
		    file://Source/WebCore/LICENSE-LGPL-2;md5=36357ffde2b64ae177b2494445b79d21 \
		    file://Source/WebCore/LICENSE-LGPL-2.1;md5=a778a33ef338abbaf8b8a7c36b6eec80 \
		   "

SRC_URI = "\
  http://www.webkitgtk.org/releases/${BPN}-${PV}.tar.xz \
  file://0001-This-patch-fixes-a-command-line-that-is-too-long-ove.patch \
  file://gcc5.patch \
  "
SRC_URI[md5sum] = "df79991848a5096d3a75289ebce547ae"
SRC_URI[sha256sum] = "3d1f0c534935f43fd74df90f2648fcee672d60f1f57a30fa557a77891ae04d20"

inherit cmake lib_package pkgconfig perlnative pythonnative distro_features_check

# depends on libxt
REQUIRED_DISTRO_FEATURES = "x11"

DEPENDS = "zlib enchant libsoup-2.4 curl libxml2 cairo libxslt libxt libidn gnutls \
           gtk+ gtk+3 gstreamer1.0 gstreamer1.0-plugins-base flex-native gperf-native sqlite3 \
	   pango icu bison-native gnome-common gawk intltool-native libwebp \
	   atk udev harfbuzz jpeg libpng pulseaudio librsvg libtheora libvorbis libxcomposite libxtst \
	   ruby-native libsecret libnotify gstreamer1.0-plugins-bad \
          "
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)}"

EXTRA_OECMAKE = " \
		-DPORT=GTK \
		-DCMAKE_BUILD_TYPE=Release \
		-DENABLE_INTROSPECTION=False \
		-DENABLE_MINIBROWSER=True \
	        ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '-DENABLE_WEBGL=True', '-DENABLE_WEBGL=False', d)} \
		"

# Javascript JIT is not supported on powerpc
EXTRA_OECMAKE_append_powerpc = " -DENABLE_JIT=False "
EXTRA_OECMAKE_append_powerpc64 = " -DENABLE_JIT=False "

# ARM JIT code does not build on ARMv5/6 anymore, apparently they test only on v7 onwards
EXTRA_OECMAKE_append_armv5 = " -DENABLE_JIT=False "
EXTRA_OECMAKE_append_armv6 = " -DENABLE_JIT=False "

# binutils 2.25.1 has a bug on aarch64:
# https://sourceware.org/bugzilla/show_bug.cgi?id=18430
EXTRA_OECMAKE_append_aarch64 = " -DUSE_LD_GOLD=False "

# JIT not supported on MIPS either
EXTRA_OECMAKE_append_mips = " -DENABLE_JIT=False "
EXTRA_OECMAKE_append_mips64 = " -DENABLE_JIT=False "

FILES_${PN} += "${libdir}/webkit2gtk-4.0/injected-bundle/libwebkit2gtkinjectedbundle.so"
FILES_${PN}-dbg += "${libdir}/webkit2gtk-4.0/injected-bundle/.debug/libwebkit2gtkinjectedbundle.so"
FILES_${PN}-dbg += "${libdir}/webkitgtk/webkit2gtk-4.0/.debug/*"

