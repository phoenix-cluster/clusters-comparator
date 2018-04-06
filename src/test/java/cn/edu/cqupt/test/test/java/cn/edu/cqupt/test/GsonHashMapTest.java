package cn.edu.cqupt.test.test.java.cn.edu.cqupt.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GsonHashMapTest {
    public static void main(String[] args) {
        HashMap<String, HashMap<Integer, People>> groups = new HashMap<>();

        HashMap<Integer, People> group1 = new HashMap<>();
        group1.put(1, new People("Li Ming", 18));
        group1.put(2, new People("Wang Mei", 18));

        HashMap<Integer, People> group2 = new HashMap<>();
        group2.put(1, new People("Xiao Li", 18));
        group2.put(2, new People("Da Bao", 18));

        groups.put("G1", group1);
        groups.put("G2", group2);

        // method I
        Gson gson1 = new Gson();
        String map2Json1 = gson1.toJson(groups); // convert Map to json
        HashMap<Integer, People> json2map1 = gson1.fromJson(map2Json1, new HashMap<Integer, People>().getClass());// convert json to Map

        System.out.println("1. convert Map to json:");
        System.out.println(map2Json1);
        System.out.println("2. convert json to Map:");
        System.out.println(json2map1);


        // method II
        Gson gson2 = new GsonBuilder().create();
        String map2Json2 = gson2.toJson(groups); // convert Map to json
        Type type = new TypeToken<HashMap<Integer, People>>() {
        }.getType();
        HashMap<Integer, People> json2map2 = gson2.fromJson(map2Json2, type); // convert json to Map

        System.out.println("3. convert Map to json:");
        System.out.println(map2Json2);
        System.out.println("4. convert json to Map:");
        System.out.println(json2map2);
    }
}

class People {
    private String name;
    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}