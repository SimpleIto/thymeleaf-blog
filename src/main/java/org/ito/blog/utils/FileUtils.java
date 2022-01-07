package org.ito.blog.utils;

import java.io.*;

public class FileUtils{
    /**
     * 存储文件方法
     * @param path 文件路径
     * @param data 文件数据
     * @return 若指定文件不存在，则用传入的data创建一个文件（若目录不存在则创建目录）；若文件存在，则直接返回该文件对象
     * @throws IOException
     */
    public static File CreateAndSaveFile(String path,byte[] data) throws IOException {
        File saveFile = new File(path);
        if (saveFile.exists())
            return saveFile;
        File parent = saveFile.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
        outputStream.write(data);
        outputStream.close();
        return saveFile;
    }
}
