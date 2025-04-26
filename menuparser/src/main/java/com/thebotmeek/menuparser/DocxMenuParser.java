package com.thebotmeek.menuparser;

import com.foodorder.server.models.MenuItem;
import com.thebotmeek.menuparser.exception.MenuParserException;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Named("DOCX")
public class DocxMenuParser implements MenuParser {
    TextToMenuItemParser textToMenuItemParser;
    ObjectMapper objectMapper;

    DocxMenuParser(TextToMenuItemParser textToMenuItemParser, ObjectMapper objectMapper) {
        this.textToMenuItemParser = textToMenuItemParser;
    }

    @Override
    public @Valid Set<MenuItem> parse(MenuParseEvent menuParseEvent) throws MenuParserException {
        try  {
            String docAsTest = getDocumentAsRawText(menuParseEvent.getInputStream());
            String menuItemsJson = textToMenuItemParser.parseMenu(docAsTest);
            return Arrays.stream(objectMapper.readValue(menuItemsJson, MenuItem[].class)).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new MenuParserException(
                    menuParseEvent.getMenuParseTask().getUserId(),
                    menuParseEvent.getMenuParseTask().getTaskId(),
                    e
            );
        }
    }

    private String getDocumentAsRawText(InputStream file) throws Docx4JException {
        MainDocumentPart mainDocumentPart =
                WordprocessingMLPackage.load(file).getMainDocumentPart();
        return mainDocumentPart.getContent().stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
