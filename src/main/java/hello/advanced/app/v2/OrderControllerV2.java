package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request") // HTTP 파라미터로 itemId
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = trace.begin("OrderController.request()"); // 로그추적기 시작
            orderService.orderItem(status.getTraceId(), itemId);
            trace.end(status); // 로그추적기 끝
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 꼭 다시 던져줘야 함
        }
        // 문제 : begin(), end() 뿐만아니라 exception() 도 처리해줘야한다. 더불어 try~catch 도 추가된다.
    }
}
