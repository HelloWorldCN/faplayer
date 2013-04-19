#!/bin/sh

##@{	add by juguofeng	2013-04-18

COLOR_RS="\033[31;49;1m"
COLOR_BS="\033[34;49;1m"
COLOR_ED="\033[39;49;0m"

DEFAULT="sh fa_build.sh # <-- this will build for cortex-a8 with neon by default"
ARMEABI="sh fa_build.sh ABI=armeabi # <-- this will build for arm1136j-s with softfp"
ARMEABI_V7A="sh fa_build.sh ABI=armeabi-v7a FPU=vfpv3-d16 TUNE=cortex-a9 # <-- Xoom"

usage() {
	echo "======================================================================"
	echo $COLOR_RS"please input the correct <os-option number>"$COLOR_ED
	echo ""
	echo "Usage: sh $0 <os-option number>"
	echo "=========>"$COLOR_BS"[1 -> DEFAULT]"$COLOR_ED "or" $COLOR_BS"[2 -> ARMEABI]"$COLOR_ED "or" $COLOR_BS"[3 -> ARMEABI_V7A]"$COLOR_ED
	echo ""
	echo "meaning:"
	echo "["$COLOR_BS"  DEFAULT  "$COLOR_ED"]"$DEFAULT
	echo "["$COLOR_BS"  ARMEABI  "$COLOR_ED"]"$ARMEABI
	echo "["$COLOR_BS"ARMEABI_V7A"$COLOR_ED"]"$ARMEABI_V7A
	echo "======================================================================"
	exit 1
}

if [ $# -ne 1 ]
then
	usage $*
fi

if [ "DEFAULT" = "$1" -o "1" = "$1" ]
then
	echo ""
	echo $COLOR_BS"======>compiling for Android ["$COLOR_BS"  DEFAULT  ]..."$COLOR_ED
	echo ""
	sh fa_build.sh
	exit
fi
	
if [ "ARMEABI" = "$1" -o "2" = "$1" ]
then
	echo ""
	echo $COLOR_BS"======>compiling for Android ["$COLOR_BS"  ARMEABI  ]..."$COLOR_ED
	echo ""
	sh fa_build.sh ABI=armeabi
	exit
fi

if [ "ARMEABI_V7A" = "$1" -o "3" = "$1" ]
then
	echo ""
	echo $COLOR_BS"======>compiling for Android ["$COLOR_BS"ARMEABI_V7A]..."$COLOR_ED
	echo ""
	sh fa_build.sh ABI=armeabi-v7a FPU=vfpv3-d16 TUNE=cortex-a9
	exit
fi
	
usage $*


##@}	end		###
