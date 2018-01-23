    package cn.worldwalker.game.wyqp.common.utils;  
    import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  

import cn.worldwalker.game.wyqp.common.constant.Constant;
    /** 
     * @说明 从网络获取图片到本地 
     * @author 崔素强 
     * @version 1.0 
     * @since 
     */  
    public class UrlImgDownLoadUtil {  
    	public static void main(String[] args) {
    		System.out.println(getLocalImgUrl1("http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLzL5Bf9HpibQR27xNRia4KrXtsruqd3viamaDdN8aQul0TL0U75l3AHzsFiciaLBwib8ntQ8Dsj6FRGqmQ/0", 111111));
    	}
    	
    	  public static String getLocalImgUrl1(String strUrl, Integer playerId){
          	String fileName = playerId + ".jpg";  
          	File file = new File("C:/Users/jinfeng.liu/Desktop/game-fan/" + fileName);
          	if (file.exists()) {
  				return "" + fileName;
  			}
          	byte[] btImg = getImageFromNetByUrl(strUrl);  
              if(null != btImg && btImg.length > 0){  
                  writeImageToDisk(btImg, file);  
              }else{  
                  System.out.println("没有从该连接获得内容");  
              }  
              return "" + fileName;
          }
    	
        public static String getLocalImgUrl(String strUrl, Integer playerId){
        	String fileName = playerId + ".jpg";  
        	File file = new File(Constant.localWxHeadImgPath + fileName);
        	if (file.exists()) {
				return Constant.downloadWxHeadImgUrl + fileName;
			}
        	byte[] btImg = getImageFromNetByUrl(strUrl);  
            if(null != btImg && btImg.length > 0){  
                writeImageToDisk(btImg, file);  
            }else{  
                System.out.println("没有从该连接获得内容");  
            }  
            return Constant.downloadWxHeadImgUrl + fileName;
        }
        
        /** 
         * 根据地址获得数据的字节流 
         * @param strUrl 网络连接地址 
         * @return 
         */  
        public static byte[] getImageFromNetByUrl(String strUrl){  
            try {  
                URL url = new URL(strUrl);  
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                conn.setRequestMethod("GET");  
                conn.setConnectTimeout(5 * 1000);  
                InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
                byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
                return btImg;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return null;  
        }  
        
        /** 
         * 将图片写入到磁盘 
         * @param img 图片数据流 
         * @param fileName 文件保存时的名称 
         */  
        public static void writeImageToDisk(byte[] img, File file){  
            try {  
                FileOutputStream fops = new FileOutputStream(file);  
                fops.write(img);  
                fops.flush();  
                fops.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        
        /** 
         * 从输入流中获取数据 
         * @param inStream 输入流 
         * @return 
         * @throws Exception 
         */  
        public static byte[] readInputStream(InputStream inStream) throws Exception{  
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
            byte[] buffer = new byte[1024];  
            int len = 0;  
            while( (len=inStream.read(buffer)) != -1 ){  
                outStream.write(buffer, 0, len);  
            }  
            inStream.close();  
            return outStream.toByteArray();  
        }  
    }  