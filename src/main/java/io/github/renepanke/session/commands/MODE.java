package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.lang.Strings;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.TransferMode;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MODE implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(MODE.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        if (argument == null || Strings.isBlank(argument) || argument.length() != 1) {
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            return;
        }

        try {
            TransferMode transferMode = TransferMode.parseFromString(argument);
            session.setTransferMode(transferMode);
            // Maybe also 504 Command not implemented for that parameter if it's decided to only support STREAM or S.
            Reply.PositiveCompletion.send_200_CommandOkay(session);
        } catch (FTPServerException e) {
            LOG.atError().setCause(e).log("Can't set transfer mode.");
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
        }
    }
}
