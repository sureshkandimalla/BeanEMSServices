package com.bean.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;

import java.util.Arrays;

public final class NullPropertyUtils {
  public static String[] getNullPropertyNames(Object source) {
    BeanWrapper src = new BeanWrapperImpl(source);
    return Arrays.stream(src.getPropertyDescriptors())
        .map(PropertyDescriptor::getName)
        .filter(name -> src.getPropertyValue(name) == null)
        .toArray(String[]::new);
  }
}
