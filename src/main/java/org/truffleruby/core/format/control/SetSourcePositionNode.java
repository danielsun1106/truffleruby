/*
 * Copyright (c) 2016, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.core.format.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;
import org.truffleruby.core.format.FormatNode;
import org.truffleruby.core.format.exceptions.OutsideOfStringException;
import org.truffleruby.core.format.exceptions.RangeException;

public class SetSourcePositionNode extends FormatNode {

    private final int position;

    private final ConditionProfile rangeProfile = ConditionProfile.createBinaryProfile();

    public SetSourcePositionNode(int position) {
        this.position = position;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (rangeProfile.profile(position > getSourceLength(frame))) {
            throw new OutsideOfStringException();
        }

        if (rangeProfile.profile(position < 0)) {
            throw new RangeException("pack length too big");
        }

        setSourcePosition(frame, position);
        return null;
    }

}
