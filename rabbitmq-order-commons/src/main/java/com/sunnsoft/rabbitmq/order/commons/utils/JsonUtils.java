package com.sunnsoft.rabbitmq.order.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * 易购商城自定义响应结构
 */
public class JsonUtils implements Serializable{

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Set<T> jsonToSet(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(Set.class, beanType);
        try {
            Set<T> set =MAPPER.readValue(jsonData, javaType);
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ParseException {
        Set<Integer> set=new HashSet<>();
        set.add(1);
        set.add(2);
        System.out.println("set:"+set);
        String json="[1,2,3]";
        Set<Integer> set1 = JsonUtils.jsonToSet(json, Integer.class);
        System.out.println("set1:"+set1);
        System.out.println("set1:"+set1.getClass());
        Map<Integer,String> map=new HashMap<>();
        map.put(1,"map1");
        map.put(2,"map2");
        String json2="{'1':'map1','2':'map2'}";
    }

}
