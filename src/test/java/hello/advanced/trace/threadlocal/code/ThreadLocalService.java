package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        // 파라미터로 넘어온 name 을 nameStore(쓰레드로컬) 에 저장
        nameStore.set(name);
        // 1초간 쉰다음
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        // 쓰레드로컬에 저장된 nameStore 리턴
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
/* ThreadLocal 사용법 */
// 저장 : ThreadLocal.set(xxx)
// 조회 : ThreadLocal.get()
// 삭제 : ThreadLocal.remove()

// 주의
// 해당 쓰레드가 쓰레드로컬을 모두 사용하고 나면 제거해주어야 한다!!
// ThreadLocal.remove() 을 호출