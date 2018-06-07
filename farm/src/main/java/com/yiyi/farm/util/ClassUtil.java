package com.yiyi.farm.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author peach
 * @date 2018-04-22 15:02:16
 * @description 类分析器
 */
public final class ClassUtil {
    //工具类,防止实例化
    private ClassUtil(){}

    public static List<Class<?>> getClasses(String pkgName){
        List<Class<?>> resultList = new ArrayList<>();
        String pkgPath = StringUtil.replaceTo(pkgName, '.', '/');
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(pkgPath);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if (isFile(url)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "utf-8");

                    findAndAddClass(pkgName, filePath, resultList);
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    /**
     *
     * @param pkgName 包名(xx.xx.xx),
     * @param filePath 包代表的文件路径(/xx/xx/xx)
     * @param classes 收集包下所有.class文件所代表的Class对象的集合
     */
    private static void findAndAddClass(String pkgName, String filePath, List<Class<?>> classes) {
        File dir = new File(filePath);
        if(notExistDirectory(dir)){
            return;
        }

        File[] files = dir.listFiles(ClassUtil::canFindOrGetClass);

        for(File file: files){
            if(file.isDirectory()){
                findAndAddClass(linkByDot(pkgName, file.getName()), file.getAbsolutePath(), classes);
            }else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try{
                    classes.add(Class.forName(linkByDot(pkgName, className)));
                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 判读文件是否是可以迭代查询class文件或者可以获取(本身就是)Class文件
     * @param file
     * @return
     */
    private static boolean canFindOrGetClass(File file) {
        return file.isDirectory() || file.getName().endsWith(".class");
    }

    /**
     * 判断dir代表的资源不是一个存在的目录
     * @param dir
     * @return
     */
    private static boolean notExistDirectory(File dir) {
        return !dir.exists() || !dir.isDirectory();
    }

    /**
     * url的协议的是否是文件
     * @param url
     * @return
     */
    private static boolean isFile(URL url) {
        return url.getProtocol().equals("file");
    }

    private static String linkByDot(String s1, String s2){
        return s1 + '.' + s2;
    }

    public static void main(String[] args) {
        List<Class<?>> list = getClasses("com.yiyi.farm.controller");
        for(Class<?> clazz : list)
            System.out.println(clazz);
    }
}
