package com.impression.Utilities;

import java.util.List;

/**
 * Created by Pulkit Juneja on 12-Jun-16.
 */
public  interface GenericDataListListener<T> {

    void OnData(List<T> data, Exception e);
}


