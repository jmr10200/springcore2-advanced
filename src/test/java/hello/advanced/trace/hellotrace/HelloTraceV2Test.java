package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTraceV2Test {

    @Test
    void begin_level2() {
        HelloTraceV2 trace = new HelloTraceV2();
        // 시작 : begin() 사용
        TraceStatus status1 = trace.begin("hello1");
        // 다음 : beginSync() 사용
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception_level2() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("hello");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }

}
/* V2 의 문제점 */
// HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 동기화가 필요하다.
// TraceId 의 동기화를 위해서 관련 메소드의 모든 파라미터를 수정해야 한다.
// -> 만약 인터페이스가 있으면 인터페이스까지 모두 고쳐야 하는 상황이다.
// 로그를 처음 실행할 때는 begin() 을, 처음이 아닐 때는 beginSync()를 호출해야 한다.
// -> 만약 컨트롤러를 통해서 서비스를 호출하는 것이 아니라, 다른 곳에서 서비스를 처음으로 호출하는 상황이라면 파라미터로 넘길 TraceId 가없다.
// 즉, HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 를 파라미터로 넘기는 것 외의 다른 대안이 필요하다.