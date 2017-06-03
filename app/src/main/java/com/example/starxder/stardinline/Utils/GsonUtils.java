package com.example.starxder.stardinline.Utils;

import com.example.starxder.stardinline.Beans.Category;
import com.example.starxder.stardinline.Beans.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/22.
 */

public class GsonUtils {
    public static List<User> getUserByGson(String jsonString) {
        List<User> list = new ArrayList<User>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<User>>() {

        }.getType());
        return list;
    }


    public static List<Category> getCategoryByGson(String jsonString) {
        List<Category> list = new ArrayList<Category>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<Category>>() {

        }.getType());
        return list;
    }


}

