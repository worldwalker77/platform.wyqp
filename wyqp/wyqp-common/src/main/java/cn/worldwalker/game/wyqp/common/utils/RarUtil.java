package cn.worldwalker.game.wyqp.common.utils;

import java.io.File;
import java.io.FileOutputStream;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class RarUtil {
	
	public static void unrar(File sourceRar, File destDir) throws Exception {  
        Archive archive = null;  
        FileOutputStream fos = null;  
        System.out.println("Starting...");  
        try {  
            archive = new Archive(sourceRar);  
            FileHeader fh = archive.nextFileHeader();  
            int count = 0;  
            File destFileName = null;  
            while (fh != null) {  
            	String fileName = fh.getFileNameString().trim();
                if (fileName.contains("\\")) {
					fileName = fileName.replace("\\", "/");
				}
                String compressFileNamePath = destDir.getAbsolutePath() + "/" + fileName;
                destFileName = new File(compressFileNamePath); 
                if (fh.isDirectory()) {  
                    if (!destFileName.exists()) {  
                        destFileName.mkdirs();  
                    }  
                    fh = archive.nextFileHeader();  
                    continue;  
                }   
                if (!destFileName.getParentFile().exists()) {  
                    destFileName.getParentFile().mkdirs();  
                }  
                fos = new FileOutputStream(destFileName);  
                archive.extractFile(fh, fos);  
                fos.close();  
                fos = null;  
                fh = archive.nextFileHeader();  
            }  
  
            archive.close();  
            archive = null;  
            System.out.println("Finished !");  
        } catch (Exception e) {  
            throw e;  
        } finally {  
            if (fos != null) {  
                try {  
                    fos.close();  
                    fos = null;  
                } catch (Exception e) {  
                    //ignore  
                }  
            }  
            if (archive != null) {  
                try {  
                    archive.close();  
                    archive = null;  
                } catch (Exception e) {  
                    //ignore  
                }  
            }  
        }  
    }  
	
	public static void mkDir(String dir){
		File newFile = new File(dir);
    	if (!newFile.exists()) {
    		newFile.mkdirs();
		}
	}
	
	
	/**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	public static void main(String[] args) {
		
		File sourceRar = new File("C:\\Users\\jinfeng.liu\\Desktop\\rar\\170819173506.rar");
		
		File destDir = new File("C:\\Users\\jinfeng.liu\\Desktop\\rar");
		
		String a = "170822094410\\game_code_170822094410.zip";
		a = a.replace("\\", "/");
		System.out.println(a);
	}
}
