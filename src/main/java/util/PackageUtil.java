package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 指定扫描路径的工具类
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/24 13:22
 */
public class PackageUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PackageUtil.class);

    private static final String CLASS_SUFFIX = ".class";
    private PackageUtil(){}

    /**
     * 扫描指定包下的所有文件
     * @param packageName 包路径
     * @return 包下的所有文件全限定名
     */
    public static Set<String> scannerClassFile(String packageName){
        Set<String> classNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                classNames = getClassNameFromDir(url.getPath(), packageName, false);
            } else if (protocol.equals("jar")) {
                try(JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();){
                    classNames = getClassNameFromJar(jarFile.entries(), packageName, false);
                } catch(Exception e){
                    LOG.error(e.getMessage(), e);
                }
            }
        } else {
            /*从所有的jar包中查找包名*/
            classNames = getClassNameFromJars(((URLClassLoader)loader).getURLs(), packageName, false);
        }

        return classNames;
    }

    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @param packageName 类名集合
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
        Set<String> className = new HashSet<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    className.addAll(getClassNameFromDir(childFile.getPath(), packageName+"."+childFile.getName(), isRecursion));
                }
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(CLASS_SUFFIX) && !fileName.contains("$")) {
                    className.add(packageName+ "." + fileName.replace(CLASS_SUFFIX, ""));
                }
            }
        }

        return className;
    }

    /**
     * @param jarEntries
     * @param packageName
     * @param isRecursion
     * @return
     */
    private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries, String packageName, boolean isRecursion){
        Set<String> classNames = new HashSet<>();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            if(!jarEntry.isDirectory()){
                String entryName = jarEntry.getName().replace("/", ".");
                if (entryName.endsWith(CLASS_SUFFIX) && !entryName.contains("$") && entryName.startsWith(packageName)) {
                    entryName = entryName.replace(CLASS_SUFFIX, "");
                    if(isRecursion || (!entryName.replace(packageName+".", "").contains("."))){
                        classNames.add(entryName);
                    }
                }
            }
        }

        return classNames;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     * @param urls URL集合
     * @param packageName 包路径
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<>();

        for (int i = 0; i < urls.length; i++) {
            String classPath = urls[i].getPath();

            //不必搜索classes文件夹
            if (classPath.endsWith("classes/")) {continue;}
            try (JarFile jarFile = new JarFile(classPath.substring(classPath.indexOf('/')))){
                classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return classNames;
    }
}

