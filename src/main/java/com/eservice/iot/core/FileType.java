package com.eservice.iot.core;

/**
 * @program: yttps_shzx
 * @description: 上传文件类型
 * @author: Mr.Zhang
 * @create: 2019-05-30 16:39
 **/
public enum FileType {
    // 未知
    UNKNOWN,
    // 压缩文件
    ZIP, RAR, _7Z, TAR, GZ, TAR_GZ, BZ2, TAR_BZ2,
    // 位图文件
    BMP, PNG, JPG, JPEG,
    // 矢量图文件
    SVG,
    // 影音文件
    AVI, MP4, MP3, AAR, OGG, WAV, WAVE
}
