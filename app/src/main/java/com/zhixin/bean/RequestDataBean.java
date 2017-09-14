package com.zhixin.bean;

import com.zhixin.utils.L;

public class RequestDataBean {
    //http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8
    public static String locationcity = "";

    public static String outputfromat = "json";
    //"iGs8rFvzh1e8c7C9DjXT5toK",
    public static String ak[] = {"6tYzTvGZSOpYB5Oc2YGGOKt8"};
    //,"FK9mkfdQsloEngodbFl4FeY3", "x6zT6NfQdFn5Xw84DzM6cXp","5slgyqGDENN7Sy7pw29IUvrZ"};

    //public static String ak ="7x6zT6NfQdFn5Xw84DzM6cXp";
    public static String getLocation() {
        return locationcity;
    }

    public static void setLocation(String location) {
        RequestDataBean.locationcity = location;
    }

    public static String getOutput() {
        return outputfromat;
    }

    public static void setOutput(String output) {
        RequestDataBean.outputfromat = output;
    }

    public static String getData() {
        //L.e("getData msg:"+"http://api.map.baidu.com/telematics/v3/weather?location="+locationcity + "&output=" + outputfromat + "&ak=" + ak);
        return "http://api.map.baidu.com/telematics/v3/weather?location="
                + locationcity + "&output=" + outputfromat + "&ak=" + ak[(int) Math.random() ];
    }

}
