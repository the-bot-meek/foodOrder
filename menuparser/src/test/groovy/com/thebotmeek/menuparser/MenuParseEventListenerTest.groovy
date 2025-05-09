package com.thebotmeek.menuparser

import com.thebotmeek.menuparser.exception.MenuParserException
import spock.lang.Specification

class MenuParseEventListenerTest extends Specification {
    def "should update the task to be successful"() {
        given:
        MenuParseTask menuParseTask = new MenuParseTask(userId: "userId", taskId: "taskId")
        MenuParseEvent menuParseEvent = new MenuParseEvent(menuParseTask: menuParseTask, inputStream: null, supportedFileTypes: SupportedFileTypes.DOCX)

        MenuParseTaskRepository menuParseTaskRepository = Mock(MenuParseTaskRepository)

        MenuParser docxMenuParser = Mock(MenuParser)
        docxMenuParser.parse(_ as MenuParseEvent) >> Set.of()


        Map<String, MenuParser> menuParsers = [
                "DOCX": docxMenuParser
        ]

        MenuParseEventListener menuParseEventListener = new MenuParseEventListener(
                menuParsers,
                menuParseTaskRepository
        )

        when:
        menuParseEventListener.onApplicationEvent(menuParseEvent)

        then:
        1 * menuParseTaskRepository.save({MenuParseTask mpt -> mpt.status == Status.SUCCESS})
    }

    def "should update the task to ERROR when a exception is thrown"() {
        given:
        MenuParseTask menuParseTask = new MenuParseTask(userId: "userId", taskId: "taskId")
        MenuParseEvent menuParseEvent = new MenuParseEvent(menuParseTask: menuParseTask, inputStream: null, supportedFileTypes: SupportedFileTypes.DOCX)

        MenuParseTaskRepository menuParseTaskRepository = Mock(MenuParseTaskRepository)

        MenuParser docxMenuParser = Mock(MenuParser)
        docxMenuParser.parse(_ as MenuParseEvent) >> {throw new MenuParserException()}


        Map<String, MenuParser> menuParsers = [
                "DOCX": docxMenuParser
        ]

        MenuParseEventListener menuParseEventListener = new MenuParseEventListener(
                menuParsers,
                menuParseTaskRepository
        )

        when:
        menuParseEventListener.onApplicationEvent(menuParseEvent)

        then:
        1 * menuParseTaskRepository.save({MenuParseTask mpt -> mpt.status == Status.ERROR})
    }
}
