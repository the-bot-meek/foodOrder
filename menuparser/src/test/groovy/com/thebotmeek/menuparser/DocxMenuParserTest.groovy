package com.thebotmeek.menuparser

import spock.lang.Specification

class DocxMenuParserTest extends Specification {
    private static InputStream getTestFile(String fileName) {
        return new FileInputStream("src/test/groovy/resources/testFiles/" + fileName)
    }

    // Not the best test but I don't think their is a good way to test this.
    def "parser file to plain text"() {
        given:
        InputStream testFile = getTestFile('menu.docx')
        MenuParser menuParser = new DocxMenuParser()

        when:
        String fileAsRawText = menuParser.getDocumentAsRawText(testFile)

        then:
        assert fileAsRawText
        assert fileAsRawText.length() != 0
        assert fileAsRawText.contains("Spaghetti Bolognese")
    }
}
