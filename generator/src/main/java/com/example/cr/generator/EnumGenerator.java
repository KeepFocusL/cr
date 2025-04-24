package com.example.cr.generator;

import cn.hutool.core.util.StrUtil;
import com.example.cr.generator.enums.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class EnumGenerator {
    static String frontendType = "web-admin";
    static String basePath = frontendType + "/src/assets/js/";
    static String enumsJsPath = basePath + "enums.js";

    static {
        new File(basePath).mkdirs();
    }

    public static void main(String[] args) {
        StringBuffer bufferArray = new StringBuffer();
        long begin = System.currentTimeMillis();
        try {
            // 具体的枚举类可能在不同的模块中
            // 需要在 generator 模块中引入目标模块依赖，
            // 或者将目标模块的枚举代码复制到 generator 模块（暂时来看：少量代码冗余比额外引入依赖更合适）
            toJson(TrainType.class, bufferArray);
            toJson(SeatType.class, bufferArray);
            toJson(SeatCol.class, bufferArray);
            toJson(PassengerType.class, bufferArray);
            toJson(ConfirmOrderStatus.class, bufferArray);
            writeJs(bufferArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - begin) + " 毫秒");
    }

    private static void toJson(Class clazz, StringBuffer bufferArray) throws Exception {
        // enumConst：将 OneTow 格式转成 ONE_TOW 格式（全大写，下划线分割）
        String enumConst = StrUtil.toUnderlineCase(clazz.getSimpleName()).toUpperCase();
        Object[] objects = clazz.getEnumConstants();

        // Method name = clazz.getMethod("name");
        // Method getDesc = clazz.getMethod("getDesc");
        // Method getCode = clazz.getMethod("getCode");

        // 排除额外的属性和 $VALUES
        ArrayList<Field> targetFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isPrivate(field.getModifiers())
                    || "ENUM$VALUES".equals(field.getName())
                    || "$VALUES".equals(field.getName())
            ) {
                continue;
            }
            targetFields.add(field);
        }

        bufferArray.append("window.").append(enumConst).append("_ARRAY = [");
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            // bufferArray.append("{code:\"").append(getCode.invoke(obj)).append("\", desc:\"").append(getDesc.invoke(obj)).append("\"}");
            formatJsonObj(bufferArray, targetFields, clazz, obj);
            if (i < objects.length - 1) {
                bufferArray.append(", ");
            }
        }
        bufferArray.append("];\r\n");
    }

    public static void writeJs(StringBuffer stringBuffer) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(enumsJsPath);
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            System.out.println(enumsJsPath);
            osw.write(stringBuffer.toString());
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将枚举转成 JSON 对象字符串
     * 如: 将 CollectionType.VIDEO("video", "视频") 转成 {code:"video", desc:"视频"}
     * @param bufferObject
     * @param targetFields
     * @param clazz
     * @param obj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void formatJsonObj(StringBuffer bufferObject, List<Field> targetFields, Class clazz, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        bufferObject.append("{");
        for (int j = 0; j < targetFields.size(); j++) {
            Field field = targetFields.get(j);
            String fieldName = field.getName();
            String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            bufferObject.append(fieldName).append(":\"").append(clazz.getMethod(getMethod).invoke(obj)).append("\"");
            if (j < targetFields.size() - 1) {
                bufferObject.append(", ");
            }
        }
        bufferObject.append("}");
    }
}