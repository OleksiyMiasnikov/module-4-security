package com.epam.esm;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Key key1 = new Key();
        key1.setValue("key1");

        Key key2 = new Key();
        key2.setValue("key2");

        HashMap<Key, String> map = new HashMap<>();

        map.put(key1, "Map value for key1");
        map.put(key2, "Map value for key2");

        key1.setValue("key3");

        System.out.println(map.get(key1));
    }

    static class Key {
        private String value;
        //other methods

        public void setValue(String value) {
            this.value = value;
        }
    }
}