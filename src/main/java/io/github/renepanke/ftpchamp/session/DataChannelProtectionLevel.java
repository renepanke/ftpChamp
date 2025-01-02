package io.github.renepanke.ftpchamp.session;

import io.github.renepanke.ftpchamp.exceptions.FTPServerException;

public enum DataChannelProtectionLevel {
    CLEAR,
    PRIVATE;

    public static DataChannelProtectionLevel parseFromString(String str) throws FTPServerException {
        return switch (str.toUpperCase()) {
            case "C" -> CLEAR;
            case "P" -> PRIVATE;
            default -> throw new FTPServerException("Invalid data channel protection level: " + str);
        };
    }
}
