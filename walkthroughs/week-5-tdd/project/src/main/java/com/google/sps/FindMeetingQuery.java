// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;

public final class FindMeetingQuery {
  // @param events: all of the events that exist so far 
  //                each event contains name, time range, and attendees
  // @param request: MeetingRequest containing event name, duration, and attendees

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<Event> overlapEvents = findAttendeeEvents(events, request.getAttendees());
    throw new UnsupportedOperationException("TODO: Implement this method.");
  }

  // return only the events that our attendees are going to
  private Collection<Event> findAttendeeEvents(Collection<Event> events, Collection<String> attendees) {
    Collection<Event> output = new ArrayList<>();
    for (Event e : events) {
      for (String a : attendees) {
        if (e.getAttendees().contains(a)) {
          output.add(e);
          break;
        }
      }
    }
    return output;
  }

  // overlay time blocks and find gaps?
  // check to see which of these gaps fits our duration
}
