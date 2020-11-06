package com.google.swarm.sqlserver.migration.utils;

import org.apache.beam.sdk.options.ValueProvider;

public class CustomValueProvider {

  public static ValueProvider<String> getValueProviderOf(String option){
    return new ValueProvider<String>() {
      @Override
      public String get() {
        return option;
      }

      @Override
      public boolean isAccessible() {
        if(option!=null)
          return true;
        else return false;
      }
    };
  }

  public static ValueProvider<Integer> getValueProviderOf(Integer option){
   return new ValueProvider<Integer>() {
     @Override
     public Integer get() {
       return option;
     }

     @Override
     public boolean isAccessible() {
       if(option!=null)
        return true;
       else return false;
     }
   };
  }

}
