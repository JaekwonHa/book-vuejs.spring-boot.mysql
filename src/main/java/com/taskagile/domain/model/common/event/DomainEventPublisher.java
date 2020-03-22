package com.taskagile.domain.model.common.event;

public interface DomainEventPublisher {

    /**
     * Publish a domain event
     */
    void publish(DomainEvent event);
}
