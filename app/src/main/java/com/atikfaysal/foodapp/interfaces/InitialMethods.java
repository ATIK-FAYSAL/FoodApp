package com.atikfaysal.foodapp.interfaces;

import java.util.List;

public interface InitialMethods
{
    void initComponent();//initialize all component

    void setToolbar();//set toolbar at top

    void processJsonData(String json);//process json string

    List<?> processJsonValue(String json);//process json string and return a list
}
