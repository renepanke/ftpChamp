package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.FileRenameStatus;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

public class RNFR implements Command {

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        FileRenameStatus status = session.initializeFileRename(argument);
        switch (status) {
            case INITIALIZED -> Reply.PositiveIntermediate.send_350_RequestedFileActionPendingFurtherInformation(session);
            case FILE_NOT_FOUND, ERROR, UNINITIALIZED -> Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
        }
    }
}
