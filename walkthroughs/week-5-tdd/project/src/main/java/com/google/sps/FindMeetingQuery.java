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
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

// general alg:
// find events that attendees are marked as attending now
// order by start time
// go to the end of the earliest event and check conflicts
// if no conflicts, mark time until the next conflict


public final class FindMeetingQuery {
  // @param events: all of the events that exist so far 
  //                each event contains name, time range, and attendees
  // @param request: MeetingRequest containing event name, duration, and attendees

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> overlapEvents = findAttendeeEvents(events, request.getAttendees());
    Collections.sort(overlapEvents, TimeRange.ORDER_BY_START);


    throw new UnsupportedOperationException("TODO: Implement this method.");
  }

  // return only the TimeRanges that our attendees are going to to find potential conflicts
  private ArrayList<TimeRange> findAttendeeEvents(Collection<Event> events, Collection<String> attendees) {
    ArrayList<TimeRange> output = new ArrayList<>();
    for (Event e : events) {
      for (String a : attendees) {
        if (e.getAttendees().contains(a)) {
          output.add(e.getWhen());
          break;
        }
      }
    }
    return output;
  }

  private Collection<TimeRange> removeConflicts(Collection<Event> events) {
    Collection<TimeRange> output = new ArrayList<>();
    return output;
  }

  // overlay time blocks and find gaps?
  // check to see which of these gaps fits our duration
}
