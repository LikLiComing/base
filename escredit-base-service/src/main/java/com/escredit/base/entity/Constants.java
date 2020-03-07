package com.escredit.base.entity;

public class Constants {

    public static final String DIVIDED_SYMBOLS = ",";

    public enum Database{

        YES(1,"是"),NO(0,"否");

        private int value;
        private String text;

        private Database(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return this.value;
        }

        public String getText() {
            return this.text;
        }

        public static Database get(int value) {
            for (Database bt : Database.values()) {
                if (bt.getValue() == value){
                    return bt;
                }
            }
            throw new IllegalArgumentException("unknown value:" + value);
        }
    }

}
