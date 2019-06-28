package com.organization.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Endpoint;

import com.aliyun.oss.OSSClient;
import com.organization.config.CommConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {
    private static Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    private static String endpoint = CommConfig.endpoint;
    private static String accessKeyId = CommConfig.accessKeyId;
    private static String accessKeySecret = CommConfig.accessKeySecret;
    private static String bucketName = CommConfig.bucketName;

    /**
     * 上传图片公共方法（非图片服务器）
     * @param file
     * @param savedUrl
     * @param request
     * @return
     */
    public static String uploadPhoto(MultipartFile file, String savedUrl, HttpServletRequest request) {
        String picURL = null;
        try {
            long fileSize = file.getSize();
            long fileSizeM = fileSize / 1024 / 1024;
            logger.info("传入文件大小：" + fileSizeM + "M");
            if (fileSizeM > 20){
                logger.error("传入文件大小超过20M");
                return null;
            }

            //文件上传到...路径
            String uploadPath = null;
            // 保存到数据库里的地址（拼接的部分）
            StringBuffer requestURL = request.getRequestURL();// http://localhost:8082/app/mine/uploadUserHead
            String[] split = requestURL.toString().split("/");
            // 得到上传的文件名
            String fileName = file.getOriginalFilename();
            // 文件后缀名
            String fileEx = fileName.substring(fileName.indexOf("."), fileName.length());
            // 重新命名的文件名
            String newFileName = MD5Utils.get4code() + System.currentTimeMillis() + fileEx;

            String system = System.getProperty("os.name");
            System.out.println("系统名称：" + system);
            if (system.toUpperCase().contains("WINDOWS")) {
                // 获取Servlet容器(tomcat)对象实际路径
                uploadPath = request.getSession().getServletContext().getRealPath("/upload");
                picURL = split[0] + "//" + split    [2] + "/upload/" + newFileName;
            } else if ("Linux".equalsIgnoreCase(system)) {
                uploadPath = "/mnt/mallUpload/";
                picURL = split[0] + "//" + split[2] + uploadPath + newFileName;
            } else {
                uploadPath = "/mnt/mallUpload/";
                picURL = split[0] + "//" + split[2] + uploadPath + newFileName;
            }
            String pathName = uploadPath + File.separator + newFileName;
            logger.info("上传的文件保存位置及名称：" + pathName);
            logger.info("上传的文件保存到数据库的路径及名称：" + picURL);
            // 创建文件对象
            File localFile = new File(pathName);
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            // windows下把文件上传至项目制定目录，linux下把文件上传至指定文件夹
            file.transferTo(localFile);
//            if (null != savedUrl && !"".equals(savedUrl)) {
//                String substring = savedUrl.substring(savedUrl.lastIndexOf("/"));
//                File file = new File(uploadPath + substring);
//                if (file.exists()) {
//                    logger.info("执行删除文件方法开始");
//                    file.delete();
//                    logger.info("执行删除文件方法结束");
//                }
//            }
        } catch (Exception e) {
            logger.error("上传文件方法错误：e:" + e.getMessage());
            e.printStackTrace();
        }
        return picURL;
    }

    /**
     * 上传文件公共方法（oss简单上传）
     * @param file
     * @param folder
     * @param request
     * @return
     */
    public static String uploadPhotoToOss(MultipartFile file, String folder, HttpServletRequest request) {
        String strUrl = null;
        try {
            long fileSize = file.getSize();
            long fileSizeM = fileSize / 1024 / 1024;
            logger.info("传入文件大小：" + fileSizeM + "M");
            if (fileSizeM > 20){
                logger.error("传入文件大小超过20M");
                return null;
            }

            String objectName = "";
            if (StringUtil.isEmpty(folder)) {
                folder = "temp";
            }
            //功能模块路径
            folder = "mallProject/" + folder + "/";
            // 得到上传的文件名
            String fileName = file.getOriginalFilename();
            String fileEx = fileName.substring(fileName.indexOf("."), fileName.length());
            // 路径+重新命名的文件名
            objectName = folder + MD5Utils.get4code() + System.currentTimeMillis() + fileEx;

            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));

            // 关闭OSSClient。
            ossClient.shutdown();
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            String url = ossClient.generatePresignedUrl(bucketName, objectName, expiration).toString();
            strUrl = url.substring(0, url.lastIndexOf("?"));
            logger.info("截取'?'后的Url:" + strUrl);
        } catch (Exception e) {
            logger.info("上传文件方法错误：e:" + e.getMessage());
            e.printStackTrace();
        }
        return strUrl;
    }
    /**
     * 判断文件大小
     *
     * @param :multipartFile:上传的文件
     * @param size: 限制大小
     * @param unit:限制单位（B,K,M,G)
     * @return boolean:是否大于
     */
    public static boolean checkFileSize( MultipartFile multipartFile, int size, String unit) {
        long len = multipartFile.getSize();//上传文件的大小, 单位为字节.
        //准备接收换算后文件大小的容器
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        //如果上传文件大于限定的容量
        if (fileSize > size) {
            return false;
        }
        return true;
    }
}
