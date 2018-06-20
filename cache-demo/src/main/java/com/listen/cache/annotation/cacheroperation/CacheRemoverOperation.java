/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.listen.cache.annotation.cacheroperation;

import org.springframework.cache.interceptor.CacheOperation;

public class CacheRemoverOperation extends AbstractCacheOperation {

	private final String key;


	public CacheRemoverOperation(CacheRemoverOperation.Builder b) {
		super(b);
		this.key = b.key;
	}


	public String getKey() {
		return key;
	}
	
	/**
	 * @since 4.3
	 */
	public static class Builder extends AbstractCacheOperation.Builder {
		
		private String key;
		
		public void setKey(String key) {
			this.key = key;
		}

		@Override
		protected StringBuilder getOperationDescription() {
			StringBuilder sb = super.getOperationDescription();
			sb.append(" | key='");
			sb.append(this.key);
			return sb;
		}

		@Override
		public CacheRemoverOperation build() {
			return new CacheRemoverOperation(this);
		}
	}

}
