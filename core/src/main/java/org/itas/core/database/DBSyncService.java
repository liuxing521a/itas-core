package org.itas.core.database;

import org.itas.core.Builder;
import org.itas.core.DBSync;
import org.itas.core.Service;
import org.itas.util.ItasException;
import org.itas.util.Logger;
import org.itas.util.Utils.TimeUtil;

/**
 * <p>环写数据库线程</p>
 * @author liuzhen<liuxing521a@163.com>
 * @date 2014-3-17
 */
final class DBSyncService implements Runnable, Service {
	
  DBSyncService(DBSync sync, long interval) {
    this.sync = (DBSyncImpl)sync;
    this.interval = interval;
    this.lastTime  = TimeUtil.systemTime();
    this.worker = new Thread("persistent DB");
    this.worker.setDaemon(true);
  }
	
  /** 线程运行标记 */
  private boolean isFlag;
  
  /** 上次同步数据库时间  */
  private long lastTime;
  
  /** 数据库同步者 */
  private final DBSyncImpl sync;
	
  /** 同步间隔时间  */
  private final long interval;

  /** 执行线程*/
  private final Thread worker;

	
  @Override
  public void startUP() {
	if (worker.isAlive()) {
	  throw new ItasException("thread is alive");
    }
    	
	isFlag = false;
	worker.start();
  }

  @Override
  public void shutDown() {
    isFlag = true;
  }

  @Override
  public void run() {
	synchronized (worker) {
	  while (!isFlag) {
	    try {
		  if (TimeUtil.systemTime() - lastTime > interval) {
		    sync.doPersistent();
			lastTime = TimeUtil.systemTime();
		  }

		  Thread.sleep(interval);
	    } catch(InterruptedException exception) {
		  // do nothing
	    } catch (Throwable e) {
	      Logger.error("", e);
	    }
	  }
	}
  }
  
  public static class DBSyncThreadBuilder implements Builder {

	private DBSync sync;
	
	private long interval;
	
	public DBSyncThreadBuilder setSync(DBSync sync) {
	  this.sync = sync;
	  return this;
	}

	public DBSyncThreadBuilder setInterval(long interval) {
	  this.interval = interval;
	  return this;
	}

	@Override
	public DBSyncService builder() {
	  return new DBSyncService(this.sync, this.interval);
	}
	  
  }
	
}
