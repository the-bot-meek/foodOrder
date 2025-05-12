package com.thebotmeek.menuparser.integration

import com.thebotmeek.menuparser.MenuParseTask
import com.thebotmeek.menuparser.MenuParseTaskRepository
import com.thebotmeek.menuparser.Status
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MenuParseTaskRepositoryIntegrationTest extends Specification {
    @Inject
    MenuParseTaskRepository menuParseTaskRepository

    def "Test adding ParseMenuTask" () {
        given:
        MenuParseTask menuParseTask = new MenuParseTask(
                userId: "userId",
                taskId: "taskId",
                status: Status.SUCCESS,
                results: []
        )

        when:
        MenuParseTask savedMenuParseTask = menuParseTaskRepository.save(menuParseTask)

        then:
        savedMenuParseTask == menuParseTask
    }

    def "Get ParseMenuTask by userId" () {
        given:
        MenuParseTask menuParseTask = new MenuParseTask(
                userId: "userId",
                taskId: "taskId",
                status: Status.SUCCESS,
                results: []
        )
        MenuParseTask saved = menuParseTaskRepository.save(menuParseTask)

        when:
        Optional<MenuParseTask> retrievedMenuParseTask = menuParseTaskRepository.getTask(saved.getTaskId(), "userId")

        then:
        retrievedMenuParseTask.isPresent()
        retrievedMenuParseTask.get() == menuParseTask
    }
}
