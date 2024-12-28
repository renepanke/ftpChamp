package io.github.renepanke.ftpchamp.commands.shared;

import io.github.renepanke.ftpchamp.fs.FileSystem;
import io.github.renepanke.ftpchamp.session.Session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MachineListing {

    private MachineListing() {
        throw new AssertionError();
    }


    public static String compile(Path target, Session session) {
        StringBuilder listing = new StringBuilder();
        getSize(target).ifPresent(x -> listing.append("size=").append(x).append("\r\n"));
        getModify(target).ifPresent(x -> listing.append("modify=").append(x).append("\r\n"));
        getCreate(target).ifPresent(x -> listing.append("create=").append(x).append("\r\n"));
        getType(target, session).ifPresent(x -> listing.append("type=").append(x).append("\r\n"));
        getUnique(target).ifPresent(x -> listing.append("unique=").append(x).append("\r\n"));
        getPerm(target).ifPresent(x -> listing.append("perm=").append(x).append("\r\n"));
        getLang(target).ifPresent(x -> listing.append("lang=").append(x).append("\r\n"));
        getMediaType(target).ifPresent(x -> listing.append("media-type=").append(x).append("\r\n"));
        getCharset().ifPresent(x -> listing.append("charset=").append(x).append("\r\n"));
        return listing.toString();
    }

    private static Optional<String> getCharset() {
        return Optional.of("UTF-8");
    }

    private static Optional<String> getMediaType(Path target) {
        return FileSystem.userDefinedFileAttribute(target, "Media-Type");
    }

    private static Optional<String> getLang(Path target) {
        return FileSystem.userDefinedFileAttribute(target, "lang");
    }

    private static Optional<String> getPerm(Path target) {
        List<String> p = new LinkedList<>();
        if (Files.isDirectory(target)) {
            if (Files.isReadable(target)) {
                p.add("e");
                p.add("l");
            }
            if (Files.isWritable(target)) {
                p.add("c");
                p.add("d");
                p.add("f");
                p.add("m");
                p.add("p");
            }
        } else {
            if (Files.isReadable(target)) {
                p.add("r");
                p.add("w");
            }
            if (Files.isWritable(target)) {
                p.add("a");
                p.add("d");
                p.add("f");
            }
        }
        return Optional.of(String.join("", p));
    }

    private static Optional<String> getUnique(Path target) {
        return Optional.of(target).map(Path::toAbsolutePath).map(Path::toString);
    }

    private static Optional<String> getType(Path target, Session session) {
        if (FileSystem.isDirectory(target)) {
            if (session.getWorkingDirectory().equals(target)) {
                return Optional.of("cdir");
            }
            if (session.getWorkingDirectory().getParent().equals(target)) {
                return Optional.of("pdir");
            }
            return Optional.of("dir");
        }
        return Optional.of("file");
    }

    private static Optional<String> getCreate(Path target) {
        return Optional.of(FileSystem.creationDateTime(target));
    }

    private static Optional<String> getModify(Path target) {
        return Optional.of(FileSystem.modificationDateTime(target));
    }

    private static Optional<String> getSize(Path target) {
        return Optional.of(FileSystem.size(target)).map(String::valueOf);
    }
}
