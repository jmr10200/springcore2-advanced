package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {
        TraceStatus status = null;

        try {
            // 파라미터로 전달받은 traceId 를 이용, beginSync() 를 호출
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            // beginSync() 동작
            // 내부에서 다음 traceId 생성, 트랜젝션 ID 유지, level +1

            // beginSync() 가 반환한 TraceStatus 로부터 traceId를 불러와 리포지토리 save() 호출&전달
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
