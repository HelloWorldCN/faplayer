#!/bin/sh

echo "===>start cross-compile ..."

#ffmpeg
echo "===>start compiling ffmpeg"

if [ ! -d "android/ffmpeg" ]; then
	tar xvf tarballs/ffmpeg-0.10.7.tar.bz2
	mv ffmpeg-0.10.7/ android/ffmpeg
	cp patch/ffmpeg/ffmpeg-0.10.7.patch android/ffmpeg/
	cd android/ffmpeg
	patch -p1 < ffmpeg-0.10.7.patch
	./configure  --prefix=../../../arm-linux-androideabi --enable-cross-compile --target-os=linux --arch=arm --cpu=cortex-a8 --enable-neon --cross-prefix=arm-linux-androideabi-
else
	cd android/ffmpeg
fi

make -j4
make install
echo "===>compiling ffmpeg down"
