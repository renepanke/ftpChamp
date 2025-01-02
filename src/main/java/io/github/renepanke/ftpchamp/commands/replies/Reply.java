package io.github.renepanke.ftpchamp.commands.replies;

import io.github.renepanke.ftpchamp.session.Session;

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

        public static void send_213_FileStatus(String fileStatus, Session session) {
            session.sendResponse("213 " + fileStatus);
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
            session.sendResponse("227 Entering Passive Mode (" + addressString + "," + (port / 256) + "," + (port % 256) + ").");
        }

        public static void send_229_EnteringExtendedPassiveMode(Session session, int port) {
            session.sendResponse("Entering Extended Passive Mode (|||" + port + "|)");
        }

        public static void send_230_UserLoggedInProceed(Session session) {
            session.sendResponse("230 User logged in, proceed.");
        }

        public static void send_232_UserLoggedInAuthorizedBySecurityDataExchange(Session session) {
            session.sendResponse("232 User logged in, authorized by security data exchange.");
        }

        public static void send_234_SecurityDataExchangeComplete(Session session) {
            session.sendResponse("234 Security data exchange complete.");
        }

        /**
         * This reply indicates that the security data exchange completed successfully.
         *
         * @param base64data
         * @param session
         */
        public static void send_235_ADAT(String base64data, Session session) {
            if (base64data == null) {
                session.sendResponse("235");
                return;
            }
            session.sendResponse("235 ADAT=" + base64data);
        }

        public static void send_250_RequestedFileActionOkayCompleted(Session session) {
            session.sendResponse("250 Requested file action okay, completed.");
        }

        public static void send_250_RequestFileActionOkayCompletedMLST(String listingString, Session session) {
            session.sendResponse("250 Requested file action okay, completed.\r\n" + listingString);
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

        /**
         * This reply indicates that the requested security mechanism is ok, and includes security data to be used by
         * the client to construct the next command.
         *
         * @param base64data
         * @param session
         */
        public static void send_334_ADAT(String base64data, Session session) {
            if (base64data == null) {
                session.sendResponse("334");
                return;
            }
            session.sendResponse("334 ADAT=" + base64data);
        }

        /**
         * This reply indicates that the security data is acceptable, and more is required to complete the security
         * data exchange.
         *
         * @param base64data
         * @param session
         */
        public static void send_335_ADAT(String base64data, Session session) {
            if (base64data == null) {
                session.sendResponse("335");
                return;
            }
            session.sendResponse("335 ADAT=" + base64data);
        }

        /**
         * The exact representation of the challenge should be chosen by the mechanism to be sensible to the human user
         * of the system.
         *
         * @param challenge
         * @param session
         */
        public static void send_336_UsernameOkayNeedPasswordChallengeIs(String challenge, Session session) {
            session.sendResponse("336 Username okay, need password.  Challenge is \"" + challenge + "\"");
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

        public static void send_431_NeedSomeUnavailableResourcesToProcessSecurity(Session session) {
            session.sendResponse("431 Need some unavailable resources to process security.");
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

        public static void send_533_CommandProtectionLevelDeniedForPolicyReasons(Session session) {
            session.sendResponse("533 Command protection level denied for policy reasons.");
        }

        public static void send_534_RequestDeniedForPolicyReasons(Session session) {
            session.sendResponse("534 Request denied for policy reasons.");
        }

        public static void send_535_FailedSecurityCheck(Session session) {
            session.sendResponse("535 Failed security check (hash, sequence, etc).");
        }

        public static void send_536_RequestedPROTLevelNotSupportedByMechanism(Session session) {
            session.sendResponse("536 Requested PROT level not supported by mechanism.");
        }

        public static void send_537_CommandProtectionLevelNotSupportedBySecurityMechanism(Session session) {
            session.sendResponse("537 Command protection level not supported by security mechanism.");
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
