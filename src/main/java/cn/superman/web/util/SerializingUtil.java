package cn.superman.web.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class SerializingUtil {

    public static <T> byte[] toByte(T source, Class<T> targetClass) {
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(targetClass);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(source, schema, buffer);

            return protostuff;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            buffer.clear();
        }
        return null;
    }

    public static <T> T toBean(byte[] source, Class<T> targetClass) throws InstantiationException, IllegalAccessException {
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(targetClass);
        System.out.println(targetClass);
        T newInstance = targetClass.newInstance();
        ProtostuffIOUtil.mergeFrom(source, newInstance, schema);

        return newInstance;
    }

    public static <T> void writeBeanToFile(T source, Class<T> targetClass, File targetFile) throws IOException {
        byte[] classByte = toByte(source, targetClass);
        FileUtils.writeByteArrayToFile(targetFile, classByte);
    }

    public static <T> T readBeanFromFile(File sourceFile, Class<T> targetClass) throws InstantiationException, IllegalAccessException, IOException {
        byte[] source = FileUtils.readFileToByteArray(sourceFile);
        return toBean(source, targetClass);
    }
}
