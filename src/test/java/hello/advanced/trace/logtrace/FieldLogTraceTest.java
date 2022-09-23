package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FieldLogTraceTest {

    FieldLogTrace trace = new FieldLogTrace();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception_level2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }
}
/* FieldLogTrace 의 동시성 문제 */
// 현재 상품을 저장하는데 1초 (1000ms) 소요됨을 가정하고 있으므로 1초안에 2번 실행해보면 동시성 문제가 발생한다.
// 동시에 여러 사용자가 요청하면 여러 쓰레드가 동시에 어플리케이션 로직을 호출하게 된다.
// 따라서 로그를 확인해보면 뒤섞여 출력된다.

// 출력 로그확인 (1초에 2회 요청)
// [4dcb0ca2] OrderController.request()
// [4dcb0ca2] |-->OrderService.orderItem()
// [4dcb0ca2] |    |-->OrderRepository.save()
// [4dcb0ca2] |    |    |-->OrderController.request()
// [4dcb0ca2] |    |    |    |-->OrderService.orderItem()
// [4dcb0ca2] |    |    |    |    |-->OrderRepository.save()
// [4dcb0ca2] |    |<--OrderRepository.save() time=1005ms
// [4dcb0ca2] |<--OrderService.orderItem() time=1012ms
// [4dcb0ca2] OrderController.request() time=1016ms
// [4dcb0ca2] |    |    |    |    |<--OrderRepository.save() time=1003ms
// [4dcb0ca2] |    |    |    |<--OrderService.orderItem() time=1003ms
// [4dcb0ca2] |    |    |<--OrderController.request() time=1005ms

// 문제 확인
// 1. 두번의 요청에 대한 traceId 가 동일하다
// 2. level 이 많이 꼬였다. 로그가 뒤섞여 출력된다.
// 이러한 문제를 동시성 문제라 한다.

/* 동시성 문제 */
// FieldLogTrace 는 싱글톤으로 등록된 스프링 빈이다.
// 즉 이 객체의 인스턴스가 어플리케이션에 1개 존재한다는 의미이다.
// 1개만 있는 인스턴스의 FieldLogTrace.traceHolder 필드를 여러 쓰레드가 동시에 접근하여 발생한다.