/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * SerializeEnum.
 * @author xiaoyu
 */
@Getter
@RequiredArgsConstructor
public enum SerializeEnum {

    /**
     * Jdk serialize protocol enum.
     */
    JDK("jdk"),

    /**
     * Kryo serialize protocol enum.
     */
    KRYO("kryo"),

    /**
     * Hessian serialize protocol enum.
     */
    HESSIAN("hessian"),

    /**
     * Protostuff serialize protocol enum.
     */
    PROTOSTUFF("protostuff");

    private final String serialize;

    /**
     * Acquire serialize protocol serialize protocol enum.
     *
     * @param serialize the serialize protocol
     * @return the serialize protocol enum
     */
    public static SerializeEnum acquire(final String serialize) {
        Optional<SerializeEnum> serializeEnum =
                Arrays.stream(SerializeEnum.values())
                        .filter(v -> Objects.equals(v.getSerialize(), serialize))
                        .findFirst();
        return serializeEnum.orElse(SerializeEnum.KRYO);

    }
}
