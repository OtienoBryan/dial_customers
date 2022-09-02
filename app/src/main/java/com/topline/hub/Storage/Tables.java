package com.topline.hub.Storage;

import android.provider.BaseColumns;

/**
 * Created by muejacob on 07/10/2017.
 */

public abstract class Tables {

    public static final class API_APP {
        public static final String BASE_URL = "http://geojakltd.co.ke/impulse/mobile/v1/";
        public static final String PRODUCTS = "getProducts.php";
    }

    public static final class Defaults {
        public static final String TEXT_TYPE = " TEXT";
        public static final String INTEGER_TYPE = " INTEGER";
        public static final String NUMERIC_TYPE = " NUMERIC";
        public static final String REAL_TYPE = " REAL";
        public static final String COMMA = ", ";
        public static final String UNIQUE = " UNIQUE";
        public static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";
        public static final String DATABASE_NAME="topline_db";
        public static final int DATABASE_VERSION=1;


    }

    public static final class TableProduct implements BaseColumns {
        public static final String TABLENAME="tbl_product";
        public static final String ID="id";
        public static final String NAME="name";
        public static final String SKU="sku";
        public static final String CATEGORY="category";
        public static final String PRICE="price";

        public static final String SQL_CREATE="CREATE TABLE "+TABLENAME+ "("+_ID+ Defaults.INTEGER_TYPE+ Defaults.PRIMARY_KEY+ Defaults.COMMA+"" +
                ID+ Defaults.TEXT_TYPE+ Defaults.COMMA+NAME+ Defaults.TEXT_TYPE+ Defaults.COMMA+SKU+ Defaults.TEXT_TYPE+ Defaults.COMMA+
                CATEGORY+ Defaults.TEXT_TYPE+ Defaults.COMMA+PRICE+ Defaults.TEXT_TYPE+")";
    }

}
