package codeit.api.post.handler;

import codeit.api.history.service.HistoryService;
import codeit.api.post.dto.event.PostHitEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostEventHandler {
    private final HistoryService historyService;
    @Async
    @EventListener
    public void handlePostHitEvent(PostHitEvent event){
        historyService.saveHistory(event.getUserId(), event.getPostId());
    }
}
