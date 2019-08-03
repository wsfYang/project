package com.eservice.iot.util;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * @program: yttps_shzx
 * @description: 解压zip/rar工具类
 * @author: Mr.Zhang
 * @create: 2019-05-30 11:16
 **/
public class UnPackageUtil {

    private final static Logger logger = LoggerFactory.getLogger(UnPackageUtil.class);

    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    public static void unPackZip(File zipFile, String password, String destPath) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset(getEncoding(zipFile));
            logger.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password);
            }
        } catch (Exception e) {
            logger.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
        }
    }

    /**
     *  构建目录
     * @param outputDir 输出目录
     * @param subDir 子目录
     */
    public static void createDirectory(String outputDir, String subDir){
        File file = new File(outputDir);
        //子目录不为空
        if(!(subDir == null || subDir.trim().equals(""))) {
            file = new File(outputDir + File.separator + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

    /**
     * 解压缩zip文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressZip(File file, String targetPath){

    }
    /**
     * 解压缩7z文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompress7Z(File file, String targetPath){
        SevenZFile sevenZFile = null;
        OutputStream outputStream = null;
        try {
            sevenZFile = new SevenZFile(file);
            // 创建输出目录
            createDirectory(targetPath, null);
            SevenZArchiveEntry entry;

            while((entry = sevenZFile.getNextEntry()) != null){
                if(entry.isDirectory()){
                    // 创建子目录
                    createDirectory(targetPath, entry.getName());
                }else{
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int len = 0;
                    byte[] b = new byte[2048];
                    while((len = sevenZFile.read(b)) != -1){
                        outputStream.write(b, 0, len);
                    }
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            try {
                if(sevenZFile != null){
                    sevenZFile.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩RAR文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressRAR(File file, String targetPath){
        Archive archive = null;
        OutputStream outputStream = null;
        try {
            archive = new Archive(file);
            FileHeader fileHeader;
            // 创建输出目录
            createDirectory(targetPath, null);
            while( (fileHeader = archive.nextFileHeader()) != null){
                if(fileHeader.isDirectory()){
                    // 创建子目录
                    createDirectory(targetPath, fileHeader.getFileNameString().trim());
                }else{
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + fileHeader.getFileNameString().trim()));
                    archive.extractFile(fileHeader, outputStream);
                }
            }
        } catch (RarException | IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if(archive != null){
                    archive.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩tar文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressTar(File file, String targetPath ){
        FileInputStream fis = null;
        OutputStream fos = null;
        TarInputStream tarInputStream = null;
        try {
            fis = new FileInputStream(file);
            tarInputStream = new TarInputStream(fis, 1024 * 2);
            // 创建输出目录
            createDirectory(targetPath, null);
            TarEntry entry = null;
            while(true){
                entry = tarInputStream.getNextEntry();
                if( entry == null){
                    break;
                }
                if(entry.isDirectory()){
                    // 创建子目录
                    createDirectory(targetPath, entry.getName());
                }else{
                    fos = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int count;
                    byte []data = new byte[2048];
                    while ((count = tarInputStream.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }    finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(tarInputStream != null){
                    tarInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩bz2文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressBZ2(File file, String targetPath){
        FileInputStream fis = null;
        OutputStream fos = null;
        BZip2CompressorInputStream bis = null;
        String suffix = ".bz2";
        try {
            fis = new FileInputStream(file);
            bis = new BZip2CompressorInputStream(fis);
            // 创建输出目录
            createDirectory(targetPath, null);
            File tempFile = new File(targetPath + File.separator + file.getName().replace(suffix, ""));
            fos = new FileOutputStream(tempFile);

            int count;
            byte data[] = new byte[2048];
            while ((count = bis.read(data)) != -1) {
                fos.write(data, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩tar.bz2文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressTarBz2(File file, String targetPath){
        FileInputStream fis = null;
        OutputStream fos = null;
        BZip2CompressorInputStream bis = null;
        TarInputStream tis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BZip2CompressorInputStream(fis);
            tis = new TarInputStream(bis, 1024 * 2);
            // 创建输出目录
            createDirectory(targetPath, null);
            TarEntry entry;
            while((entry = tis.getNextEntry()) != null){
                if(entry.isDirectory()){
                    // 创建子目录
                    createDirectory(targetPath, entry.getName());
                }else{
                    fos = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int count;
                    byte data[] = new byte[2048];
                    while ((count = tis.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
                if(tis != null){
                    tis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩gz文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressGz(File file, String targetPath){
        FileInputStream  fileInputStream = null;
        GZIPInputStream gzipIn = null;
        OutputStream out = null;
        String suffix = ".gz";
        try {
            fileInputStream = new FileInputStream(file);
            gzipIn = new GZIPInputStream(fileInputStream);
            // 创建输出目录
            createDirectory(targetPath, null);

            File tempFile = new File(targetPath + File.separator + file.getName().replace(suffix, ""));
            out = new FileOutputStream(tempFile);
            int count;
            byte data[] = new byte[2048];
            while ((count = gzipIn.read(data)) != -1) {
                out.write(data, 0, count);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if(out != null){
                    out.close();
                }
                if(gzipIn != null){
                    gzipIn.close();
                }
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩tar.gz文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressTarGz(File file, String targetPath){
        FileInputStream  fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        GZIPInputStream gzipIn = null;
        TarInputStream tarIn = null;
        OutputStream out = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            gzipIn = new GZIPInputStream(bufferedInputStream);
            tarIn = new TarInputStream(gzipIn, 1024 * 2);

            // 创建输出目录
            createDirectory(targetPath, null);

            TarEntry entry = null;
            while((entry = tarIn.getNextEntry()) != null){
                // 是目录
                if(entry.isDirectory()){
                    // 创建子目录
                    createDirectory(targetPath, entry.getName());
                }else{
                    // 是文件
                    File tempFIle = new File(targetPath + File.separator + entry.getName());
                    createDirectory(tempFIle.getParent() + File.separator, null);
                    out = new FileOutputStream(tempFIle);
                    int len =0;
                    byte[] b = new byte[2048];

                    while ((len = tarIn.read(b)) != -1){
                        out.write(b, 0, len);
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                if(out != null){
                    out.close();
                }
                if(tarIn != null){
                    tarIn.close();
                }
                if(gzipIn != null){
                    gzipIn.close();
                }
                if(bufferedInputStream != null){
                    bufferedInputStream.close();
                }
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测字符串是否有乱码
     * @param str
     * @return
     */
    private static boolean isMessyCode( String str ) {
        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt( i ) ;
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            if( (int)c == 0xfffd )
            {
                // 存在乱码
                return true ;
            }
        }
        return false ;
    }

    /**
     * 检测压缩包的格式
     * @param file 压缩包文件
     * @return
     * @throws Exception
     */
    private static String getEncoding( File file ) throws Exception {
        String encoding = "GBK" ;
        ZipFile zipFile = new ZipFile( file ) ;
        zipFile.setFileNameCharset( encoding ) ;
        List<net.lingala.zip4j.model.FileHeader> list = zipFile.getFileHeaders() ;
        for( int i = 0; i < list.size(); i++ )
        {
            net.lingala.zip4j.model.FileHeader fileHeader = list.get( i ) ;
            String fileName = fileHeader.getFileName();
            if( isMessyCode( fileName ) )
            {
                encoding = "UTF-8" ;
                break ;
            }
        }
        return encoding ;
    }
}
