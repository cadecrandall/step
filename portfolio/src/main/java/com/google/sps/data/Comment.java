package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import javax.servlet.http.HttpServletRequest;



/** Container to hold comment and metadata. */
public class Comment {

  public final String username;
  public final String subject;
  public final long timestamp;
  public final String message;

  private static final String USERNAME_PROPERTY = "username";
  private static final String SUBJECT_PROPERTY = "subject";
  private static final String TIMESTAMP_PROPERTY = "timestamp";
  private static final String MESSAGE_PROPERTY = "message";

  private static final String COMMENT_FORM_ID = "comment-message";
  private static final String USERNAME_FORM_ID = "username";
  private static final String SUBJECT_FORM_ID = "subject";

  private static final String COMMENT_ENTITY = "Comment";

  public Comment(Entity entity) {
    this.username = (String) entity.getProperty(USERNAME_PROPERTY);
    this.subject = (String) entity.getProperty(SUBJECT_PROPERTY);
    this.timestamp = (long) entity.getProperty(TIMESTAMP_PROPERTY);
    this.message = (String) entity.getProperty(MESSAGE_PROPERTY);
  }

  public Comment(HttpServletRequest request) {
    this.username = request.getParameter(USERNAME_FORM_ID);
    this.subject = request.getParameter(SUBJECT_FORM_ID);
    this.timestamp = System.currentTimeMillis();
    this.message = request.getParameter(COMMENT_FORM_ID);
  }

  /** Return an Entity for DataStore */
  public Entity toEntity() {
    Entity commentEntity = new Entity(COMMENT_ENTITY);
    commentEntity.setProperty(USERNAME_PROPERTY, this.username);
    commentEntity.setProperty(SUBJECT_PROPERTY, this.subject);
    commentEntity.setProperty(MESSAGE_PROPERTY, this.message);
    commentEntity.setProperty(TIMESTAMP_PROPERTY, this.timestamp);
    return commentEntity;
  }
}