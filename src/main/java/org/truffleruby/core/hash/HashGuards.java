/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.core.hash;

import com.oracle.truffle.api.object.DynamicObject;
import org.truffleruby.Layouts;
import org.truffleruby.language.RubyGuards;

public abstract class HashGuards {

    // Storage strategies

    public static boolean isNullHash(DynamicObject hash) {
        assert RubyGuards.isRubyHash(hash);
        return Layouts.HASH.getStore(hash) == null;
    }

    public static boolean isPackedHash(DynamicObject hash) {
        assert RubyGuards.isRubyHash(hash);
        // Can't do instanceof Object[] due to covariance
        final Object store = Layouts.HASH.getStore(hash);
        return store != null && store.getClass() == Object[].class;
    }

    public static boolean isBucketHash(DynamicObject hash) {
        assert RubyGuards.isRubyHash(hash);
        return Layouts.HASH.getStore(hash) instanceof Entry[];
    }

    // Higher level properties

    public static boolean isEmptyHash(DynamicObject hash) {
        assert RubyGuards.isRubyHash(hash);
        return Layouts.HASH.getSize(hash) == 0;
    }

    public static boolean isCompareByIdentity(DynamicObject hash) {
        assert RubyGuards.isRubyHash(hash);
        return Layouts.HASH.getCompareByIdentity(hash);
    }

}
