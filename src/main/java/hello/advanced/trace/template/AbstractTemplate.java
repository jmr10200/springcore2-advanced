package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

// <T> : 제네릭. 반환타입 정의
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    protected abstract T call();

    public T execute(String message) {
        TraceStatus status = null;

        try {
            status = trace.begin(message);

            // 로직 호출
            T result = call(); // 핵심기능 : 변하는부분

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
