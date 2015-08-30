package com.zs.tools;

import com.feinno.framework.common.exception.BusinessException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Allen on 2015/6/22.
 */
public class UpLoadFileTools {

    /**
     * 上传图片
     * @param request
     * @param id
     * @param saveImgPathType
     * @param imgType
     * @param imgSize
     * @param imgMaxCount
     * @param imgPath
     * @return
     * @throws Exception
     */
    public static String uploadImg(HttpServletRequest request,List<MultipartFile> fileList,String imgType, int imgSize, int imgMaxCount, String imgPath)throws Exception{
        String imgUrl = "";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(commonsMultipartResolver.isMultipart(request)) {
            //取得request中的所有文件名
            if(null != fileList && 0 < fileList.size()) {
                //判断上传文件数量
                if (fileList.size() > imgMaxCount) {
                    throw new BusinessException("最多只能上传" + imgMaxCount + "张图片！");
                }
                for (int i=0; i<fileList.size(); i++) {
                    MultipartFile file = fileList.get(i);
                    //取得上传文件
                    if (file != null) {
                        //取得当前上传文件的文件名称
                        String fileName = file.getOriginalFilename();
                        //取得当前上传文件大小
                        long fileSize = file.getSize();
                        //如果名称不为"",说明该文件存在，否则说明该文件不存在
                        if (!StringUtils.isEmpty(fileName.trim()) && 0 < fileSize) {
                            //判断上传文件类型
                            String fileExtention = StringUtils.substringAfterLast(fileName, ".");
                            if (!fileExtention.matches(imgType)) {
                                throw new BusinessException("图片的类型只能是：" + imgType + "！");
                            }
                            //判断上传文件大小
                            if (fileSize / 1024 > imgSize) {
                                throw new BusinessException("图片最大不能超过" + imgSize + "kb");
                            }
                            //重命名上传后的文件名
                            fileName = UUID.randomUUID().toString() + "." + fileExtention;
                            //判断路径是否存在，不存在就创建
                            //定义上传路径
                            String savePath = imgPath  + "/" + fileName;
                            //服务器tomcat路径+保存路径
                            File localFile = new File(request.getRealPath("") + savePath);
                            if (localFile.mkdirs()) {
                                file.transferTo(localFile);
                                imgUrl += savePath + ",";
                            }
                        }
                    }
                }
            }
        }
        return  imgUrl.substring(0, imgUrl.length() > 0 ? imgUrl.length()-1 : 0);
    }

    /**
     * 删除文件
     * @param path
     * @throws Exception
     */
    public static void delFile(HttpServletRequest request, String path)throws Exception{
        File file = new File(request.getRealPath("") + path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    public static boolean delDir(String path)throws Exception{
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                File childrenFile = new File(dir, children[i]);
                boolean success = delDir(childrenFile.getPath());
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
