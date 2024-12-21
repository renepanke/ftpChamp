package io.github.renepanke.session;

public enum FileStructure {
    FILE,
    RECORD,
    PAGE,
    UNKNOWN;

    public static FileStructure parseFromString(String fileStructureString) {
        return switch (fileStructureString.toUpperCase()) {
            case "F" -> FILE;
            case "R" -> RECORD;
            case "P" -> PAGE;
            default -> UNKNOWN;
        };
    }
}
