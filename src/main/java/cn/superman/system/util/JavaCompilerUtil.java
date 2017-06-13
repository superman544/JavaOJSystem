package cn.superman.system.util;

import java.io.File;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class JavaCompilerUtil {
    private static JavaCompiler javaCompiler;

    private JavaCompilerUtil() {
    };

    private static JavaCompiler getJavaCompiler() {
        if (javaCompiler == null) {
            synchronized (JavaCompilerUtil.class) {
                if (javaCompiler == null) {
                    javaCompiler = ToolProvider.getSystemJavaCompiler();
                }
            }
        }

        return javaCompiler;
    }

    public static boolean compilerJavaFile(String sourceFileInputPath, String classFileOutputPath) {
        // 设置编译选项，配置class文件输出路径
        Iterable<String> options = Arrays.asList("-d", classFileOutputPath);
        StandardJavaFileManager fileManager = getJavaCompiler().getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(new File(sourceFileInputPath)));

        return getJavaCompiler().getTask(null, fileManager, null, options, null, compilationUnits).call();
    }
}
