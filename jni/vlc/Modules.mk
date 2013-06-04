# modules begin
LOCAL_STATIC_LIBRARIES += access_avio_plugin access_demux_avformat_plugin access_http_plugin access_mms_plugin amem_plugin android_surface_plugin audiotrack_android_plugin avcodec_plugin avformat_plugin bandlimited_resampler_plugin converter_fixed_plugin dummy_plugin filesystem_plugin fixed32_mixer_plugin float32_mixer_plugin libasf_plugin realrtsp_plugin simple_channel_mixer_plugin swscale_plugin trivial_mixer_plugin ugly_resampler_plugin yuv2rgb_plugin
# modules end

#LOCAL_STATIC_LIBRARIES += libass libfreetype libiconv libcharset liblive555 libebml libmatroska libdvbpsi
LOCAL_STATIC_LIBRARIES += libiconv libcharset

# it's the only choice
ifeq ($(APP_STL),gnustl_static)
LOCAL_STATIC_LIBRARIES += gnustl_static
endif

LOCAL_WHOLE_STATIC_LIBRARIES += libavformat libavfilter libavcodec libavutil libswscale

