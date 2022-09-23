package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ThreadLocalLogTraceTest {

    ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

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
/* ThreadLocal 주의사항 */
// 쓰레드 로컬을 사용할 때 주의해야할 것이 있다.
// 사용 후 값을 remove() 로 제거하지 않으면, WAS(톰캣) 처럼 쓰레드 풀을 사용하는 경우 심각한 문제가 발생할 수 있다.

// 예를들어
// userA 가 HTTP 요청을 한다.
// WAS 는 쓰레드풀에서 Thread-1 을 할당한다.
// Thread-1 로 데이터를 보관한다.
// userA 는 Thread-1 의 값을 제거하지 않고, 쓰레드풀에 Thread-1 을 반납한다
// (쓰레드 생성비용은 비싸기 때문에 쓰레드 풀에 보관해놓고 재사용한다)
// userX 가 HTTP 요청을 한다.
// WAS 는 쓰레드풀에서 Thread-1 을 할당한다.
// userX 의 데이터를 조회한다.
// Thread-1 로 조회된 데이터가 userA 의 데이터가 된다.

// 즉, userX 가 userA 의 정보를 조회하게되는 심각한 문제가 발생한다.
// 이러한 문제 때문에
// 쓰레드 로컬을 사용하고 난 후에는 꼭 ThreadLocal.remove() 를 호출해 값을 제거해야한다.