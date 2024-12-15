package io.github.renepanke.fs;

import io.github.renepanke.exceptions.FTPServerRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.github.renepanke.lang.Bools.not;

public class FileSystem {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystem.class);

    public static String unixLikePermissionString(Path path) {
        LOG.trace("Entering unixLikePermissionString");
        try {
            StringBuilder perms = new StringBuilder();

            // Owner permissions
            perms.append(Files.isReadable(path) ? "r" : "-");
            perms.append(Files.isWritable(path) ? "w" : "-");
            perms.append(Files.isExecutable(path) ? "x" : "-");

            // Group and other permissions (assuming the same as owner for simplicity)
            for (int i = 0; i < 2; i++) {
                perms.append(Files.isReadable(path) ? "r" : "-");
                perms.append(Files.isWritable(path) ? "w" : "-");
                perms.append(Files.isExecutable(path) ? "x" : "-");
            }

            // Add directory or file type indicator
            return (Files.isDirectory(path) ? "d" : "-") + perms;
        } catch (Exception e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static int linkCount(Path path) {
        LOG.trace("Entering linkCount");
        try {
            return Files.isDirectory(path) ? 2 : 1;
        } catch (Exception e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static String owner(Path path) {
        LOG.trace("Entering owner");
        try {
            return Files.getOwner(path).getName();
        } catch (IOException e) {
            throw new FTPServerRuntimeException(e);
        }
    }

    public static String group(Path path) {
        LOG.trace("Entering group");
        try {
            PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
            if (fileAttributeView == null) {
                return "group";
            }
            return fileAttributeView.readAttributes().group().getName();
        } catch (Exception e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static long size(Path path) {
        LOG.trace("Entering size");
        try {
            return Files.readAttributes(path, BasicFileAttributes.class).size();
        } catch (IOException e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static String modificationDate(Path path) {
        LOG.trace("Entering modificationDate");
        try {
            return new SimpleDateFormat("MMM dd HH:mm")
                    .format(new Date(Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime().toMillis()));
        } catch (IOException e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static String name(Path path) {
        LOG.trace("Entering name");
        try {
            return path.getFileName().toString();
        } catch (Exception e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static boolean isDirectory(Path path) {
        try {
            return Files.isDirectory(path);
        } catch (SecurityException e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static boolean isNotADirectory(Path path) {
        return not(isDirectory(path));
    }

    public static Path getParent(Path path) {
        if (path == null) return null;
        try {
            return path.getParent();
        } catch (Exception e) {
            LOG.error("", e);
            return null;
        }
    }
}
