package io.github.renepanke.session.commands;

import io.github.renepanke.lang.Strings;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.FileStructure;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

public class STRU implements Command {

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        if (argument == null || Strings.isBlank(argument)) {
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            return;
        }

        FileStructure fileStructure = FileStructure.parseFromString(argument);
        switch (fileStructure) {
            case FILE -> Reply.PositiveCompletion.send_200_CommandOkay(session);
            case RECORD, PAGE -> Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
            case UNKNOWN -> Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
        }
    }
}
