package com.taskagile.web.socket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChannelHandlerResolverTests {

    @ChannelHandler("/boards")
    private static class BoardsChannelHandler {

        @Action("subscribe")
        public void subscribe(RealTimeSession session) {
        }
    }

    @ChannelHandler("/board/*")
    private static class BoardChannelHandler {

        @Action("subscribe")
        public void subscribe(RealTimeSession session) {
        }
    }

    @ChannelHandler("/boards")
    private static class DuplicateBoardsChannelHandler {

        @Action("subscribe")
        public void subscribe(RealTimeSession session) {
        }
    }

    //-----------------------------------------
    // Constructor
    //-----------------------------------------
    @Test
    public void constructor_emptyHandlers_shouldSucceed() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(new HashMap<>());
        Exception exception = null;
        try {
            new ChannelHandlerResolver(applicationContextMock);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    public void constructor_duplicateHandlersForSameChannel_shouldFail() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("duplicateBoardsChannelHandler", new DuplicateBoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);

        Exception exception = null;
        try {
            new ChannelHandlerResolver(applicationContextMock);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Duplicated handlers found for channel pattern `/boards`.", exception.getMessage());
    }

    @Test
    public void constructor_validHandlers_shouldSucceed() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);
        Exception exception = null;
        try {
            new ChannelHandlerResolver(applicationContextMock);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
    }

    //-----------------------------------------
    // Method findInvoker()
    //-----------------------------------------

    @Test
    public void findInvoker_noChannelHandler_shouldReturnNull() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);
        ChannelHandlerResolver resolver = new ChannelHandlerResolver(applicationContextMock);
        ChannelHandlerInvoker invoker = resolver.findInvoker(IncomingMessage.create("/abc", "test", ""));

        assertNull(invoker);
    }

    @Test
    public void findInvoker_boardChannelWithBoardId_shouldReturnHandler() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);
        ChannelHandlerResolver resolver = new ChannelHandlerResolver(applicationContextMock);
        ChannelHandlerInvoker invoker = resolver.findInvoker(IncomingMessage.create("/board/1", "subscribe", ""));

        assertNotNull(invoker);
        assertTrue(invoker.supports("subscribe"));
    }

    @Test
    public void findInvoker_boardChannelWithNoBoardId_shouldReturnHandler() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);
        ChannelHandlerResolver resolver = new ChannelHandlerResolver(applicationContextMock);
        ChannelHandlerInvoker invoker = resolver.findInvoker(IncomingMessage.create("/board/", "subscribe", ""));

        assertNotNull(invoker);
        assertTrue(invoker.supports("subscribe"));
    }

    @Test
    public void findInvoker_boardChannelWithNotSupportedAction_shouldReturnNull() {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        Map<String, Object> handlers = new HashMap<>();
        handlers.put("boardsChannelHandler", new BoardsChannelHandler());
        handlers.put("boardChannelHandler", new BoardChannelHandler());
        when(applicationContextMock.getBeansWithAnnotation(ChannelHandler.class)).thenReturn(handlers);
        ChannelHandlerResolver resolver = new ChannelHandlerResolver(applicationContextMock);
        ChannelHandlerInvoker invoker = resolver.findInvoker(IncomingMessage.create("/board/2", "invalidAction", ""));

        assertNull(invoker);
    }
}
