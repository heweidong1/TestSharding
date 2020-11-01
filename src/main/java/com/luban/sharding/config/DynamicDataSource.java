package com.luban.sharding.config;

import com.luban.sharding.utils.DbUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

public class DynamicDataSource extends AbstractRoutingDataSource{
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println(DbUtil.getDb());
        return DbUtil.getDb();
        //可以返回 return  "master";  就代表使用master的数据源
    }
}
