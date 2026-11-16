package com.patc.inventarioqr.util;

public final class QrPayloadUtil {

    private QrPayloadUtil() {
    }

    public static String paraProducto(Long productoId, String sku) {
        return "PATC-INVENTARIO;PRODUCTO_ID=" + productoId + ";SKU=" + sku;
    }
}
