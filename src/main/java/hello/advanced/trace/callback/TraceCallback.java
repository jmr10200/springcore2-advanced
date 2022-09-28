package hello.advanced.trace.callback;

public interface TraceCallback<T> {
    T call();
    // 콜백의 반환 타입 : 제네릭 사용
}
