package io.github.renepanke.ftpchamp.session;

import io.github.renepanke.ftpchamp.exceptions.FTPServerException;

public enum TransferMode {

    STREAM,
    BLOCK,
    COMPRESSED;

    public static TransferMode parseFromString(String transferModeString) throws FTPServerException {
        return switch (transferModeString.toUpperCase()) {
            case "S" -> STREAM;
            case "B" -> BLOCK;
            case "C" -> COMPRESSED;
            default -> throw new FTPServerException("Unknown Transfer Mode <" + transferModeString + ">.");
        };
    }
}
