package io.github.renepanke.ftpchamp.session;

import java.util.Optional;

public enum DataTransferType {
    IMAGE("I"),
    ASCII("A"),
    UNSUPPORTED;

    private String a;

    DataTransferType() {

    }

    DataTransferType(String a) {
        this.a = a;
    }

    public static Optional<DataTransferType> fromTypeCodeLiteral(String literal) {
        return switch (literal) {
            case "I" -> Optional.of(IMAGE);
            case "A" -> Optional.of(ASCII);
            default -> Optional.empty();
        };
    }
}
