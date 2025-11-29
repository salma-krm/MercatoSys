package com.mercatosys.enums;

public enum OrderStatus {
    PENDING,    // الطلبية لم يتم دفعها بالكامل
    PAID,       // تم دفع كامل المبلغ، جاهزة للتأكيد من ADMIN
    CONFIRMED,  // تم تأكيد الطلبية من ADMIN
    CANCELED,   // تم إلغاء الطلبية
    REJECTED    // الطلبية مرفوضة
}
