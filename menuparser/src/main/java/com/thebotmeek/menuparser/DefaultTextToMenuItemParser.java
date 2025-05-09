package com.thebotmeek.menuparser;

import dev.langchain4j.service.SystemMessage;
import io.micronaut.langchain4j.annotation.AiService;
import org.w3c.dom.Text;

@AiService
public interface DefaultTextToMenuItemParser extends TextToMenuItemParser {
    @SystemMessage("""
    You will be given a block of text that represents a menu. Please find the following information for each menu item.
    You will respond in json format with the following headers. name, description, price, menuItemCategory.
    
    name should be a String,
    description should be a String
    price should be a number with 2 decimal places.
    menuItemCategory should be one of the following strings: MAIN, STARTER, DESSERT, DRINK, SIDE.
    
    You will respond with pure json do not include any formatting.
    """)
    String parseMenu(String menuText);
}
