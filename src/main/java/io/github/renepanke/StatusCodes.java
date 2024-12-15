package io.github.renepanke;

public class StatusCodes {

    private static final int USER_LOGGER_IN_PROCEED = 230;
    private static final int IS_CURRENT_WORKING_DIRECTORY = 257;
    private static final int USERNAME_OK_NEED_PASSWORD = 331;
    private static final int COMMAND_NOT_IMPLEMENTED = 502;
    private static final int FAILED_TO_CHANGE_DIRECTORY = 550;


    public static String userNameOkNeedPassword() {
        return USERNAME_OK_NEED_PASSWORD + " User name okay, need password";
    }

    public static String commandNotImplemented() {
        return COMMAND_NOT_IMPLEMENTED + " Command not implemented";
    }

    public static String userLoggedInProceed() {
        return USER_LOGGER_IN_PROCEED + " User logged in, proceed";
    }

    public static String isCurrentWorkingDirectory(String workingDirectory) {
        return IS_CURRENT_WORKING_DIRECTORY + " \"" + workingDirectory + "\" is the current working directory";
    }

    public static String failedToChangeDirectory() {
        return FAILED_TO_CHANGE_DIRECTORY + " Requested file action not taken; File unavailable";
    }
}
