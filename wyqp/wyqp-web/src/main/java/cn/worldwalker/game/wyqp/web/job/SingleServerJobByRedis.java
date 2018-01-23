package cn.worldwalker.game.wyqp.web.job;


import java.lang.management.ManagementFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import cn.worldwalker.game.wyqp.common.utils.ApplicationContextUtil;
import cn.worldwalker.game.wyqp.common.utils.IPUtil;
import cn.worldwalker.game.wyqp.common.utils.redis.JedisTemplate;
import cn.worldwalker.game.wyqp.common.utils.redis.JedisTemplate.JedisAction;

/**
 * 试用范围:强调分布式，对频率不敏感
 * 单机任务执行，只保证同一时间点只有一个任务执行，不保证严格安装固定频率执行
 * 因每台机器的时钟可能存在几秒甚至几分的不一致，所以会发生间隔内被执行多次的情况
 */
public abstract class SingleServerJobByRedis{
	protected static final Logger logger = Logger.getLogger(SingleServerJobByRedis.class);

    public static String RUNING_FLAG = "1";

    private String jobKeyPrefix = "wyqp.SingleServerJob_";

    protected String jobName = getClass().getSimpleName();

    /**
     * JOB独占任务的最长时间 单位s，可以取任务执行时间的预估最长时间，设置该值是保证该时间段内
     * 只有一个任务执行
     */
    protected int maxOwnSecond = 30;

    /**
     * job 独占任务最少时间 单元s,可以取实际环境允许的最小频率值，
     * 若实际执行时间小于该值，该机器依旧占用直到最小时间，设置该值主要
     * 是降低间隔内被执行多次问题
     */
    protected int minOwnSecond = 10;

    public Boolean success = false;// 是否抢占成功
    @Autowired
    protected JedisTemplate jedisTemplate;


    public void doTask() {
       
		long start = System.currentTimeMillis();
		String value=IPUtil.getLocalIp()+"_"+start;
		if (isRunning(value)) {
			logger.info(getJobKey()+";value ="+value+"  get lock");
			try {
				execute(); //必须同步
				long cost = (System.currentTimeMillis() - start) / 1000;
				long min = (minOwnSecond * 1000) - cost;
				if (min > 0) { // 强制占用
					Thread.sleep(min);
				}
			} catch (Exception e) {
				logger.error("task error",e);
			}
			unlock(value);
		}

    }
    
    /**
     * 具体任务执行逻辑，由实现类去实现
     */
    public abstract void execute();

    private String getDesc() {
        try {
            return "[jobName=" + jobName + " ip=" + IPUtil.getLocalIp() + " jvmId=" + getJvmPid() + "]"
                    + ApplicationContextUtil.ctx.getId()
                    + " :";
        } catch (Exception e) {
            return "";
        }
    }

    public String getJobKey() {
        return jobKeyPrefix + jobName;
    }


    private synchronized boolean  isRunning(String value) {
        try {
            long ttl = getKeyTtl();

            if (ttl > maxOwnSecond || ttl < 0) { //TTL 可能返回-1 -2
                unlock(value); // 防呆,防止失效时间没设置成功
            }

            if (jedisTemplate.setnx(getJobKey(), value)) {
                expire(getJobKey(), maxOwnSecond);
                
                return true;
            }
            return false;
        } catch (Exception e) {
        	logger.error(getDesc() + " error",e);
            return false;
        }
    }

    private Boolean expire(final String key, final int timeOut) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                int maxRetry = 5;
                for (int i = 0; i < maxRetry; i++) {

                    if (jedis.expire(key, timeOut) == 1l) {
                        return true;

                    }
                }
                logger.error(getDesc() + " expire retry 5 times failure");
                return false;
            }
        });
    }


    public void unlock(String v) {
        try {
        	String s=jedisTemplate.get(getJobKey());
            if (s != null && s.equals(v)) {
                jedisTemplate.del(getJobKey());

            }
        } catch (Exception e) {
        	logger.error("unlock error",e);
            jedisTemplate.del(getJobKey());
        }
    }
    
    public static String getJvmPid() {
		String jvmPid = "";
		try {
			jvmPid = ManagementFactory.getRuntimeMXBean().getName();
		} catch (Exception e) {
			if(logger.isDebugEnabled()) {
				logger.debug("Get jvm pid failed!!!");
			}
		}
		return jvmPid;
	}

    public String getJobName() {
        return jobName;
    }


    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobKeyPrefix() {
        return jobKeyPrefix;
    }

    public int getMaxOwnSecond() {
        return maxOwnSecond;
    }

    public int getMinOwnSecond() {
        return minOwnSecond;
    }

    public void setJobKeyPrefix(String jobKeyPrefix) {
        this.jobKeyPrefix = jobKeyPrefix;
    }

    public void setMaxOwnSecond(int maxOwnSecond) {
        this.maxOwnSecond = maxOwnSecond;
    }

    public void setMinOwnSecond(int minOwnSecond) {
        this.minOwnSecond = minOwnSecond;
    }

    public JedisTemplate getJedisTemplate() {
        return jedisTemplate;
    }


    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }


	private long getKeyTtl() {
		return jedisTemplate.execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.ttl(getJobKey());
			}
		});
	}
}
