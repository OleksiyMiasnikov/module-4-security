package com.epam.esm;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Key key1 = new Key("key1", List.of());
        //key1.setValue("key1");

        Key key2 = new Key("key2", List.of());
       // key2.setValue("key2");

        HashMap<Key, String> map = new HashMap<>();

        map.put(key1, "Map value for key1");
        map.put(key2, "Map value for key2");

       // key1.setValue("key3");

        System.out.println(map.get(key1));
    }

    static final class Key {
        private final String value;
        private final List<String> list;

        Key(String value, List<String> list) {
            this.value = value;
            this.list = new ArrayList<>(list);
        }
        //other methods
//        public void setValue(String value) {
//            this.value = value;
//        }

        public Key append(String value) {
            //this.list.add(value);
            List<String> newList = new ArrayList<>(list);
            newList.add(value);
            return new Key(this.value, newList);
        }
        public List<String> getList() {
            return new ArrayList<>(list);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(value, key.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}