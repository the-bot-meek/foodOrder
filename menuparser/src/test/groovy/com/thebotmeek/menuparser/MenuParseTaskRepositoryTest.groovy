package com.thebotmeek.menuparser

import com.foodorder.server.repository.IDynamoDBFacadeRepository
import spock.lang.Specification

class MenuParseTaskRepositoryTest extends Specification {
    def "MenuParseTasks should be saved with the following default properties"() {
        given:
        MenuParseTask menuParseTask = new MenuParseTask("userId")

        IDynamoDBFacadeRepository dynamoDBFacadeRepository = Mock(IDynamoDBFacadeRepository)
        MenuParseTaskRepository menuParseTaskRepository = new MenuParseTaskRepository(dynamoDBFacadeRepository)

        when:
        menuParseTaskRepository.save(menuParseTask)

        then:
        1 * menuParseTaskRepository.save({MenuParseTask newMenuParseTask ->
            newMenuParseTask.getTaskId() != null
            newMenuParseTask.getUserId() == "userId"
            newMenuParseTask.getStatus() == Status.SUBMITTED
            newMenuParseTask.getDateCreated() != null
            newMenuParseTask.getResults() == null
        })
    }

    def "Properties of MenuParseTasks should not be overridden when they are set"() {
        given:
        MenuParseTask menuParseTask = new MenuParseTask(
                userId: "userId",
                taskId: "taskId",
                status: Status.SUCCESS,
                results: []
        )

        IDynamoDBFacadeRepository dynamoDBFacadeRepository = Mock(IDynamoDBFacadeRepository)
        MenuParseTaskRepository menuParseTaskRepository = new MenuParseTaskRepository(dynamoDBFacadeRepository)

        when:
        menuParseTaskRepository.save(menuParseTask)

        then:
        1 * dynamoDBFacadeRepository.save(menuParseTask)
    }
}
