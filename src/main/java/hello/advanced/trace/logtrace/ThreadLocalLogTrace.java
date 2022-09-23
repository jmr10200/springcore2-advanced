package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        // 로그 시작시 호출
        syncTraceId();

        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        // 종료 시간 계산
        Long stopTimeMs = System.currentTimeMillis();
        Long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
        // 로그 종료시 호출
        releaseTraceId();
    }

    /**
     * 로그 시작시 호출
     */
    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            // traceId 가 없으면 traceId 를 새로 생성
            traceIdHolder.set(new TraceId());
        } else {
            // traceId 가 있으면 next traceId (level + 1이 수행됨)
            traceIdHolder.set(traceId.createNextId());
        }
    }

    /**
     * 로그 종료시 호출
     */
    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            // traceId 의 level 이 0 이면
            // ThreadLocal 을 모두 사용한 것이므로 remove() 로 제거시켜야 한다.
            traceIdHolder.remove(); // destroy
        } else {
            // traceId 의 level 이 0 보다 크면, previous traceId 생성 (level -1이 수행됨)
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private static String addSpace(String startPrefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + startPrefix : "|    ");
        }
        return sb.toString();
    }
}
