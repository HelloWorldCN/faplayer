#!/bin/sh

echo "===>start cross-compile ..."

#ffmpeg
echo "===>start compiling ffmpeg"

if [ ! -d "android" ]; then
	mkdir android
fi

if [ ! -d "android/ffmpeg-0.8.14" ]; then
	tar xf tarballs/ffmpeg-0.8.14.tar.bz2
	mv ffmpeg-0.8.14 android/
	cp patch/ffmpeg/ffmpeg-0.8.14.patch android/ffmpeg-0.8.14
	cd android/ffmpeg-0.8.14
	patch -p1 < ffmpeg-0.8.14.patch
	rm ffmpeg-0.8.14.patch
	./configure  --prefix=../../../arm-linux-androideabi --enable-cross-compile --target-os=linux --arch=arm --cpu=cortex-a8 --enable-neon --cross-prefix=arm-linux-androideabi- --disable-ffmpeg --disable-ffplay --disable-ffprobe --disable-ffserver
else
	cd android/ffmpeg-0.8.14
fi

make -j4
make install
echo "===>compiling ffmpeg down"