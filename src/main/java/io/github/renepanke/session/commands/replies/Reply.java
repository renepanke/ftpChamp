package io.github.renepanke.session.commands.replies;

import io.github.renepanke.session.Session;

import java.net.InetAddress;

public class Reply {

    /**
     * 1xx
     */
    public static class PositivePreliminary {
        /**
         * In this case, the text is exact and not left to the particular implementation; it must read: MARK yyyy = mmmm
         * Where yyyy is User-process data stream marker, and mmmm server's equivalent marker
         * (note the spaces between markers and "=").
         *
         * @param session
         */
        public static void send_110_RestartMarkerReply(Session session) {
            session.sendResponse("110 Restart marker reply.");
        }

        public static void send_120_ServiceReadyInNNNMinutes(int minutes, Session session) {
            session.sendResponse("120 Service ready in " + minutes + " minutes.");
        }

        public static void send_125_DataConnectionAlreadyOpenTransferStarting(Session session) {
            session.sendResponse("125 Data connection already open; transfer starting.");
        }

        public static void send_150_FileStatusOkayAboutToOpenDataConnection(Session session) {
            session.sendResponse("150 File status okay; about to open data connection.");
        }
    }

    /**
     * 2xx
     */
    public static class PositiveCompletion {

        public static void send_200_CommandOkay(Session session) {
            session.sendResponse("200 Command okay.");
        }

        public static void send_202_CommandNotImplementedSuperflousAtThisSite(Session session) {
            session.sendResponse("202 Command not implemented, superfluous at this site.");
        }

        public static void send_211_SystemStatusOrSystemHelpReply(Session session) {
            session.sendResponse("211 System status, or system help reply.");
        }

        public static void send_212_DirectoryStatus(Session session) {
            session.sendResponse("212 Directory status.");
        }

        public static void send_213_FileStatus(Session session) {
            session.sendResponse("213 File status.");
        }

        /**
         * On how to use the server or the meaning of a particular non-standard command.  This reply is useful only to the
         * human user.
         *
         * @param session
         */
        public static void send_214_HelpMessage(Session session) {
            session.sendResponse("214 Help message.");
        }

        public static void send_220_ServiceReadyForNewUser(Session session) {
            session.sendResponse("220 Service ready for new user.");
        }

        /**
         * Logged out if appropriate.
         *
         * @param session
         */
        public static void send_221_ServiceClosingControlConnection(Session session) {
            session.sendResponse("221 Service closing control connection.");
        }

        public static void send_225_DataConnectionOpenNoTransferInProgress(Session session) {
            session.sendResponse("225 Data connection open; no transfer in progress.");
        }

        /**
         * Requested file action successful (for example, file transfer or file abort).
         *
         * @param session
         */
        public static void send_226_ClosingDataConnection(Session session) {
            session.sendResponse("226 Closing data connection.");
        }

        public static void send_227_EnteringPassiveMode(Session session, InetAddress address, int port) {
            String addressString = address.getHostAddress().replace(".", ",");
            session.sendResponse("227 Entering Passive Mode (" + addressString + "," + (port / 256) + "," + (port % 256) + ".");
        }

        public static void send_230_UserLoggedInProceed(Session session) {
            session.sendResponse("230 User logged in, proceed.");
        }

        public static void send_250_RequestedFileActionOkayCompleted(Session session) {
            session.sendResponse("250 Requested file action okay, completed.");
        }

        public static void send_257_PathnameCreated(String pathName, Session session) {
            session.sendResponse("257 \"" + pathName + "\" created.");
        }
    }

    /**
     * 3xx
     */
    public static class PositiveIntermediate {

        public static void send_331_UsernameOkayNeedPassword(Session session) {
            session.sendResponse("331 User name okay, need password.");
        }

        public static void send_332_NeedAccountForLogin(Session session) {
            session.sendResponse("332 Need account for login.");
        }

        public static void send_350_RequestedFileActionPendingFurtherInformation(Session session) {
            session.sendResponse("350 Requested file action pending further information.");
        }
    }

    /**
     * 4xx
     */
    public static class TransientNegativeCompletion {
        /**
         * This may be a reply to any command if the service knows it must shut down.
         *
         * @param session
         */
        public static void send_421_ServiceNotAvailable(Session session) {
            session.sendResponse("421 Service not available, closing control connection.");
        }

        public static void send_425_CantOpenDataConnection(Session session) {
            session.sendResponse("425 Can't open data connection.");
        }

        public static void send_426_ConnectionClosedTransferAborted(Session session) {
            session.sendResponse("426 Connection closed; transfer aborted.");
        }

        /**
         * File unavailable (e.g., file busy).
         *
         * @param session
         */
        public static void send_450_RequestedFileActionNotTaken(Session session) {
            session.sendResponse("450 Requested file action not taken.");
        }

        public static void send_451_RequestedActionAbortedLocalErrorInProcessing(Session session) {
            session.sendResponse("451 Requested action aborted: local error in processing.");
        }

        /**
         * Insufficient storage space in system.
         *
         * @param session
         */
        public static void send_452_RequestedActionNotTakenInsufficientStorageSpaceInSystem(Session session) {
            session.sendResponse("452 Requested action not taken.");
        }
    }

    /**
     * 5xx
     */
    public static class PermanentNegativeCompletion {
        /**
         * This may include errors such as command line too long.
         *
         * @param session
         */
        public static void send_500_SyntaxErrorCommandUnrecognized(Session session) {
            session.sendResponse("500 Syntax error, command unrecognized.");
        }

        public static void send_501_SyntaxErrorInParametersOrArguments(Session session) {
            session.sendResponse("501 Syntax error in parameters or arguments.");
        }

        public static void send_502_CommandNotImplemented(Session session) {
            session.sendResponse("502 Command not implemented.");
        }

        public static void send_503_BadSequenceOfCommands(Session session) {
            session.sendResponse("503 Bad sequence of commands.");
        }

        public static void send_504_CommandNotImplementedForThatParameter(Session session) {
            session.sendResponse("504 Command not implemented for that parameter.");
        }

        public static void send_530_NotLoggedIn(Session session) {
            session.sendResponse("530 Not logged in.");
        }

        public static void send_532_NeedAccountForStoringFiles(Session session) {
            session.sendResponse("532 Need account for storing files.");
        }

        /**
         * File unavailable (e.g., file not found, no access).
         *
         * @param session
         */
        public static void send_550_RequestedActionNotTakenFileUnavailable(Session session) {
            session.sendResponse("550 Requested action not taken.");
        }

        public static void send_551_RequestedActionAbortedPageTypeUnknown(Session session) {
            session.sendResponse("551 Requested action aborted: page type unknown.");
        }

        /**
         * Exceeded storage allocation (for current directory or dataset).
         *
         * @param session
         */
        public static void send_552_RequestedFileActionAborted(Session session) {
            session.sendResponse("552 Requested file action aborted.");
        }

        /**
         * File name not allowed.
         *
         * @param session
         */
        public static void send_553_RequestedActionNotTakenFileNameNotAllowed(Session session) {
            session.sendResponse("553 Requested action not taken.");
        }
    }

}
