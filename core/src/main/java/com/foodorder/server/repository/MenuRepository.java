package com.foodorder.server.repository;

import com.foodorder.server.models.Menu;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;


@Singleton
public class MenuRepository {
    private final Logger log = LoggerFactory.getLogger(MenuRepository.class);
    private final IDynamoDBFacadeRepository dynamoDBFacadeRepository;

    public MenuRepository(
            @Named("primary-table") IDynamoDBFacadeRepository dynamoDBFacadeRepository
    ) {
        this.dynamoDBFacadeRepository = dynamoDBFacadeRepository;
    }

    public Optional<Menu> getMenu(String location, String name) {
        log.trace("Getting Menu location: {}, name: {}", location, name);
        return dynamoDBFacadeRepository.load(Menu.class,"Menu_" + location, name);
    }

    public List<Menu> listMenus(String location) {
        final String pk = "Menu_" + location;
        log.trace("Getting all menus for location:{}", location);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional conditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeRepository.query(Menu.class, conditional);
    }



    public void addMenu(Menu menu) {
        log.trace("Adding Menu, menuId: {}", menu.getId());
        dynamoDBFacadeRepository.save(menu);
    }
}
