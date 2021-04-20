package io.agora.vlive.protocol.model.request;

public class Request {
    // General purposes
    public static final int OSS = 2;
    public static final int GIFT_LIST = 3;
    public static final int MUSIC_LIST = 4;

    // Live Room
    public static final int CREATE_ROOM = 8;
    public static final int ROOM_LIST = 9;
    public static final int ENTER_ROOM = 10;
    public static final int LEAVE_ROOM = 11;
    public static final int AUDIENCE_LIST = 12;
    public static final int SEND_GIFT = 13;
    public static final int GIFT_RANK = 14;
    public static final int REFRESH_TOKEN = 15;
    public static final int SEAT_STATE = 18;

    //Seat Operations
    public static final int SEAT_INTERACTION = 19;

    // Product management
    public static final int PRODUCT_LIST = 22;
    public static final int PRODUCT_PURCHASE = 23;
    public static final int PRODUCT_MANAGE = 24;

    public static String getRequestString(int request) {
        switch (request) {
            case OSS: return "oss";
            case GIFT_LIST: return "gift_list";
            case MUSIC_LIST: return "music_list";
            case CREATE_ROOM: return "create_room";
            case ROOM_LIST: return "room_list";
            case ENTER_ROOM: return "enter_room";
            case LEAVE_ROOM: return "leave_room";
            case AUDIENCE_LIST: return "audience_list";
            case SEND_GIFT: return "send_gift";
            case GIFT_RANK: return "gift_rank";
            case REFRESH_TOKEN: return "refresh_token";
            case SEAT_STATE: return "seat_state";
            case SEAT_INTERACTION: return "seat_interaction";
            case PRODUCT_LIST: return "product_list";
            case PRODUCT_PURCHASE: return "product_purchase";
            case PRODUCT_MANAGE: return "product_manage";
            default: return "unknown";
        }
    }
}