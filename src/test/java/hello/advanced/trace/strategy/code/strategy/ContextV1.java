package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 필드에 전략을 보관하는 방식
 * Context (문맥) : 변하지 않는 로직
 * 핵심 : Context 는 Strategy 인터페이스에만 의존한다.
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    // 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드
    // 전략패턴에서 이를 Context (문맥)이라 한다.
    // Context 는 크게 변하지 않지만, 그 문맥 속에서 strategy 를 통해 전략이 변경된다.
    // 스프링에서 의존관계 주입시 사용하는 방식
    public ContextV1(Strategy strategy) {
        this.strategy = strategy; // strategy 의 구현체를 주입
    }

    public void execute() {
        Long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        strategy.call(); // 위임
        // 비즈니스 로직 종료

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);

    }
}
