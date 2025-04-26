package com.thebotmeek.controllers;

import com.thebotmeek.menuparser.MenuParseTask;
import com.thebotmeek.menuparser.MenuParserFacade;
import com.thebotmeek.menuparser.SupportedFileTypes;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.authentication.Authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller("menuParseTask")
public class MenuParserController {
    MenuParserFacade menuParserFacade;
    private static final Set<String> supportedFileTypes = Arrays.stream(SupportedFileTypes.values()).map(Object::toString).collect(Collectors.toSet());;

    MenuParserController(MenuParserFacade menuParserFacade) {
        this.menuParserFacade = menuParserFacade;
    }

    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    MenuParseTask parseMenu(CompletedFileUpload file, Authentication authentication) throws IOException {
        String filename = file.getFilename();
        String suffix = filename.contains(".") ? filename.substring(filename.lastIndexOf('.')).toUpperCase() : null;
        if (!supportedFileTypes.contains(suffix)) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type");
        }
        SupportedFileTypes supportedFileTypes = SupportedFileTypes.valueOf(file.getFilename());

        return menuParserFacade.parseMenu(file.getInputStream(), authentication.getName(), supportedFileTypes);
    }

    @Get("/{taskId}")
    Optional<MenuParseTask> getTask(String taskId, Authentication authentication) {
        return menuParserFacade.getTask(taskId, authentication.getName());
    }
}
