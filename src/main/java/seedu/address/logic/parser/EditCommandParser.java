package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TASK;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditItemDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    /**
     * Parses the given {@code description} and {@code args} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String description, String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultiMap = new ArgumentMultimap();
        String processArgs = args + " "; // account for the possibility that --tk or --r or --e is given with no space
        try {
            argMultiMap = ArgumentTokenizer.tokenize(processArgs, PREFIX_DESCRIPTION, PREFIX_DATETIME, PREFIX_REMINDER,
                    PREFIX_PRIORITY, PREFIX_TAG, PREFIX_DELETE_TASK, PREFIX_DELETE_REMINDER, PREFIX_DELETE_EVENT);
        } catch (Exception e) {
            logger.info("Failure to tokenize arguments: EditCommand");
            throw new ParseException("Edit command format is incorrect.");
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(description);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditItemDescriptor editItemDescriptor = new EditItemDescriptor();
        if (argMultiMap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            editItemDescriptor.setDescription(
                    ParserUtil.parseDescription(
                            argMultiMap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultiMap.getValue(PREFIX_DATETIME).isPresent()) {
            editItemDescriptor.setEvent(
                    ParserUtil.parseDateTime(
                            argMultiMap.getValue(PREFIX_DATETIME).get()).get());
        }
        if (argMultiMap.getValue(PREFIX_REMINDER).isPresent()) {
            editItemDescriptor.setReminder(
                    ParserUtil.parseReminder(
                            argMultiMap.getValue(PREFIX_REMINDER).get()).get());
        }
        if (argMultiMap.getValue(PREFIX_PRIORITY).isPresent()) {
            editItemDescriptor.setPriority(
                    ParserUtil.parsePriority(
                            argMultiMap.getValue(PREFIX_PRIORITY).get()).get());
        }
        parseTagsForEdit(argMultiMap.getAllValues(PREFIX_TAG)).ifPresent(editItemDescriptor::setTags);

        // if delete tag is present, even if edits are made above, relevant subitems should still be deleted.
        if (argMultiMap.getValue(PREFIX_DELETE_TASK).isPresent()) {
            editItemDescriptor.hasDeleteTask = true;
        }
        if (argMultiMap.getValue(PREFIX_DELETE_REMINDER).isPresent()) {
            editItemDescriptor.hasDeleteReminder = true;
        }
        if (argMultiMap.getValue(PREFIX_DELETE_EVENT).isPresent()) {
            editItemDescriptor.hasDeleteEvent = true;
        }

        if ((!editItemDescriptor.isAnyFieldEdited()) && (!editItemDescriptor.hasAnyDelete())) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editItemDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
