package com.learn.hibernate.config;

import org.springframework.stereotype.Component;

/**
 * @author lee
 */
@Component
public class CurrentUser {
	/**
	 * 当前线程用户
	 */
	private static final ThreadLocal<CurrentUserDto> userHolder = new ThreadLocal<CurrentUserDto>();

	/**
	 * 设置当前线程的用户
	 * @param user
	 */
	public static void setCurrentUser(CurrentUserDto user) {
		userHolder.set(user);
	}

	/**
	 * 取得当前线程中的用户
	 */
	public static CurrentUserDto getCurrentUser() {
		return userHolder.get();
	}

	/**
	 * 清除当前线程用户
	 */
	public static void clearCurrentUser() {
		userHolder.remove();
	}

	public Object currentUser() {
		return getCurrentUser().getId();
	}
}
