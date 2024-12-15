package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.DataTransferType;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

import java.util.Optional;

public class Type implements Command {

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr503NotLoggedIn();
        Optional<DataTransferType> type = DataTransferType.fromTypeCodeLiteral(argument);
        if (type.isEmpty()) Reply.PermanentNegativeCompletion.send_504_CommandNotImplementedForThatParameter(session);

        session.setDataTransferType(type.get());
        Reply.PositiveCompletion.send_200_CommandOkay(session);
    }
}