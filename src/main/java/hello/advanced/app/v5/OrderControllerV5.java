package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request") // HTTP 파라미터로 itemId
    public String request(String itemId) {

        // 템플릿 실행 & 로그로 남길 message 전달
        return template.execute("OrderController.request()", new TraceCallback<>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });
    }
}
/* 템플릿 콜백 패턴 적용 */
// 한계
// 결국 로그 추적기를 적용하기 위해서 원본 코드를 수정해야 한다는 한계가 따른다.
// 원본 코드를 손대지 않고 로그 추적기를 사용하기 위해서는 프록시 개념을 알아야 한다.