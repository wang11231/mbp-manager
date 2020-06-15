package com.art.manager.constants;

/**
 * 常量锁
 */
public class LockConstants {

    public static Object LOCK0 = new Object();
    public static Object LOCK1 = new Object();
    public static Object LOCK2 = new Object();
    public static Object LOCK3 = new Object();
    public static Object LOCK4 = new Object();
    public static Object LOCK5 = new Object();
    public static Object LOCK6 = new Object();
    public static Object LOCK7 = new Object();
    public static Object LOCK8 = new Object();
    public static Object LOCK9 = new Object();
    public static Object LOCK10 = new Object();
    public static Object LOCK11 = new Object();
    public static Object LOCK12 = new Object();
    public static Object LOCK13 = new Object();
    public static Object LOCK14 = new Object();
    public static Object LOCK15 = new Object();
    public static Object LOCK16 = new Object();
    public static Object LOCK17 = new Object();
    public static Object LOCK18 = new Object();
    public static Object LOCK19 = new Object();

    private static Long LOCK_LENGTH = 20L;
    public static Object getLock(Long id){
        if(id == 0){
            return LOCK0;
        }
        long mod = id % LOCK_LENGTH;
        switch(String.valueOf(mod)){
            case "0":
                return LOCK0;
            case "1":
                return LOCK1;
            case "2":
                return LOCK2;
            case "3":
                return LOCK3;
            case "4":
                return LOCK4;
            case "5":
                return LOCK5;
            case "6":
                return LOCK6;
            case "7":
                return LOCK7;
            case "8":
                return LOCK8;
            case "9":
                return LOCK9;
            case "10":
                return LOCK10;
            case "11":
                return LOCK11;
            case "12":
                return LOCK12;
            case "13":
                return LOCK13;
            case "14":
                return LOCK14;
            case "15":
                return LOCK15;
            case "16":
                return LOCK16;
            case "17":
                return LOCK17;
            case "18":
                return LOCK18;
            case "19":
                return LOCK19;
            default:
                return LOCK0;
        }
    }


}
