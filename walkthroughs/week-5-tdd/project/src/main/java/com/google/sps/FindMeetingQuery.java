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


public final class FindMeetingQuery {
  // @param events: all of the events that exist so far 
  //                each event contains name, time range, and attendees
  // @param request: MeetingRequest containing event name, duration, and attendees

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> attendeeConflictTimes = findAttendeeEvents(events, request.getAttendees());
    Collections.sort(attendeeConflictTimes, TimeRange.ORDER_BY_START);
    ArrayList<TimeRange> possibleTimes = findPossibleTimes(attendeeConflictTimes, request.getDuration());

    return possibleTimes;
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

  private ArrayList<TimeRange> findPossibleTimes(ArrayList<TimeRange> existingTimes, long duration) {
    int previousTime = TimeRange.START_OF_DAY;
    ArrayList<TimeRange> possibleTimes = new ArrayList<>();

    for (TimeRange currentTime : existingTimes) {
      // if the difference between this start and the last end is long enough, add to list of times
      if ((currentTime.start() - previousTime) >= duration) {
        possibleTimes.add(TimeRange.fromStartEnd(previousTime, currentTime.start(), false));
      } 

      // Handle event overlap:
      // Move the previousTime forward only when the current event ending is later in the day
      if (currentTime.end() > previousTime) {
        previousTime = currentTime.end();
      } 
    }
    
    // add meeting time from the end of the last existing meeting to the end of day
    if (TimeRange.END_OF_DAY - previousTime > duration) {
      possibleTimes.add(TimeRange.fromStartEnd(previousTime, TimeRange.END_OF_DAY, true));
    }
    return possibleTimes;
  }
}
