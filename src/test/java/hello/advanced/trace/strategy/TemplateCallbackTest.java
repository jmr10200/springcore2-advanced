package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.template.Callback;
import hello.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class TemplateCallbackTest {

    /**
     * 템플릿 콜백 패턴 - 익명 내부 클래스
     */
    @Test
    void callbackV1() {
        TimeLogTemplate template = new TimeLogTemplate();

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * 템플릿 콜백 패턴 - 람다
     */
    @Test
    void callbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비즈니스 로직1 실행"));
        template.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
/* 템플릿 콜백 패턴 */
// 스프링에서는 ContextV2 와 같은 방식의 전략 패턴을 템플릿 콜백 패턴 이라 한다.
// 전략 패턴에서 Context 가 템플릿 역할을 하고, Strategy 부분이 콜백으로 넘어온다 생각하면 된다.
// 참고로 템플릿 콜백 패턴은 GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 자주 사용하기 때문에, 스프링 안에서만 이렇게 부른다.
// 전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴이라 생각하면 된다.
// 스프링에서는 JdbcTemplate, RestTemplate, TransactionTemplate, RedisTemplate 처럼 다양한 템플릿 콜백 패턴이 사용된다.
// 스프링에서 이름에 XxxTemplate 가 있다면 템플릿 콜백 패턴으로 만들어져있다 생각하면 된다.