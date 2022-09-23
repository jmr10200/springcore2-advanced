package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 동시성 문제 - 예제코드
 */
@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };

        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");

        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start(); // A 실행
        sleep(2000); // 동시성 문제 발생 X

        // thread-A 작업이 끝나기전에 thread-B 를 호출하면 동시성 문제가 발생한다.
//        sleep(100); // 동시성 문제 발생 O
        threadB.start(); // B 실행

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/* 동시성 문제 */
// [thread-A] INFO ...code.FieldService - 저장 name=userA -> nameStore=null
// [thread-B] INFO ...code.FieldService - 저장 name=userB -> nameStore=userA
// [thread-A] INFO ...code.FieldService - 조회 nameStore=userB
// [thread-B] INFO ...code.FieldService - 조회 nameStore=userB

// Thread-A : 저장한 데이터와 조회한 데이터가 불일치 하는 문제가 발생한다.
// 이처럼 여러 쓰레드가 동시에 같은 인스턴스의 필드 값을 변경하면서 발생하는 문제를 동시성 문제라 한다.
// 여러 쓰레드가 같은 인스턴스에 접근했을 때 발생하기 때문에 트래픽이 많아질 수 록 자주 발생한다.
// 스프링 빈처럼 싱글톤 객체의 필드를 변경하여 사용할 때 주의해야 한다.

// 참고
// 동시성 문제는 지역변수에서는 발생하지 않는다.
// 지역변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
// 동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용 필드에 접근할 때 발생한다.
// 동시성 문제는 값을 읽기만 하면 발생하지 않는다.
// 어디선가 값을 변경하기 떄문에 발생한다.

// 해결방안? 쓰레드로컬