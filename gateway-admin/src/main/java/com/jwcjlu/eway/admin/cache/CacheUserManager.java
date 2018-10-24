/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *//*


package com.jwcjlu.gateway.admin.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import UserService;
import UserDTO;
import com.jwcjlu.gateway.api.entity.DashBoardUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

*/
/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 *//*

@Component
public class CacheUserManager {

    private static final int MAX_COUNT = 10000;

    @Autowired
    private UserService userService;

    private final LoadingCache<String, DashBoardUser> LOADING_USER_CACHE = CacheBuilder.newBuilder()
            .maximumWeight(MAX_COUNT)
            .weigher((Weigher<String, DashBoardUser>) (string, dashBoardUser) -> getSize())
            .build(new CacheLoader<String, DashBoardUser>() {
                @Override
                public DashBoardUser load(final String userName) {
                    return cacheUser(userName);
                }
            });

    */
/**
     * this is a function.
     *
     * @return int
     *//*

    private int getSize() {
        return (int) LOADING_USER_CACHE.size();
    }

    */
/**
     * cache user.
     *
     * @param userName userName
     * @return com.jwcjlu.skyway.api.entity.DashBoardUser
     *//*

    private DashBoardUser cacheUser(final String userName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userName);
        return Optional
                .ofNullable(userService.findByName(userDTO).getDataList().get(0))
                .orElse(new DashBoardUser());

    }

}
*/
