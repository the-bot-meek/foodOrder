package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateMenuRequestConverter;
import com.foodorder.server.exceptions.MenuRequestConverterException;
import com.foodorder.server.models.Menu;
import com.foodorder.server.request.CreateMenuRequest;
import com.foodorder.server.repository.MenuRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller("menu")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MenuController {
    final private Logger log = LoggerFactory.getLogger(MenuController.class);
    final private MenuRepository menuRepository;
    final private CreateMenuRequestConverter createMenuRequestConverter;

    public MenuController(MenuRepository menuRepository, CreateMenuRequestConverter createMenuRequestConverter) {
        this.menuRepository = menuRepository;
        this.createMenuRequestConverter = createMenuRequestConverter;
    }

    @Get("/{location}/{name}")
    public Optional<Menu> getMenu(String location, String name) {
        log.info("Getting menu. location: {}, name: {}", location, name);
        return menuRepository.getMenu(location, name);
    }

    @Get("/{location}")
    public List<Menu> listMenusForLocation(String location) {
        log.info("Getting all Menus for location: {}", location);
        return menuRepository.listMenus(location);
    }

    @Post
    public HttpResponse<Menu> addMenu(@Body @Valid CreateMenuRequest createMenuRequest) {
        log.info("Adding Menu. createMenuRequest: {}", createMenuRequest);
        try {
            Menu menu = createMenuRequestConverter.convertCreateMenulRequestIntoMenu(createMenuRequest);
            menuRepository.addMenu(menu);
            return HttpResponse.ok(menu);
        } catch (MenuRequestConverterException e) {
            log.error("Error converting CreateMenuRequest to Menu", e);
            return HttpResponse.badRequest();
        }
    }
}
