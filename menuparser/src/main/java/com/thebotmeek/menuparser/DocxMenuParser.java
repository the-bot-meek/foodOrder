package com.thebotmeek.menuparser;

import com.foodorder.server.models.MenuItem;
import com.thebotmeek.menuparser.exception.MenuParserException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Named("docx")
public class DocxMenuParser implements MenuParser {
    @Override
    public Set<MenuItem> parse(MenuParseEvent menuParseEvent) throws MenuParserException {
        try  {
            String docAsTest = getDocumentAsRawText(menuParseEvent.getInputStream());
            return Set.of();
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
