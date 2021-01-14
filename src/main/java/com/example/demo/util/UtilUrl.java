package com.example.demo.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


class UtilUrl {
    public static String baseUrl = "https://open-api.10ss.net/";
    public static String openType;
    public static String freeType;
    public static String addPrinter;
    public static String scanCodeModel;
    public static String printIndex;
    public static String picturePrintIndex;
    public static String expressPrintIndex;
    public static String printerSetVoice;
    public static String printerDeleteVoice;
    public static String printerDeletePrinter;
    public static String printMenuAddPrintMenu;
    public static String printShutdownRestart;
    public static String printSetSound;
    public static String printPrintInfo;
    public static String printGetVersion;
    public static String printCancelAll;
    public static String printCancelOne;
    public static String printSetIcon;
    public static String printDeleteIcon;
    public static String printBtnPrint;
    public static String printGetOrder;
    public static String oauthSetPushUrl;
    public static String printerGetOrderStatus;
    public static String printerGetOrderPagingList;
    public static String printerGetPrintStatus;

    UtilUrl() {
    }

    static {
        openType = baseUrl + "oauth/authorize";
        freeType = baseUrl + "oauth/oauth";
        addPrinter = baseUrl + "printer/addprinter";
        scanCodeModel = baseUrl + "oauth/scancodemodel";
        printIndex = baseUrl + "print/index";
        picturePrintIndex = baseUrl + "pictureprint/index";
        expressPrintIndex = baseUrl + "expressprint/index";
        printerSetVoice = baseUrl + "printer/setvoice";
        printerDeleteVoice = baseUrl + "printer/deletevoice";
        printerDeletePrinter = baseUrl + "printer/deleteprinter";
        printMenuAddPrintMenu = baseUrl + "printmenu/addprintmenu";
        printShutdownRestart = baseUrl + "printer/shutdownrestart";
        printSetSound = baseUrl + "printer/setsound";
        printPrintInfo = baseUrl + "printer/printinfo";
        printGetVersion = baseUrl + "printer/getversion";
        printCancelAll = baseUrl + "printer/cancelall";
        printCancelOne = baseUrl + "printer/cancelone";
        printSetIcon = baseUrl + "printer/seticon";
        printDeleteIcon = baseUrl + "printer/deleteicon";
        printBtnPrint = baseUrl + "printer/btnprint";
        printGetOrder = baseUrl + "printer/getorder";
        oauthSetPushUrl = baseUrl + "oauth/setpushurl";
        printerGetOrderStatus = baseUrl + "printer/getorderstatus";
        printerGetOrderPagingList = baseUrl + "printer/getorderpaginglist";
        printerGetPrintStatus = baseUrl + "printer/getprintstatus";
    }
}
