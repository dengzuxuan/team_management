package com.team.backend.utils.aop;

import cn.hutool.json.JSONObject;
import com.team.backend.utils.AtomicCounter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName GlobalActuator
 * @Description 全局接口监控
 * @Author Colin
 * @Date 2023/10/23 11:04
 * @Version 1.0
 */
@Component
@Aspect
public class GlobalActuator {
    private static final Logger log = LoggerFactory.getLogger(GlobalActuator.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //客户端发送的每次Http请求，对应的在服务端都会分配一个新的线程来处理，在处理过程中设计到的后端代码都属于同一个线程，可以通过ThreadLocal代码获取线程ID.
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    //concurrentHashMap是一个支持高并发更新与查询的哈希表(基于HashMap)。
    ConcurrentHashMap<Object,Object> countMap = new ConcurrentHashMap<Object,Object>();
    /**
     execution：这是一个切入点表达式的关键字，指定了要匹配方法执行的操作。
      *：这是通配符，匹配任意返回类型。
      com.team.backend.controller：这是包名，表示要匹配的目标方法所在的包。
      *Controller：这是类名通配符，表示匹配任何以 "Controller" 结尾的类名。
      *：这是方法名通配符，表示匹配任何方法名。
      (..)：这是参数列表通配符，表示匹配任何参数列表。
    **/
    @Pointcut("execution(* com.team.backend.controller.*.*(..))")
    private void controllerPt(){

    }

    @Before("com.team.backend.utils.aop.GlobalActuator.controllerPt()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        startTime.set(System.currentTimeMillis());
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        System.out.println("@"+System.currentTimeMillis());
    }

    @AfterReturning(returning = "returnVal", pointcut = "com.team.backend.utils.aop.GlobalActuator.controllerPt()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnVal) throws Throwable {

        Signature signature = joinPoint.getSignature();
        String declaringName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        String mapKey = declaringName + methodName;

        // 执行成功则计数加一
        int increase = AtomicCounter.getInstance().increase();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        synchronized (this) {
            //在项目启动时，需要在Redis中读取原有的接口请求次数
//            if (countMap.size() == 0) {
//                JSONObject jsonObject = RedisUtils.objFromRedis(StringConst.INTERFACE_ACTUATOR);
//                if (jsonObject != null) {
//                    Set<String> strings = jsonObject.keySet();
//                    for (String string : strings) {
//                        Object o = jsonObject.get(string);
//                        countMap.putIfAbsent(string, o);
//                    }
//                }
//            }
        }

        // 如果此次访问的接口不在countMap，放入countMap
        countMap.putIfAbsent(mapKey, 0);
        countMap.compute(mapKey, (key, value) -> (Integer) value + 1);


        synchronized (this) {
            // 内存计数达到30 更新redis
            if (increase == 30) {
                //RedisUtils.objToRedis(StringConst.INTERFACE_ACTUATOR, countMap, Constants.AVA_REDIS_TIMEOUT);
                //删除过期时间
                //stringRedisTemplate.persist(StringConst.INTERFACE_ACTUATOR);
                //计数器置为0
                AtomicCounter.getInstance().toZero();
            }
        }

        //log.info("方法执行次数:" + mapKey + "------>" + countMap.get(mapKey));
        //log.info("URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }


    /**
     * 当接口报错时执行此方法
     */
    @AfterThrowing(pointcut = "com.team.backend.utils.aop.GlobalActuator.controllerPt()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("接口访问失败，URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }
}
