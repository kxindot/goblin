package com.kxindot.goblin.concurrent;

import static com.kxindot.goblin.concurrent.Completables.State.COMPLETED;
import static com.kxindot.goblin.concurrent.Completables.State.EXCEPTION;
import static com.kxindot.goblin.concurrent.Completables.State.TIMEOUT;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Completable}任务集合。
 * 
 * @author ZhaoQingJiang
 */
public class Completables {

	private State state;
	private List<Completable> tasks;
	
	Completables(List<Completable> tasks) {
		this.state = State.COMPLETED;
		this.tasks = tasks;
	}
	
	/**
	 * 任务集执行是否完成。
	 * 
	 * @return boolean
	 */
	public boolean isCompleted() {
		return state == COMPLETED;
	}
	
	/**
	 * 任务集执行是否超时。
	 * 
	 * @return boolean
	 */
	public boolean isTimeout() {
		return state == TIMEOUT;
	}
	
	/**
	 * 任务集执行是否异常。
	 * 
	 * @return boolean
	 */
	public boolean isException() {
		return state == EXCEPTION;
	}
	
	/**
	 * 任务数量。
	 * 
	 * @return int
	 */
	public int size() {
		return tasks.size();
	}
	
	/**
	 * 获取任务。
	 * 
	 * @param index int
	 * @return Completable
	 */
	public Completable get(int index) {
		return tasks.get(index);
	}
	
	/**
	 * 获取第一个执行超时的任务。若无则返回null。
	 * 
	 * @return Completable
	 */
	public Completable getFirstTimeout() {
		return size() < 1 ? null : tasks.stream().filter(Completable::isTimeout).findFirst().orElse(null);
	}
	
	/**
	 * 获取第一个执行异常的任务。若无则返回null。
	 * 
	 * @return Completable
	 */
	public Completable getFirstException() {
		return size() < 1 ? null : tasks.stream().filter(Completable::isError).findFirst().orElse(null);
	}
	
	/**
	 * 获取执行超时任务列表。
	 * 
	 * @return {@code List<Completable>}
	 */
	public List<Completable> listTimeouts() {
		return tasks.stream().filter(Completable::isTimeout).collect(Collectors.toList());
	}
	
	/**
	 * 获取执行异常任务列表。
	 * 
	 * @return {@code List<Completable>}
	 */
	public List<Completable> listExceptions() {
		return tasks.stream().filter(Completable::isError).collect(Collectors.toList());
	}
	
	/**
	 * 设置任务集执行状态。
	 * 
	 * @param state State
	 */
	void setState(State state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return tasks.stream().map(Completable::toString).collect(Collectors.joining("\n"));
	}
	
	/**
	 * 任务执行状态。
	 * 
	 * @author ZhaoQingJiang
	 */
	enum State {

		COMPLETED, TIMEOUT, EXCEPTION
		
	}
	
}
