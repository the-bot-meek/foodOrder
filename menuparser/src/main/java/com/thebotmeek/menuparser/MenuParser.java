package com.thebotmeek.menuparser;

import com.foodorder.server.models.MenuItem;
import com.thebotmeek.menuparser.exception.MenuParserException;

import java.io.InputStream;
import java.util.Set;

public interface MenuParser {
    Set<MenuItem> parse(MenuParseEvent inputStream) throws MenuParserException;
}
