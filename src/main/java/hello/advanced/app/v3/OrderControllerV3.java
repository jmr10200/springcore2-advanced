package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request") // HTTP 파라미터로 itemId
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = trace.begin("OrderController.request()"); // 로그추적기 시작
            orderService.orderItem(itemId);
            trace.end(status); // 로그추적기 끝
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 꼭 다시 던져줘야 함
        }
        // 문제 : begin(), end() 뿐만아니라 exception() 도 처리해줘야한다. 더불어 try~catch 도 추가된다.
    }
}
/* v0 과 비교했을 때 문제점 */
// 핵심기능인 주문 로직 orderService.orderItem(itemId) 을 제외한,
// 부가기능인 로그 추적기의 로직이 더 많고, 변하지 않으며 반복적으로 사용된다.

/* 변하는 것과 변하지 않는 것을 분리 */
// 좋은 설계는 변하는 것과 변하지 않는 것을 분리하는 것이다.
// 여기서 핵심기능은 변하고, 부가기능은 변하지않는다.
// 이 둘을 분리해서 모듈화 해야한다.
// 템플릿 메소드 패턴 (Template Method Pattern) 은 이런 문제를 해결하는 디자인 패턴이다.
