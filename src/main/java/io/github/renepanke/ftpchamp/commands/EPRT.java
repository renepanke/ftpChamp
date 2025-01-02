package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.lang.Strings;
import io.github.renepanke.ftpchamp.session.ConnectionMode;
import io.github.renepanke.ftpchamp.session.Session;

import java.net.*;
import java.util.List;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;
import static io.github.renepanke.ftpchamp.lang.Bools.not;

/**
 * EPRT<space><d><net-prt><d><net-addr><d><tcp-port><d>
 */
public class EPRT implements Command {

    private final Command innerCommand;

    public EPRT() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            /*
              Following the space, a delimiter character (<d>) MUST be specified.  The delimiter character MUST be one
              of the ASCII characters in range 33-126 inclusive. The character "|" (ASCII 124) is recommended unless
              it coincides with a character needed to encode the network address.
             */
            char delimiter = argument.charAt(0);
            if (delimiter < 33 || delimiter > 126) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            List<String> splitArg = Strings.splitAt(argument, delimiter);
            if (splitArg.size() != 4) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            /*
               The <net-prt> argument MUST be an address family number defined by IANA in the latest Assigned Numbers
               RFC (RFC 1700 [RP94] as of the writing of this document). This number indicates the protocol to be used
               (and, implicitly, the address length).  This document will use two of address family numbers from [RP94]
               as examples, according to the following table:
               AF Number   Protocol
               ---------   --------
               1           Internet Protocol, Version 4 [Pos81a]
               2           Internet Protocol, Version 6 [DH96]
             */
            String netPrt = splitArg.get(1);
            if (not("1".equals(netPrt)) && not("2".equals(netPrt))) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            /*
              The <net-addr> is a protocol specific string representation of the network address.  For the two address
              families specified above (AF Number 1 and 2), addresses MUST be in the following format:
              AF Number   Address Format      Example
              ---------   --------------      -------
              1           dotted decimal      132.235.1.2
              2           IPv6 string         1080::8:800:200C:417A
                          representations
                          defined in [HD96]
             */
            String netAddr = splitArg.get(2);
            InetAddress addr;
            try {
                addr = InetAddress.getByName(netAddr);
                if ("1".equals(netPrt) && addr instanceof Inet6Address) {
                    Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                    return;
                }
                if ("2".equals(netPrt) && addr instanceof Inet4Address) {
                    Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                    return;
                }
            } catch (UnknownHostException e) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            /*
              The <tcp-port> argument must be the string representation of the number of the TCP port on which the host
              is listening for the data connection.
             */
            String tcpPort = splitArg.get(3);
            int port;
            try {
                port = Integer.parseInt(tcpPort);
            } catch (NumberFormatException e) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            session.setActiveClientDataAddress(addr);
            session.setActiveClientDataPort(port);
            session.setConnectionMode(ConnectionMode.ACTIVE);
            Reply.PositiveCompletion.send_200_CommandOkay(session);
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
