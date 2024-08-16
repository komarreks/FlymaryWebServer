package main.model.orders;

public enum OrderStatus {
    OPEN,
    IN_WORK,
    WAYT_PAY,
    PAYED,
    SEND,
    FINISH,
    TERMINATED;

    public static OrderStatus getStatus(String name){
        if (name.equals("OPEN")) return OPEN;
        if (name.equals("IN_WORK")) return IN_WORK;
        if (name.equals("WAYT_PAY")) return WAYT_PAY;
        if (name.equals("PAYED")) return PAYED;
        if (name.equals("SEND")) return SEND;
        if (name.equals("FINISH")) return FINISH;
        if (name.equals("TERMINATED")) return TERMINATED;

        return null;
    }
}
