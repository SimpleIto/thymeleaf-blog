package org.ito.blog.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanMapper {
    public static <T> T map(Object source,Class<T> targetClass){
        T target = BeanUtils.instantiateClass(targetClass);
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> targetClass){
        ArrayList<T> resultList = new ArrayList<>();
        for(Object source:sourceList){
            T target = BeanUtils.instantiateClass(targetClass);
            BeanUtils.copyProperties(source,target);
            resultList.add(target);
        }
        return resultList;
    }
}
