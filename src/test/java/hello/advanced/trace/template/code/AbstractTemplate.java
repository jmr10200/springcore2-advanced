package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    protected abstract void call();

    // 핵심기능(bizLogic) : 추상화
    // 부가기능 : 변하지 않음
    public void execute() {
        Long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        call(); // 상속
        // 비즈니스 로직 종료

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
