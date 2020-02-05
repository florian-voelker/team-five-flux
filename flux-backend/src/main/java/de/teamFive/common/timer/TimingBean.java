package de.teamFive.common.timer;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.logging.Logger;

@Stateless
public class TimingBean {
    private static final Logger log = Logger.getLogger(TimingBean.class.getCanonicalName());
    private FuncI toBeCalled;
    @Resource
    private TimerService timerService;

    public Timer startTimer(Integer duration, FuncI func) {
        this.toBeCalled = func;
        TimerConfig config = new TimerConfig();
        return timerService.createSingleActionTimer(duration, config);
    }

    @Timeout
    public void execute(Timer timer) {
        log.info("Message sent to client");
        toBeCalled.func();
    }

    @FunctionalInterface
    public interface FuncI {
        void func();
    }

}
