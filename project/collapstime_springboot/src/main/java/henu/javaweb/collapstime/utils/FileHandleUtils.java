package henu.javaweb.collapstime.utils;

import henu.javaweb.collapstime.model.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

/**
 * 工具类，用来处理上传的文件
 */
public class FileHandleUtils {
    /**
     * 上传图片处理(原版)
     * @param file savePath
     * @return
     */
    public static FileUploadResult fileUpload(MultipartFile file, String savePath) {
        FileUploadResult result = new FileUploadResult();
        if (file.isEmpty()) {
            result.setMsg("上传文件为空");
            return result;
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        if(".png".equals(suffixName) || ".jpg".equals(suffixName) || ".jpeg".equals(suffixName)){
            String filePath = ""; // 上传后的路径
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")){
                filePath = Cons.RESOURCE_WIN_PATH;
            }else {
                filePath = Cons.RESOURCE_MAC_PATH;
            }
            filePath+="/img/"+savePath+"/";
            fileName = UUID.randomUUID() + suffixName; // 新文件名
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.setMsg("上传成功");
            result.setFilePath("/img/"+savePath+"/"+fileName);
            return result;

        }else {
            result.setMsg("只支持上传png,jpeg,jpg格式的图像");
            return result;
        }
    }

    /**
     * 可裁剪的新方法上传图片
     * @param file
     * @param savePath
     * @return
     */
    public static FileUploadResult filehandle(String file, String savePath){
        //对字节数组字符串进行Base64解码并生成图片
        if (file == null) //图像数据为空
            return null;
        file=file.split("base64,")[1];//把传送过来的前面不属于base64的部分分割掉，保留base64编码部分
        try
        {
            //Base64解码
            byte[] b = Base64.getDecoder().decode(file);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            FileUploadResult result = new FileUploadResult();
            String newfilename = System.currentTimeMillis() + UUID.randomUUID().toString().replace(".", "").substring(0, 6) + ".png" ;
            String filePath = Cons.RESOURCE_WIN_PATH;
            filePath+="/img/"+savePath+"/";
            String fileName = UUID.randomUUID() + newfilename;
            String imgFilePath = filePath + fileName;//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();//关闭流
            result.setMsg("上传成功");
            result.setFilePath("/img/"+savePath+"/"+fileName);//设置返回路径
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 删除本地文件
     * @param filepath
     * @return
     */
    public static String deleteFile(String filepath){
        String resultInfo=null;
        File file = new File(filepath);
        if (file.exists()) {//文件是否存在
            if (file.delete()) {//存在就删了，返回1
                resultInfo =  "删除成功";
            } else {
                resultInfo =  "删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        return resultInfo;
    }

    /**
     * 上传MP3文件
     * @param file
     * @return
     */
    public static FileUploadResult mp3Upload(MultipartFile file) {
        FileUploadResult result = new FileUploadResult();
        if (file.isEmpty()) {
            result.setMsg("上传文件为空");
            return result;
        }
        String fileName = file.getOriginalFilename();  // 文件名
        //String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = ""; // 上传后的路径
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            filePath = Cons.RESOURCE_WIN_PATH;
        } else {
            filePath = Cons.RESOURCE_MAC_PATH;
        }
        filePath += "/song/";
        //fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setMsg("上传成功");
        result.setFilePath("/song/" + fileName);
        return result;

    }
}
