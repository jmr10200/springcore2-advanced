package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1();
        // Context 에 strategy 구현체(전략)를 주입하여 사용
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();

    }

    /**
     * 전략 패턴 익명 내부 클래스1
     */
    @Test
    void strategyV2() {
        Strategy strategyLogic1 = new StrategyLogic1(){
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("strategyLogic1={}", strategyLogic1.getClass());
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new StrategyLogic2(){
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("strategyLogic2={}", strategyLogic2.getClass());
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * 전략 패턴 익명 내부 클래스2
     */
    @Test
    void strategyV3() {
        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        context2.execute();
    }

    /**
     * 전략 패턴, 람다
     */
    @Test
    void strategyV4() {
        // 인터페이스에 메소드가 1개만 존재하는 경우 : 람다 가능
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
        context2.execute();
    }

    private void logic1() {
        Long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        Long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        // 비즈니스 로직 종료

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}

/* 전략 패턴 */
// 변하지 않는 부분 : Context (문맥)
// 변하는 부분 : Strategy 구현해서 만들기

// 선 조립, 후 실행
// Context 의 내부 필드에 Strategy 를 두고 사용 하는 부분이다.
// 이 방식은 Context 와 Strategy 를 실행 전에 원하는 모양으로 조립해두고,
// 그 다음에 Context 를 실행하는 선 조립, 후 실행 방식에서 매우 유용하다.
// Context 와 Strategy 를 한번 조립하고 나면 이후로는 Context 를 실행하기만 하면 된다.
// 스프링으로 어플리케이션을 개발할 때, 어플리케이션 로딩 시점에 의존관계 주입을 통해 필요한 의존관계를
// 모두 맺어두고 난 다음에 실제 요청을 처리하는 것과 같은 원리이다.
// 이 방식의 단점은 Context 와 Strategy 를 조립한 이후에는 전략을 변경하기가 번거롭다는 점이다.
// 물론 Context 에 Setter 를 제공해서 Strategy 를 받아 변경하면 되지만,
// Context 를 싱글톤으로 사용할 때는 동시성 이슈 등 고려할 점이 많다.
// 그래서 전략을 실시간으로 변경해야 하면 차라리 이전에 개발한 테스트 코드처럼
// Context 를 하나 더 생성하고 그곳에 다른 Strategy 를 주입하는 것이 더 나은 선택일 수 있다.
