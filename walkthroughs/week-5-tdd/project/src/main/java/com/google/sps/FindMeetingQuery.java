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

  /** 
    * Create a Collection of TimeRange objects where calendar events can fulfill MeetingRequest
    * @param events all of the events that exist so far
    * @param request contains event name, duration, and attendees
    * @return <code>Collection<TimeRange></code> where meetings are appropriate
    */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> eventsForRequiredAttendees = 
        findAttendeeEvents(events, request.getAttendees());
    ArrayList<TimeRange> eventsForOptionalAttendees = 
        findAttendeeEvents(events, request.getOptionalAttendees());

    // Create an ArrayList of the two event types combined
    ArrayList<TimeRange> optionalAndRequiredEvents = new ArrayList<>();
    optionalAndRequiredEvents.addAll(eventsForRequiredAttendees);
    optionalAndRequiredEvents.addAll(eventsForOptionalAttendees); 

    Collections.sort(eventsForRequiredAttendees, TimeRange.ORDER_BY_START);
    Collections.sort(eventsForOptionalAttendees, TimeRange.ORDER_BY_START);
    Collections.sort(optionalAndRequiredEvents, TimeRange.ORDER_BY_START);

    // Return the time slots found to include all optional attendees if one is found 
    // or if there are no required attendee events
    ArrayList<TimeRange> possibleTimes = 
        findPossibleTimes(optionalAndRequiredEvents, request.getDuration());
    if (possibleTimes.size() != 0 || eventsForRequiredAttendees.size() == 0) {
      return possibleTimes;
    } else {
      // If no events satisfy optional attendee schedules, return the timeslots that work for 
      // mandatory attendees only
      return findPossibleTimes(eventsForRequiredAttendees, request.getDuration());
    }
  }

  /**
    * Determine possible TimeRanges that attendees are scheduled to attend to find potential 
    * conflicts
    * @param events Collection of Event objects
    * @param attendees Collection of Strings of attendee names
    */
  private ArrayList<TimeRange> findAttendeeEvents(
        Collection<Event> events, Collection<String> attendees) {
          
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

  /** 
    * Private method to determine possible event times
    * @param existingTimes ArrayList<TimeRange> of times that have been found from existing events
    * @param duration Duration given by the MeetingRequest
    */
  private ArrayList<TimeRange> findPossibleTimes(
        ArrayList<TimeRange> existingTimes, long duration) {

    int previousTime = TimeRange.START_OF_DAY;
    ArrayList<TimeRange> possibleTimes = new ArrayList<>();

    for (TimeRange currentTime : existingTimes) {
      // If the difference between this start and the last end is long enough, add to list of times
      if ((currentTime.start() - previousTime) >= duration) {
        possibleTimes.add(TimeRange.fromStartEnd(previousTime, currentTime.start(), false));
      } 

      // Handle event overlap:
      // Move the previousTime forward only when the current event ending is later in the day
      if (currentTime.end() > previousTime) {
        previousTime = currentTime.end();
      } 
    }
    
    // Add meeting time from the end of the last existing meeting to the end of day
    if (TimeRange.END_OF_DAY - previousTime > duration) {
      possibleTimes.add(TimeRange.fromStartEnd(previousTime, TimeRange.END_OF_DAY, true));
    }
    return possibleTimes;
  }
}
