package io.github.renepanke.ftpchamp.session;

import io.github.renepanke.ftpchamp.exceptions.FTPServerException;

public enum SecurityMechanism {
    UNINITIALIZED,
    TLS,
    SSL,
    GSSAPI,
    KERBEROS;

    public static SecurityMechanism parseFromString(String securityMechanismString) throws FTPServerException {
        return switch (securityMechanismString.toUpperCase()) {
            case "TLS" -> TLS;
            case "SSL" -> SSL;
            case "GSSAPI" -> GSSAPI;
            case "KERBEROS" -> KERBEROS;
            default -> throw new FTPServerException("Unknown FTP Security Mechanism <" + securityMechanismString + ">");
        };
    }
}
