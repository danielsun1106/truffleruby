#!/usr/bin/env ruby

#
# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
# This code is released under a tri EPL/GPL/LGPL license. You can use it,
# redistribute it and/or modify it under the terms of the:
#
# Eclipse Public License version 1.0
# GNU General Public License version 2
# GNU Lesser General Public License version 2.1
#

require 'erb'

types = {
  'void*' => "0", 'void' => nil,
  'bool' => 'false', 'int' => '0', 'long' => '0', 'char' => "'0'",
  'int8_t' => '0', 'int16_t' => '0', 'int32_t' => '0', 'int64_t' => '0',
  'uint8_t' => '0', 'uint16_t' => '0', 'uint32_t' => '0', 'uint64_t' => '0',
  'float' => '0.0', 'double' => '0.0',
  'constchar*' => '""',
}

methods = []

lines = IO.readlines("lib/cext/include/sulong/truffle.h") + IO.readlines("lib/cext/include/sulong/polyglot.h")
lines.each do |l|
   match = !l.start_with?('//') && !l.start_with?(' *') && /^(.+?)\b(truffle|polyglot)(.+)\)(?=;)/.match(l)
   if match
     ret = types.fetch(match[1].gsub(' ', '')) { |t| raise "unknown type: `#{t}` for line `#{l}`" }
     methods << {:met => match[0], :ret => ret}
   end
end

File.write('src/main/c/sulongmock/sulongmock.c', ERB.new(<<TRC).result)
/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 *
 */

// This file is automatically generated by ruby tool/generate-sulongmock.rb

#include <stdio.h>
#include <stdint.h>
#include <sulong/truffle.h>

void rb_tr_mock() {
  fprintf(stderr, "Warning: Mock method called in sulongmock\\n");
  abort();
}
<% methods.each do |m| %>
<%= m[:met] %> {
  rb_tr_mock();<% if m[:ret] %>
  return <%= m[:ret] %>;<% end %>
}
<% end %>

TRC
