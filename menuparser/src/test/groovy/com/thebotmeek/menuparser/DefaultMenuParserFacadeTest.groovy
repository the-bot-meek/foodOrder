package com.thebotmeek.menuparser

import io.micronaut.context.event.ApplicationEventPublisher
import spock.lang.Specification

import java.time.Instant

class DefaultMenuParserFacadeTest extends Specification {
    def "add menu parse task"() {
        given:
        MenuParseTaskRepository menuParseTaskRepository = Mock(MenuParseTaskRepository)
        ApplicationEventPublisher applicationEventPublisher = Mock(ApplicationEventPublisher)
        DefaultMenuParserFacade menuParserFacade = new DefaultMenuParserFacade(menuParseTaskRepository, applicationEventPublisher)

        when:
        menuParserFacade.parseMenu(null, "userId", SupportedFileTypes.DOCX)

        then:
        1 * menuParseTaskRepository.save({ MenuParseTask menuParseTask ->
            menuParseTask.getUserId() == "userId"
            menuParseTask.getStatus() == Status.SUBMITTED
            menuParseTask.getTaskId() == null
            menuParseTask.getDateCreated() != null
        })
    }

    def "test get menu parse task"() {
        given:
        MenuParseTaskRepository menuParseTaskRepository = Mock(MenuParseTaskRepository)
        def menuParserFacade = new DefaultMenuParserFacade(menuParseTaskRepository, null)
        MenuParseTask menuParseTask = new MenuParseTask(taskId: "001", userId: "userId", status: Status.SUBMITTED, dateCreated: Instant.ofEpochSecond(100))

        menuParseTaskRepository.getTask(menuParseTask.getTaskId(), menuParseTask.getUserId()) >> Optional.of(menuParseTask)

        when:
        Optional<MenuParseTask> fetchedMenuParseTask = menuParserFacade.getTask(menuParseTask.getTaskId(), menuParseTask.getUserId())

        then:
        assert fetchedMenuParseTask.isPresent()
        assert fetchedMenuParseTask.get() == menuParseTask
    }
}
