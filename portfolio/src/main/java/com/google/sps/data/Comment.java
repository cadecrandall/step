package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;


/** Container to hold comment and metadata. */
public class Comment {

  private final String username;
  private final String subject;
  private final long timestamp;
  private final String message;

  private static final String USERNAME_PROPERTY = "username";
  private static final String SUBJECT_PROPERTY = "subject";
  private static final String TIMESTAMP_PROPERTY = "timestamp";
  private static final String MESSAGE_PROPERTY = "message";

  public Comment(Entity entity) {
    this.username = (String) entity.getProperty(USERNAME_PROPERTY);
    this.subject = (String) entity.getProperty(SUBJECT_PROPERTY);
    this.timestamp = (long) entity.getProperty(TIMESTAMP_PROPERTY);
    this.message = (String) entity.getProperty(MESSAGE_PROPERTY);
  }
}